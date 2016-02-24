package com.dante.knowledge.mvp.interf;

import com.dante.knowledge.mvp.other.Data;

/**
 * when news loaded, this interface is called
 */
public interface OnLoadDataListener<T extends Data> {
    void onSuccess(T data);
    void onFailure(String msg, Exception e);
}
