package com.dante.knowledge.news.other;

import com.dante.knowledge.news.model.DetailNews;

/**
 * Created by Dante on 2016/1/30.
 */
public interface OnLoadDetailListener {
    void onDetailSuccess(DetailNews detailNews);
    void onFailure(String msg, Exception e);

}
