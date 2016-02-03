package com.dante.knowledge.news.interf;

import com.dante.knowledge.news.other.NewsDetail;
import com.dante.knowledge.news.other.NewsItem;

/**
 * Created by Dante on 2016/1/30.
 */
public interface NewsDetailPresenter<T extends NewsItem> {
    void loadNewsDetail(T story);
}
