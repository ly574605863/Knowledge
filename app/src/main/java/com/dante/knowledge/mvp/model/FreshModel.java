package com.dante.knowledge.mvp.model;

import com.dante.knowledge.mvp.interf.NewsModel;
import com.dante.knowledge.mvp.interf.OnLoadDataListener;
import com.dante.knowledge.mvp.interf.OnLoadDetailListener;
import com.dante.knowledge.net.API;
import com.dante.knowledge.net.DB;
import com.dante.knowledge.net.Json;
import com.dante.knowledge.net.Net;
import com.dante.knowledge.ui.BaseActivity;
import com.dante.knowledge.utils.Constants;
import com.dante.knowledge.utils.SPUtil;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Response;

/**
 * deals with the fresh news' data work
 */
public class FreshModel implements NewsModel<FreshPost, FreshDetailJson> {
    /**
     * clear page record to zero and start new request
     */
    public static final int TYPE_FRESH = 0;
    /**
     * a continuous request with increasing one page each time
     */
    public static final int TYPE_CONTINUOUS = 1;
    public static final int GET_DURATION = 3000;
    private static final String TAG = "test";
    private final Realm realm;
    private int page;
    private long lastGetTime;
    private BaseActivity mActivity;

    public FreshModel(BaseActivity activity) {
        mActivity = activity;
        realm = mActivity.mRealm;
    }

    @Override
    public void getNews(int type, final OnLoadDataListener listener) {
        lastGetTime = System.currentTimeMillis();
        if (type == TYPE_FRESH) {
            page = 1;//如果是全新请求，就初始化page为1
        }
        getFreshNews(listener);
    }

    private void getFreshNews(final OnLoadDataListener listener) {
        Callback<FreshJson> callback = new Callback<FreshJson>() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                if (System.currentTimeMillis() - lastGetTime < GET_DURATION) {
                    Net.get(API.FRESH_NEWS + page, this, API.TAG_FRESH);
                    return;
                }
                listener.onFailure("load fresh news failed");
            }

            @Override
            public void onResponse(FreshJson response) {
                if (mActivity.isFinishing() || response == null) {
                    return;
                }
                DB.saveList(realm, response.getPosts());
                listener.onSuccess();
                page ++;
                SPUtil.save(Constants.PAGE, page);
            }

            @Override
            public FreshJson parseNetworkResponse(Response response) throws IOException {
                return Json.parseFreshNews(response.body().string());
            }
        };
        Net.get(API.FRESH_NEWS + page, callback, mActivity);
    }

    @Override
    public void getNewsDetail(final FreshPost freshPost, final OnLoadDetailListener<FreshDetailJson> listener) {
        if (getDetailFromDB(freshPost, listener)) return;

        requestData(freshPost, listener);
    }

    private boolean getDetailFromDB(FreshPost freshPost, OnLoadDetailListener<FreshDetailJson> listener) {
        FreshDetail post = DB.getById(realm, freshPost.getId(), FreshDetail.class);
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
        Callback<FreshDetailJson> callback = new Callback<FreshDetailJson>() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                if (System.currentTimeMillis() - lastGetTime < GET_DURATION) {
                    Net.get(API.FRESH_NEWS_DETAIL + freshPost.getId(), this, API.TAG_FRESH);
                    return;
                }
                listener.onFailure("load fresh detail failed", e);

            }

            @Override
            public FreshDetailJson parseNetworkResponse(Response response) throws IOException {
                return Json.parseFreshDetail(response.body().string());
            }

            @Override
            public void onResponse(FreshDetailJson response) {
                DB.save(realm, response.getPost());
                listener.onDetailSuccess(response);
            }
        };
        Net.get(API.FRESH_NEWS_DETAIL + freshPost.getId(), callback, mActivity);
    }
}
