package com.dante.knowledge.news.model;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.dante.knowledge.net.API;
import com.dante.knowledge.net.Constants;
import com.dante.knowledge.net.DB;
import com.dante.knowledge.net.Json;
import com.dante.knowledge.net.Net;
import com.dante.knowledge.news.interf.NewsModel;
import com.dante.knowledge.news.interf.OnLoadDetailListener;
import com.dante.knowledge.news.interf.OnLoadNewsListener;
import com.dante.knowledge.utils.StringUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Date;

import io.realm.Realm;
import okhttp3.Call;

/**
 * deals with the zhihu news' data work
 */
public class ZhihuNewsModel implements NewsModel<ZhihuItem, ZhihuNews, ZhihuDetail> {

    private String date;
    private long lastGetTime;
    private Context context;
    public static final int GET_DURATION = 2000;
    private int type;
    private boolean isBefore;

    public ZhihuNewsModel(Context context) {
        this.context = context;
    }

    public void init() {
        isBefore = false;
    }

    @Override
    public void getNews(final int type, final OnLoadNewsListener<ZhihuNews> listener) {
        this.type = type;
        if (!Net.isOnline(context)) {
            if (getFromDB(listener)) return;
        }

        lastGetTime = System.currentTimeMillis();
        final StringCallback callback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (System.currentTimeMillis() - lastGetTime < GET_DURATION) {
                    getData(this);
                    return;
                } else {
                    if (getFromDB(listener)) return;
                }
                listener.onFailure("load zhihu news failed", e);
            }

            @Override
            public void onResponse(String response) {
                ZhihuNews news = Json.parseZhihuNews(response);
                date = news.getDate();
                if (type ==API.TYPE_BEFORE) {
                    addFooter(news);
                }
                DB.save(news);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                editor.putString(Constants.DATE, date);
                editor.apply();
                listener.onNewsSuccess(news);
            }
        };

        getData(callback);
    }

    private boolean getFromDB(OnLoadNewsListener<ZhihuNews> listener) {
        String date = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(Constants.DATE,
                        StringUtil.parseStandardDate(new Date()));

        if (isBefore && type == API.TYPE_BEFORE) {
            date = StringUtil.lastDay(date);
        }

        ZhihuNews zhihuNews = DB.getZhihuNews(date);

        if (null != zhihuNews) {
            isBefore = true;
            addFooter(zhihuNews);
            listener.onNewsSuccess(zhihuNews);
            return true;
        }
        return false;
    }

    private void addFooter(ZhihuNews zhihuNews) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Log.i("test", "add "+zhihuNews.getDate());

        realm.copyToRealmOrUpdate(new ZhihuItem(Integer.valueOf(zhihuNews.getDate()), 1));
        realm.commitTransaction();
    }

    private void getData(StringCallback callback) {
        if (type == API.TYPE_LATEST) {
            Net.get(API.NEWS_LATEST, callback, API.TAG_ZHIHU);

        } else if (type == API.TYPE_BEFORE) {
            Net.get(API.NEWS_BEFORE + date, callback, API.TAG_ZHIHU);
        }
    }


    @Override
    public void getNewsDetail(final ZhihuItem newsItem, final OnLoadDetailListener<ZhihuDetail> listener) {
        if (getDetailFromDB(newsItem, listener)) return;

        requestData(newsItem, listener);
    }

    private boolean getDetailFromDB(ZhihuItem newsItem, OnLoadDetailListener<ZhihuDetail> listener) {
        ZhihuDetail detailNews = DB.getById(newsItem.getId(), ZhihuDetail.class);
        if (null != detailNews) {
            listener.onDetailSuccess(detailNews);
            return true;
        }
        return false;
    }

    private void requestData(final ZhihuItem newsItem, final OnLoadDetailListener<ZhihuDetail> listener) {
        lastGetTime = System.currentTimeMillis();

        StringCallback callback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (System.currentTimeMillis() - lastGetTime < GET_DURATION) {
                    Net.get(API.BASE_URL + newsItem.getId(), this, API.TAG_ZHIHU);
                    return;
                } else {
                    if (getDetailFromDB(newsItem, listener)) return;
                }
                listener.onFailure("load zhihu detail failed", e);
            }

            @Override
            public void onResponse(String response) {
                ZhihuDetail detailNews = Json.parseZhihuDetail(response);
                DB.save(detailNews);
                listener.onDetailSuccess(detailNews);
            }
        };
        Net.get(API.BASE_URL + newsItem.getId(), callback, API.TAG_ZHIHU);
    }


}
