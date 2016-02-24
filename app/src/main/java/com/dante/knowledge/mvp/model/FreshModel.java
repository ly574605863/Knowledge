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

import okhttp3.Call;

/**
 * deals with the fresh news' data work
 */
public class FreshModel implements NewsModel<FreshPost, FreshJson, FreshDetailJson> {
    /**
     * clear page record to zero and start new request
     */
    public static final int TYPE_FRESH = 0;
    /**
     * a continuous request with increasing one page each time
     */
    public static final int TYPE_CONTINUOUS = 1;

    private int page;
    private long lastGetTime;
    public static final int GET_DURATION = 3000;

    @Override
    public void getNews(int type, final OnLoadDataListener<FreshJson> listener) {

        lastGetTime = System.currentTimeMillis();
        if (type == TYPE_FRESH) {
            page = 1;//如果是全新请求，就初始化page为1
        }
        getFreshNews(listener);
    }

    private void getFreshNews(final OnLoadDataListener<FreshJson> listener) {
        StringCallback callback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (System.currentTimeMillis() - lastGetTime < GET_DURATION) {
                    Net.get(API.FRESH_NEWS + page, this, API.TAG_FRESH);
                    return;
                }
                listener.onFailure("load fresh news failed", e);
            }

            @Override
            public void onResponse(String response) {
                FreshJson news = Json.parseFreshNews(response);
                DB.saveList(news.getPosts());
                SPUtil.save(Constants.PAGE, page);
                listener.onSuccess(news);
                page++;
            }
        };

        Net.get(API.FRESH_NEWS + page, callback, API.TAG_FRESH);
    }

    @Override
    public void getNewsDetail(final FreshPost freshPost, final OnLoadDetailListener<FreshDetailJson> listener) {
        if (getDetailFromDB(freshPost, listener)) return;

        requestData(freshPost, listener);
    }

    private boolean getDetailFromDB(FreshPost freshPost, OnLoadDetailListener<FreshDetailJson> listener) {
        FreshDetail post = DB.getById(freshPost.getId(), FreshDetail.class);
        if (null != post) {
            FreshDetailJson detailNews = new FreshDetailJson();
            detailNews.setPost(post);
            listener.onDetailSuccess(detailNews);
            return true;
        }
        return false;
    }

    private void requestData(final FreshPost freshPost, final OnLoadDetailListener<FreshDetailJson> listener) {
        lastGetTime = System.currentTimeMillis();
        StringCallback callback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (System.currentTimeMillis() - lastGetTime < GET_DURATION) {
                    Net.get(API.FRESH_NEWS_DETAIL + freshPost.getId(), this, API.TAG_FRESH);
                    return;
                }
                listener.onFailure("load fresh detail failed", e);

            }

            @Override
            public void onResponse(String response) {
                FreshDetailJson detail = Json.parseFreshDetail(response);
                DB.save(detail.getPost());
                listener.onDetailSuccess(detail);
            }
        };
        Net.get(API.FRESH_NEWS_DETAIL + freshPost.getId(), callback, API.TAG_FRESH);
    }
}
