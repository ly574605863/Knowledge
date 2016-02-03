package com.dante.knowledge.news.interf;

import com.dante.knowledge.news.other.News;
import com.dante.knowledge.news.other.NewsDetail;
import com.dante.knowledge.news.other.NewsItem;

/**
 * Created by yons on 16/1/29.
 */
public interface NewsModel<I extends NewsItem, N extends News, D extends NewsDetail> {

    void getNews(int type, OnLoadNewsListener<N> listener);

    void getNewsDetail(I story, OnLoadDetailListener<D> listener);

}