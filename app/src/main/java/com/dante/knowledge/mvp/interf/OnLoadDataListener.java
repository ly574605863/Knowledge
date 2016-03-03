package com.dante.knowledge.mvp.interf;

/**
 * when news loaded, this interface is called
 */
public interface OnLoadDataListener {
    void onSuccess();
    void onFailure(String msg);
}
