package com.dante.knowledge.news.presenter;

import android.content.Context;

import com.dante.knowledge.net.API;
import com.dante.knowledge.news.interf.NewsModel;
import com.dante.knowledge.news.interf.NewsPresenter;
import com.dante.knowledge.news.interf.NewsView;
import com.dante.knowledge.news.interf.OnLoadNewsListener;
import com.dante.knowledge.news.model.ZhihuDetail;
import com.dante.knowledge.news.model.ZhihuItem;
import com.dante.knowledge.news.model.ZhihuNews;
import com.dante.knowledge.news.model.ZhihuNewsModel;

/**
 * helps to present zhihu news list
 */
public class ZhihuNewsPresenter implements NewsPresenter, OnLoadNewsListener<ZhihuNews> {

    private NewsView<ZhihuNews> mNewsView;
    private NewsModel<ZhihuItem, ZhihuNews, ZhihuDetail> mNewsModel;

    public ZhihuNewsPresenter(NewsView<ZhihuNews> newsView, Context context) {
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
    public void onNewsSuccess(ZhihuNews news) {
        mNewsView.addNews(news);
        mNewsView.hideProgress();
    }

    @Override
    public void onFailure(String msg, Exception e) {
        mNewsView.hideProgress();
        mNewsView.loadFailed(msg);
    }

}
