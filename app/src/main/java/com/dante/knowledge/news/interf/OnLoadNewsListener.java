package com.dante.knowledge.news.interf;

import com.dante.knowledge.news.other.News;

/**
 * when news loaded, this interface is called
 */
public interface OnLoadNewsListener<T extends News> {
    void onNewsSuccess(T news);
    void onFailure(String msg, Exception e);
}
