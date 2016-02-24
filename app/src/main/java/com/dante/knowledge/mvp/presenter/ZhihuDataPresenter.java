package com.dante.knowledge.mvp.presenter;

import android.content.Context;

import com.dante.knowledge.mvp.interf.NewsModel;
import com.dante.knowledge.mvp.interf.NewsPresenter;
import com.dante.knowledge.mvp.interf.NewsView;
import com.dante.knowledge.mvp.interf.OnLoadDataListener;
import com.dante.knowledge.mvp.model.ZhihuDetail;
import com.dante.knowledge.mvp.model.ZhihuJson;
import com.dante.knowledge.mvp.model.ZhihuModel;
import com.dante.knowledge.mvp.model.ZhihuStory;
import com.dante.knowledge.net.API;

/**
 * helps to present zhihu news list
 */
public class ZhihuDataPresenter implements NewsPresenter, OnLoadDataListener<ZhihuJson> {

    private NewsView<ZhihuJson> mNewsView;
    private NewsModel<ZhihuStory, ZhihuJson, ZhihuDetail> mNewsModel;

    public ZhihuDataPresenter(NewsView<ZhihuJson> newsView, Context context) {
        this.mNewsView = newsView;
        mNewsModel = new ZhihuModel();
    }


    @Override
    public void loadNews() {
        mNewsView.showProgress();
        mNewsModel.getNews(API.TYPE_LATEST, this);
    }


    @Override
    public void loadBefore() {
        mNewsModel.getNews(API.TYPE_BEFORE, this);
    }


    @Override
    public void onSuccess(ZhihuJson news) {
        mNewsView.addNews(news);
        mNewsView.hideProgress();
    }

    @Override
    public void onFailure(String msg, Exception e) {
        mNewsView.hideProgress();
        mNewsView.loadFailed(msg);
    }

}
