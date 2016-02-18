package com.dante.knowledge.mvp.interf;

import com.dante.knowledge.mvp.other.Data;

/**
 * fragment or activity need to implement this to show news list.
 */
public interface NewsView<T extends Data> {
    void showProgress();
    void addNews(T news);
    void hideProgress();
    void loadFailed(String msg);
}
