package com.dante.knowledge.mvp.interf;

import com.dante.knowledge.mvp.other.Data;

/**
 * when news loaded, this interface is called
 */
public interface OnLoadDataListener<T extends Data> {
    void onDataSuccess(T news);
    void onFailure(String msg, Exception e);
}
