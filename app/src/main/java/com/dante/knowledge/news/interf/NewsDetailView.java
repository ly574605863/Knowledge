package com.dante.knowledge.news.interf;

import com.dante.knowledge.news.other.NewsDetail;

/**
 * Created by Dante on 2016/1/30.
 */
public interface NewsDetailView<T extends NewsDetail> {
    void showProgress();
    void showDetail(T detailNews);
    void hideProgress();
    void showLoadFailed(String msg);
}
