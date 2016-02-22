package com.dante.knowledge.mvp.presenter;

import android.content.Context;

import com.dante.knowledge.mvp.interf.NewsDetailPresenter;
import com.dante.knowledge.mvp.interf.NewsDetailView;
import com.dante.knowledge.mvp.interf.NewsModel;
import com.dante.knowledge.mvp.interf.OnLoadDetailListener;
import com.dante.knowledge.mvp.model.ZhihuData;
import com.dante.knowledge.mvp.model.ZhihuDetail;
import com.dante.knowledge.mvp.model.ZhihuItem;
import com.dante.knowledge.mvp.model.ZhihuNewsModel;

/**
 * helps to present zhihu news detail page
 */
public class ZhihuDetailPresenter implements NewsDetailPresenter<ZhihuItem>, OnLoadDetailListener<ZhihuDetail> {

    private NewsModel<ZhihuItem, ZhihuData, ZhihuDetail> newsModel;
    private NewsDetailView<ZhihuDetail> newsDetailView;

    public ZhihuDetailPresenter(NewsDetailView<ZhihuDetail> newsDetailView, Context context) {
        this.newsModel = new ZhihuNewsModel(context);
        this.newsDetailView = newsDetailView;
    }

    @Override
    public void loadNewsDetail(ZhihuItem zhihuItem) {
        newsDetailView.showProgress();
        newsModel.getNewsDetail(zhihuItem, this);
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
