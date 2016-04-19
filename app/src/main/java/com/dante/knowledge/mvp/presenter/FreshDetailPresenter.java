package com.dante.knowledge.mvp.presenter;

import com.dante.knowledge.mvp.interf.NewsDetailPresenter;
import com.dante.knowledge.mvp.interf.NewsDetailView;
import com.dante.knowledge.mvp.interf.NewsModel;
import com.dante.knowledge.mvp.interf.OnLoadDetailListener;
import com.dante.knowledge.mvp.model.FreshDetailJson;
import com.dante.knowledge.mvp.model.FreshModel;
import com.dante.knowledge.mvp.model.FreshPost;
import com.dante.knowledge.ui.BaseActivity;

/**
 * helps to present fresh news detail page
 */
public class FreshDetailPresenter implements NewsDetailPresenter<FreshPost>, OnLoadDetailListener<FreshDetailJson>{

    private NewsModel<FreshPost, FreshDetailJson> mNewsModel;
    private NewsDetailView<FreshDetailJson> newsDetailView;

    public FreshDetailPresenter(NewsDetailView<FreshDetailJson> newsDetailView, BaseActivity activity) {
        this.mNewsModel = new FreshModel(activity);
        this.newsDetailView = newsDetailView;
    }

    @Override
    public void loadNewsDetail(FreshPost freshPost) {
        newsDetailView.showProgress();
        mNewsModel.getNewsDetail(freshPost, this);
    }

    @Override
    public void onDetailSuccess(FreshDetailJson detailNews) {
        newsDetailView.showDetail(detailNews);

    }

    @Override
    public void onFailure(String msg, Exception e) {
        newsDetailView.showLoadFailed(msg);
        newsDetailView.hideProgress();
    }
}
