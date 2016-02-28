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

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.Call;

/**
 * deals with the zhihu news' data work
 */
public class ZhihuModel implements NewsModel<ZhihuStory, ZhihuJson, ZhihuDetail> {

    private String date;
    private long lastGetTime;
    public static final int GET_DURATION = 2000;
    private int type;

    @Override
    public void getNews(final int type, final OnLoadDataListener<ZhihuJson> listener) {
        this.type = type;

        lastGetTime = System.currentTimeMillis();
        final StringCallback callback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (System.currentTimeMillis() - lastGetTime < GET_DURATION) {
                    getData(this);
                    return;
                }
                listener.onFailure("load zhihu news failed", e);
            }

            @Override
            public void onResponse(String response) {
                ZhihuJson zhihuJson = Json.parseZhihuNews(response);
                DB.findAllDateSorted(ZhihuJson.class);
                update(zhihuJson);
                date = zhihuJson.getDate();
                SPUtil.save(Constants.DATE, date);
                listener.onSuccess(zhihuJson);
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
                        //We use latest top items, so just delete old ones.
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
        List<ZhihuStory> zhihuStories = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ZhihuJson zhihuJson = list.get(i);
//            //this item is to show date as header
//            // so just new it with the date as id
//            // and the type is 1 (default is 0)
            zhihuStories.add(new ZhihuStory(Integer.valueOf(zhihuJson.getDate()) - 1, 1));
            zhihuStories.addAll(zhihuJson.getStories());
        }
        realm.copyToRealmOrUpdate(zhihuStories);
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
