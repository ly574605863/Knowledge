package com.dante.knowledge.mvp.model;


import com.dante.knowledge.mvp.interf.NewsModel;
import com.dante.knowledge.mvp.interf.OnLoadDataListener;
import com.dante.knowledge.mvp.interf.OnLoadDetailListener;
import com.dante.knowledge.net.API;
import com.dante.knowledge.net.DB;
import com.dante.knowledge.net.Json;
import com.dante.knowledge.net.Net;
import com.dante.knowledge.utils.Constants;
import com.dante.knowledge.utils.SPUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.Call;

/**
 * deals with the zhihu news' data work
 */
public class ZhihuModel implements NewsModel<ZhihuStory, ZhihuDetail> {

    private String date;
    private long lastGetTime;
    public static final int GET_DURATION = 2000;
    private int type;

    @Override
    public void getNews(final int type, final OnLoadDataListener listener) {
        this.type = type;

        lastGetTime = System.currentTimeMillis();
        final StringCallback callback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (System.currentTimeMillis() - lastGetTime < GET_DURATION) {
                    getData(this);
                    return;
                }
                listener.onFailure("load zhihu news failed");
            }

            @Override
            public void onResponse(String response) {
                ZhihuJson zhihuJson = Json.parseZhihuNews(response);
                update(zhihuJson);
                date = zhihuJson.getDate();
                if (type == API.TYPE_BEFORE) {
                    SPUtil.save(Constants.DATE, date);
                }
                listener.onSuccess();
            }
        };

        getData(callback);
    }

    private void update(final ZhihuJson zhihuJson) {
        if (null != zhihuJson) {
            DB.realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    if (type == API.TYPE_LATEST) {
                        realm.where(ZhihuTop.class).findAll().clear();
                    }
                    realm.copyToRealmOrUpdate(zhihuJson);
                    setupStories(realm);
                }
            });
        }

    }

    private void setupStories(Realm realm) {
        RealmResults<ZhihuJson> list = realm.where(ZhihuJson.class).findAllSorted(Constants.DATE, Sort.DESCENDING);
    }

    private void getData(StringCallback callback) {
        if (type == API.TYPE_LATEST) {
            Net.get(API.NEWS_LATEST, callback, API.TAG_ZHIHU);

        } else if (type == API.TYPE_BEFORE) {
            date = DB.findAll(ZhihuJson.class).last().getDate();
            Net.get(API.NEWS_BEFORE + date, callback, API.TAG_ZHIHU);
        }
    }


    @Override
    public void getNewsDetail(final ZhihuStory newsItem, final OnLoadDetailListener<ZhihuDetail> listener) {
        requestData(newsItem, listener);
    }

    private void requestData(final ZhihuStory newsItem, final OnLoadDetailListener<ZhihuDetail> listener) {
        lastGetTime = System.currentTimeMillis();

        StringCallback callback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (System.currentTimeMillis() - lastGetTime < GET_DURATION) {
                    Net.get(API.BASE_URL + newsItem.getId(), this, API.TAG_ZHIHU);
                    return;
                }
                listener.onFailure("load zhihu detail failed", e);
            }

            @Override
            public void onResponse(String response) {
                ZhihuDetail detailNews = Json.parseZhihuDetail(response);
                DB.saveOrUpdate(detailNews);
                listener.onDetailSuccess(detailNews);
            }
        };
        Net.get(API.BASE_URL + newsItem.getId(), callback, API.TAG_ZHIHU);
    }


}
