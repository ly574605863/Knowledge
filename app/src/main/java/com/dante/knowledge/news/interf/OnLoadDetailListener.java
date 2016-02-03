package com.dante.knowledge.news.interf;

import com.dante.knowledge.news.other.NewsDetail;

/**
 * Created by Dante on 2016/1/30.
 */
public interface OnLoadDetailListener<T extends NewsDetail> {
    void onDetailSuccess(T detailNews);
    void onFailure(String msg, Exception e);

}
