package com.dante.knowledge.news.listener;

import com.dante.knowledge.bean.BeforeNews;
import com.dante.knowledge.bean.LatestNews;
import com.dante.knowledge.bean.StoriesEntity;
import com.dante.knowledge.bean.TopStoriesEntity;

import java.util.List;

/**
 * Created by yons on 16/1/29.
 */
public interface OnLoadNewsListener {
    void onTopSuccess(List<TopStoriesEntity> topStoriesEntity);
    void onStoriesSuccess(List<StoriesEntity> storiesEntities);
    void onFailure(String msg, Exception e);
}
