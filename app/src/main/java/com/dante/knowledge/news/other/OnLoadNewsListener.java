package com.dante.knowledge.news.other;

import com.dante.knowledge.news.model.LatestNews;

/**
 * Created by yons on 16/1/29.
 */
public interface OnLoadNewsListener {
    void onNewsSuccess(LatestNews news);
    void onFailure(String msg, Exception e);
}
