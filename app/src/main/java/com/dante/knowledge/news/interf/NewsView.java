package com.dante.knowledge.news.interf;

import com.dante.knowledge.news.other.News;

/**
 * Created by yons on 16/1/29.
 */
public interface NewsView<T extends News> {
    void showProgress();
    void addNews(T news);
    void hideProgress();
    void showLoadFailed(String msg);
}
