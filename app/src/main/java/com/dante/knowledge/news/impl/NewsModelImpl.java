package com.dante.knowledge.news.impl;

import com.dante.knowledge.bean.LatestNews;
import com.dante.knowledge.net.API;
import com.dante.knowledge.net.GsonUtil;
import com.dante.knowledge.net.Net;
import com.dante.knowledge.news.inter.NewsModel;
import com.dante.knowledge.news.listener.OnLoadNewsListener;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by yons on 16/1/29.
 */
public class NewsModelImpl implements NewsModel {

    private String date;

    @Override
    public void loadNews(int type, final OnLoadNewsListener listener) {
        if (type == API.TYPE_LATEST){
            Net.get(API.NEWS_LATEST, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onFailure("load news failed", e);
                }

                @Override
                public void onResponse(String response) {
                    LatestNews news= GsonUtil.deserialized(response);
                    listener.onTopSuccess(news.getTop_stories());
                    listener.onStoriesSuccess(news.getStories());
                    date= news.getDate();
                }
            });

        }else if (type ==API.TYPE_BEFORE){

            Net.get(API.NEWS_BEFORE+ date, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onFailure("load before failed", e);
                }

                @Override
                public void onResponse(String response) {
                    LatestNews news= GsonUtil.deserialized(response);
                    listener.onStoriesSuccess(news.getStories());
                    date= news.getDate();
                }
            });
        }
    }
}
