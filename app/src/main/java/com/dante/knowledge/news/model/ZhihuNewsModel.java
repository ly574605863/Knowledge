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

    @Override
    public void getNews(int type, final OnLoadNewsListener<ZhihuNews> listener) {
        if (type == API.TYPE_LATEST) {
            Net.get(API.NEWS_LATEST, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onFailure("load zhihu news failed", e);
                }

                @Override
                public void onResponse(String response) {
                    ZhihuNews news = GsonUtil.parseZhihuNews(response);
                    listener.onNewsSuccess(news);
                    date = news.getDate();
                }
            }, API.TAG_ZHIHU_LATEST);

        } else if (type == API.TYPE_BEFORE) {

            Net.get(API.NEWS_BEFORE + date, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onFailure("load zhihu before news failed", e);
                }

                @Override
                public void onResponse(String response) {
                    ZhihuNews news = GsonUtil.parseZhihuNews(response);
                    listener.onNewsSuccess(news);
                    date = news.getDate();
                }
            }, API.TAG_ZHIHU_BEFORE);
        }
    }


    @Override
    public void getNewsDetail(ZhihuItem newsItem, final OnLoadDetailListener<ZhihuDetail> listener) {
        Net.get(API.BASE_URL + newsItem.getId(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                listener.onFailure("load zhihu detail failed", e);
            }

            @Override
            public void onResponse(String response) {
                ZhihuDetail detailNews = GsonUtil.parseZhihuDetail(response);
                listener.onDetailSuccess(detailNews);
            }
        }, API.TAG_ZHIHU_DETAIL);
    }

}
