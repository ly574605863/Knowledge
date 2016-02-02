package com.dante.knowledge.news.view;

import com.dante.knowledge.news.model.DetailNews;

/**
 * Created by Dante on 2016/1/30.
 */
public interface NewsDetailView {
    void showProgress();
    void showDetail(DetailNews detailNews);
    void hideProgress();
    void showLoadFailed(String msg);
}
