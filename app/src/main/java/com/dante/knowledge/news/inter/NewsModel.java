package com.dante.knowledge.news.inter;

import com.dante.knowledge.news.listener.OnLoadNewsListener;

/**
 * Created by yons on 16/1/29.
 */
public interface NewsModel {

    void loadNews(int type, OnLoadNewsListener listener);


}