package com.dante.knowledge.mvp.interf;

import com.dante.knowledge.mvp.other.Data;
import com.dante.knowledge.mvp.other.NewsDetail;
import com.dante.knowledge.mvp.other.NewsItem;

/**
 * deals with the data work
 */
public interface NewsModel<I extends NewsItem, N extends Data, D extends NewsDetail> {

    void getNews(int type, OnLoadDataListener<N> listener);

    void getNewsDetail(I newsItem, OnLoadDetailListener<D> listener);

    void init();
}