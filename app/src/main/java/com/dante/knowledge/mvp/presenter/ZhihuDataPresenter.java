package com.dante.knowledge.mvp.presenter;

import android.content.Context;

import com.dante.knowledge.mvp.interf.NewsModel;
import com.dante.knowledge.mvp.interf.NewsPresenter;
import com.dante.knowledge.mvp.interf.NewsView;
import com.dante.knowledge.mvp.interf.OnLoadDataListener;
import com.dante.knowledge.mvp.model.ZhihuData;
import com.dante.knowledge.mvp.model.ZhihuDetail;
import com.dante.knowledge.mvp.model.ZhihuItem;
import com.dante.knowledge.mvp.model.ZhihuNewsModel;
import com.dante.knowledge.net.API;

/**
 * helps to present zhihu news list
 */
public class ZhihuDataPresenter implements NewsPresenter, OnLoadDataListener<ZhihuData> {

    private NewsView<ZhihuData> mNewsView;
    private NewsModel<ZhihuItem, ZhihuData, ZhihuDetail> mNewsModel;

    public ZhihuDataPresenter(NewsView<ZhihuData> newsView, Context context) {
        this.mNewsView = newsView;
        mNewsModel = new ZhihuNewsModel(context);
    }


    @Override
    public void loadNews() {
        mNewsModel.init();
        mNewsView.showProgress();
        mNewsModel.getNews(API.TYPE_LATEST, this);
    }


    @Override
    public void loadBefore() {
        mNewsModel.getNews(API.TYPE_BEFORE, this);
    }


    @Override
    public void onDataSuccess(ZhihuData news) {
        mNewsView.addNews(news);
        mNewsView.hideProgress();
    }

    @Override
    public void onFailure(String msg, Exception e) {
        mNewsView.hideProgress();
        mNewsView.loadFailed(msg);
    }

}
