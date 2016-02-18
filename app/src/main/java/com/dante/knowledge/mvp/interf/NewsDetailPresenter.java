package com.dante.knowledge.mvp.interf;

import com.dante.knowledge.mvp.other.NewsItem;

/**
 * helps to present news detail page
 */
public interface NewsDetailPresenter<T extends NewsItem> {
    void loadNewsDetail(T newsItem);
}
