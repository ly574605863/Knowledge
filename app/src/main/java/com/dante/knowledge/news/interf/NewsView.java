package com.dante.knowledge.news.interf;

import com.dante.knowledge.news.other.News;

/**
 * fragment or activity need to implement this to show news list.
 */
public interface NewsView<T extends News> {
    void showProgress();
    void addNews(T news);
    void hideProgress();
    void showLoadFailed(String msg);
}
