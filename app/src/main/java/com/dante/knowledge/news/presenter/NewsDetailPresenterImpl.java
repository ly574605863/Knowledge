package com.dante.knowledge.news.presenter;

import com.dante.knowledge.news.model.DetailNews;
import com.dante.knowledge.news.model.StoryEntity;
import com.dante.knowledge.news.model.NewsModelImpl;
import com.dante.knowledge.news.view.NewsDetailView;
import com.dante.knowledge.news.model.NewsModel;
import com.dante.knowledge.news.other.OnLoadDetailListener;

/**
 * Created by Dante on 2016/1/30.
 */
public class NewsDetailPresenterImpl implements NewsDetailPresenter, OnLoadDetailListener{

    private NewsModel newsModel;
    private NewsDetailView newsDetailView;

    public NewsDetailPresenterImpl(NewsDetailView newsDetailView) {
        this.newsModel = new NewsModelImpl();
        this.newsDetailView = newsDetailView;
    }

    @Override
    public void loadNewsDetail(StoryEntity story) {
        newsDetailView.showProgress();
        newsModel.getNewsDetail(story, this);
    }

    @Override
    public void onDetailSuccess(DetailNews detailNews) {
        newsDetailView.showDetail(detailNews);
//        newsDetailView.hideProgress();
    }

    @Override
    public void onFailure(String msg, Exception e) {
        newsDetailView.showLoadFailed(msg);
        newsDetailView.hideProgress();
    }
}
