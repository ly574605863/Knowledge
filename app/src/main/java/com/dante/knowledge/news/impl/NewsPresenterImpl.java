package com.dante.knowledge.news.impl;

import com.dante.knowledge.bean.BeforeNews;
import com.dante.knowledge.bean.LatestNews;
import com.dante.knowledge.bean.StoriesEntity;
import com.dante.knowledge.bean.TopStoriesEntity;
import com.dante.knowledge.net.API;
import com.dante.knowledge.news.inter.NewsModel;
import com.dante.knowledge.news.inter.NewsPresenter;
import com.dante.knowledge.news.inter.NewsView;
import com.dante.knowledge.news.listener.OnLoadNewsListener;

import java.util.List;

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
        mNewsModel.loadNews(API.TYPE_LATEST, this);
    }

    @Override
    public void loadBefore() {
        mNewsView.showProgress();
        mNewsModel.loadNews(API.TYPE_BEFORE, this);
    }

    @Override
    public void onTopSuccess(List<TopStoriesEntity> topStoriesEntity) {
        mNewsView.setTop(topStoriesEntity);
    }

    @Override
    public void onStoriesSuccess(List<StoriesEntity> storiesEntities) {
        mNewsView.hideProgress();
        mNewsView.addNews(storiesEntities);
    }

    @Override
    public void onFailure(String msg, Exception e) {
        mNewsView.hideProgress();
        mNewsView.showLoadFailed(msg);
    }

}
