package com.dante.knowledge.mvp.presenter;

import android.content.Context;

import com.dante.knowledge.mvp.interf.NewsModel;
import com.dante.knowledge.mvp.interf.NewsPresenter;
import com.dante.knowledge.mvp.interf.NewsView;
import com.dante.knowledge.mvp.interf.OnLoadDataListener;
import com.dante.knowledge.mvp.model.FreshData;
import com.dante.knowledge.mvp.model.FreshDetail;
import com.dante.knowledge.mvp.model.FreshItem;
import com.dante.knowledge.mvp.model.FreshNewsModel;

/**
 * helps to present fresh news list
 */
public class FreshDataPresenter implements NewsPresenter, OnLoadDataListener<FreshData> {
    private NewsView<FreshData> mNewsView;
    private NewsModel<FreshItem, FreshData, FreshDetail> mNewsModel;

    public FreshDataPresenter(NewsView<FreshData> newsView, Context context) {
        this.mNewsView = newsView;
        mNewsModel = new FreshNewsModel(context);
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
    public void onDataSuccess(FreshData news) {
        mNewsView.addNews(news);
        mNewsView.hideProgress();
    }

    @Override
    public void onFailure(String msg, Exception e) {
        mNewsView.hideProgress();
        mNewsView.loadFailed(msg);
    }
}
