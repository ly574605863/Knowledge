package com.dante.knowledge.mvp.presenter;

import com.dante.knowledge.mvp.interf.NewsModel;
import com.dante.knowledge.mvp.interf.NewsPresenter;
import com.dante.knowledge.mvp.interf.NewsView;
import com.dante.knowledge.mvp.interf.OnLoadDataListener;
import com.dante.knowledge.mvp.model.FreshDetailJson;
import com.dante.knowledge.mvp.model.FreshJson;
import com.dante.knowledge.mvp.model.FreshModel;
import com.dante.knowledge.mvp.model.FreshPost;

/**
 * helps to present fresh news list
 */
public class FreshDataPresenter implements NewsPresenter, OnLoadDataListener<FreshJson> {
    private NewsView<FreshJson> mNewsView;
    private NewsModel<FreshPost, FreshJson, FreshDetailJson> mNewsModel;

    public FreshDataPresenter(NewsView<FreshJson> newsView) {
        this.mNewsView = newsView;
        mNewsModel = new FreshModel();
    }

    @Override
    public void loadNews() {
        mNewsView.showProgress();
        mNewsModel.getNews(FreshModel.TYPE_FRESH, this);
    }

    @Override
    public void loadBefore() {
        mNewsView.showProgress();
        mNewsModel.getNews(FreshModel.TYPE_CONTINUOUS, this);

    }

    @Override
    public void onSuccess(FreshJson news) {
        mNewsView.addNews(news);
        mNewsView.hideProgress();
    }

    @Override
    public void onFailure(String msg, Exception e) {
        mNewsView.hideProgress();
        mNewsView.loadFailed(msg);
    }
}
