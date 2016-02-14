package com.dante.knowledge.news.model;

import com.dante.knowledge.net.API;
import com.dante.knowledge.net.GsonUtil;
import com.dante.knowledge.net.Net;
import com.dante.knowledge.news.interf.NewsModel;
import com.dante.knowledge.news.interf.OnLoadDetailListener;
import com.dante.knowledge.news.interf.OnLoadNewsListener;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * deals with the zhihu news' data work
 */
public class ZhihuNewsModel implements NewsModel<ZhihuItem, ZhihuNews, ZhihuDetail> {

    private String date;
    private long lastGetTime;
    public static final int GET_DURATION = 2000;

    @Override
    public void getNews(final int type, final OnLoadNewsListener<ZhihuNews> listener) {
        lastGetTime = System.currentTimeMillis();

        final StringCallback callback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (System.currentTimeMillis() - lastGetTime < GET_DURATION) {
                    getData(type, this);
                    return;
                }
                listener.onFailure("load zhihu news failed", e);
            }

            @Override
            public void onResponse(String response) {
                ZhihuNews news = GsonUtil.parseZhihuNews(response);
                listener.onNewsSuccess(news);
                date = news.getDate();
            }
        };

        getData(type, callback);
    }

    private void getData(int type, StringCallback callback) {
        if (type == API.TYPE_LATEST) {
            Net.get(API.NEWS_LATEST, callback, API.TAG_ZHIHU_LATEST);

        } else if (type == API.TYPE_BEFORE) {
            Net.get(API.NEWS_BEFORE + date, callback, API.TAG_ZHIHU_BEFORE);
        }
    }


    @Override
    public void getNewsDetail(final ZhihuItem newsItem, final OnLoadDetailListener<ZhihuDetail> listener) {
        lastGetTime = System.currentTimeMillis();

        StringCallback callback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (System.currentTimeMillis() - lastGetTime < GET_DURATION) {
                    Net.get(API.BASE_URL + newsItem.getId(), this, API.TAG_ZHIHU_DETAIL);
                    return;
                }
                listener.onFailure("load zhihu detail failed", e);
            }

            @Override
            public void onResponse(String response) {
                ZhihuDetail detailNews = GsonUtil.parseZhihuDetail(response);
                listener.onDetailSuccess(detailNews);
            }
        };
        Net.get(API.BASE_URL + newsItem.getId(), callback, API.TAG_ZHIHU_DETAIL);
    }

}
