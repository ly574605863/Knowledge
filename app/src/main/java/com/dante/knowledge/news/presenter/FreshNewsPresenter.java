package com.dante.knowledge.news.presenter;

import com.dante.knowledge.news.interf.NewsModel;
import com.dante.knowledge.news.interf.NewsPresenter;
import com.dante.knowledge.news.interf.NewsView;
import com.dante.knowledge.news.interf.OnLoadNewsListener;
import com.dante.knowledge.news.model.FreshDetail;
import com.dante.knowledge.news.model.FreshItem;
import com.dante.knowledge.news.model.FreshNews;
import com.dante.knowledge.news.model.FreshNewsModel;

/**
 * Created by yons on 16/2/3.
 */
public class FreshNewsPresenter implements NewsPresenter, OnLoadNewsListener<FreshNews> {
    private NewsView<FreshNews> mNewsView;
    private NewsModel<FreshItem, FreshNews, FreshDetail> mNewsModel;

    public FreshNewsPresenter(NewsView<FreshNews> newsView) {
        this.mNewsView = newsView;
        mNewsModel = new FreshNewsModel();
    }

    @Override
    public void loadNews() {
        mNewsView.showProgress();
        mNewsModel.getNews(FreshNewsModel.TYPE_FRESH, this);
    }

    @Override
    public void loadBefore() {
        mNewsView.showProgress();
        mNewsModel.getNews(FreshNewsModel.TYPE_CONTINUOUS, this);

    }

    @Override
    public void onNewsSuccess(FreshNews news) {
        mNewsView.addNews(news);
        mNewsView.hideProgress();
    }

    @Override
    public void onFailure(String msg, Exception e) {
        mNewsView.hideProgress();
        mNewsView.showLoadFailed(msg);
    }
}
