package com.dante.knowledge.news.presenter;

import com.dante.knowledge.news.interf.NewsDetailPresenter;
import com.dante.knowledge.news.model.ZhihuDetail;
import com.dante.knowledge.news.model.ZhihuItem;
import com.dante.knowledge.news.interf.NewsDetailView;
import com.dante.knowledge.news.interf.NewsModel;
import com.dante.knowledge.news.interf.OnLoadDetailListener;
import com.dante.knowledge.news.model.ZhihuNews;
import com.dante.knowledge.news.other.NewsDetail;

/**
 * Created by Dante on 2016/1/30.
 */
public class NewsDetailPresenterImpl implements NewsDetailPresenter<ZhihuItem>, OnLoadDetailListener<ZhihuDetail> {

    private NewsModel<ZhihuItem, ZhihuNews, ZhihuDetail> newsModel;
    private NewsDetailView<ZhihuDetail> newsDetailView;

    public NewsDetailPresenterImpl(NewsDetailView<ZhihuDetail> newsDetailView) {
        this.newsModel = new NewsModelImpl();
        this.newsDetailView = newsDetailView;
    }

    @Override
    public void loadNewsDetail(ZhihuItem story) {
        newsDetailView.showProgress();
        newsModel.getNewsDetail(story, this);
    }

    @Override
    public void onDetailSuccess(ZhihuDetail detailNews) {
        newsDetailView.showDetail(detailNews);
//        newsDetailView.hideProgress();
    }

    @Override
    public void onFailure(String msg, Exception e) {
        newsDetailView.showLoadFailed(msg);
        newsDetailView.hideProgress();
    }
}
