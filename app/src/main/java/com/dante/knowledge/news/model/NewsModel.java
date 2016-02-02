package com.dante.knowledge.news.model;

import com.dante.knowledge.news.other.OnLoadDetailListener;
import com.dante.knowledge.news.other.OnLoadNewsListener;

/**
 * Created by yons on 16/1/29.
 */
public interface NewsModel {

    void getNews(int type, OnLoadNewsListener listener);
    void getNewsDetail(StoryEntity story, OnLoadDetailListener listener);


}