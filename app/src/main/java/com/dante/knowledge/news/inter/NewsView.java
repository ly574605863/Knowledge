package com.dante.knowledge.news.inter;

import com.dante.knowledge.bean.StoriesEntity;
import com.dante.knowledge.bean.TopStoriesEntity;
import java.util.List;

/**
 * Created by yons on 16/1/29.
 */
public interface NewsView {
    void showProgress();
    void setTop(List<TopStoriesEntity> storiesEntities);
    void addNews(List<StoriesEntity> storiesEntities);
    void hideProgress();
    void showLoadFailed(String msg);

}
