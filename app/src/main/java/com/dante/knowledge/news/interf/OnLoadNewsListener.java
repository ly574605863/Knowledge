package com.dante.knowledge.news.interf;

import com.dante.knowledge.news.other.News;

/**
 * Created by yons on 16/1/29.
 */
public interface OnLoadNewsListener<T extends News> {
    void onNewsSuccess(T news);
    void onFailure(String msg, Exception e);
}
