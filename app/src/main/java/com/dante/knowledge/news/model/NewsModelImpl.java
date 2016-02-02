package com.dante.knowledge.news.model;

import com.dante.knowledge.net.API;
import com.dante.knowledge.net.GsonUtil;
import com.dante.knowledge.net.Net;
import com.dante.knowledge.news.other.OnLoadDetailListener;
import com.dante.knowledge.news.other.OnLoadNewsListener;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by yons on 16/1/29.
 */
public class NewsModelImpl implements NewsModel {

    private String date;

    @Override
    public void getNews(int type, final OnLoadNewsListener listener) {
        if (type == API.TYPE_LATEST){
            Net.get(API.NEWS_LATEST, new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    listener.onFailure("load news failed", e);
                }

                @Override
                public void onResponse(String response) {
                    LatestNews news= GsonUtil.parseNews(response);
                    listener.onNewsSuccess(news);
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
                    LatestNews news= GsonUtil.parseNews(response);
                    listener.onNewsSuccess(news);
                    date= news.getDate();
                }
            });
        }
    }



    @Override
    public void getNewsDetail(StoryEntity story, final OnLoadDetailListener listener) {
        Net.get(API.BASE_URL + story.getId(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                listener.onFailure("load before failed", e);
            }

            @Override
            public void onResponse(String response) {
                DetailNews detailNews =GsonUtil.parseDetail(response);
                listener.onDetailSuccess(detailNews);
            }
        });
    }

}
