package com.dante.knowledge.mvp.presenter;

import com.dante.knowledge.mvp.interf.NewsDetailPresenter;
import com.dante.knowledge.mvp.interf.NewsDetailView;
import com.dante.knowledge.mvp.interf.NewsModel;
import com.dante.knowledge.mvp.interf.OnLoadDetailListener;
import com.dante.knowledge.mvp.model.ZhihuDetail;
import com.dante.knowledge.mvp.model.ZhihuModel;
import com.dante.knowledge.mvp.model.ZhihuStory;
import com.dante.knowledge.ui.BaseActivity;

/**
 * helps to present zhihu news detail page
 */
public class ZhihuDetailPresenter implements NewsDetailPresenter<ZhihuStory>, OnLoadDetailListener<ZhihuDetail> {

    private NewsModel<ZhihuStory, ZhihuDetail> newsModel;
    private NewsDetailView<ZhihuDetail> newsDetailView;

    public ZhihuDetailPresenter(NewsDetailView<ZhihuDetail> newsDetailView, BaseActivity activity) {
        this.newsModel = new ZhihuModel(activity);
        this.newsDetailView = newsDetailView;
    }

    @Override
    public void loadNewsDetail(ZhihuStory zhihuStory) {
        newsDetailView.showProgress();
        newsModel.getNewsDetail(zhihuStory, this);
    }

    @Override
    public void onDetailSuccess(ZhihuDetail detailNews) {
        newsDetailView.showDetail(detailNews);
    }

    @Override
    public void onFailure(String msg, Exception e) {
        newsDetailView.showLoadFailed(msg);
        newsDetailView.hideProgress();
    }
}
