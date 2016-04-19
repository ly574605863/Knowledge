package com.dante.knowledge.mvp.presenter;

import com.dante.knowledge.mvp.interf.NewsModel;
import com.dante.knowledge.mvp.interf.NewsPresenter;
import com.dante.knowledge.mvp.interf.NewsView;
import com.dante.knowledge.mvp.interf.OnLoadDataListener;
import com.dante.knowledge.mvp.model.FreshDetailJson;
import com.dante.knowledge.mvp.model.FreshJson;
import com.dante.knowledge.mvp.model.FreshModel;
import com.dante.knowledge.mvp.model.FreshPost;
import com.dante.knowledge.ui.BaseActivity;

/**
 * helps to present fresh news list
 */
public class FreshDataPresenter implements NewsPresenter, OnLoadDataListener {
    private NewsView<FreshJson> mNewsView;
    private NewsModel<FreshPost, FreshDetailJson> mNewsModel;

    public FreshDataPresenter(NewsView<FreshJson> newsView, BaseActivity activity) {
        this.mNewsView = newsView;
        mNewsModel = new FreshModel(activity);
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
    public void onSuccess() {
        mNewsView.addNews(null);
        mNewsView.hideProgress();
    }

    @Override
    public void onFailure(String msg) {
        mNewsView.hideProgress();
        mNewsView.loadFailed(msg);
    }
}
