package com.dante.knowledge.news.presenter;

import com.dante.knowledge.news.model.LatestNews;
import com.dante.knowledge.net.API;
import com.dante.knowledge.news.model.NewsModel;
import com.dante.knowledge.news.model.NewsModelImpl;
import com.dante.knowledge.news.view.NewsView;
import com.dante.knowledge.news.other.OnLoadNewsListener;

/**
 * Created by yons on 16/1/29.
 */
public class NewsPresenterImpl implements NewsPresenter, OnLoadNewsListener {

    private NewsView mNewsView;
    private NewsModel mNewsModel;

    public NewsPresenterImpl(NewsView newsView) {
        this.mNewsView = newsView;
        mNewsModel = new NewsModelImpl();
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
    public void onNewsSuccess(LatestNews news) {
        mNewsView.addNews(news);
        mNewsView.hideProgress();
    }

    @Override
    public void onFailure(String msg, Exception e) {
        mNewsView.hideProgress();
        mNewsView.showLoadFailed(msg);
    }

}
