package com.dante.knowledge.news.view;

import com.dante.knowledge.news.model.LatestNews;

/**
 * Created by yons on 16/1/29.
 */
public interface NewsView {
    void showProgress();
    void addNews(LatestNews news);
    void hideProgress();
    void showLoadFailed(String msg);
}
