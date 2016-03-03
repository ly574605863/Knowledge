package com.dante.knowledge.mvp.interf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dante.knowledge.mvp.presenter.FetchService;

/**
 * Notify the pictures have been fetched.
 */
public class UpdateReceiver extends BroadcastReceiver {
    private OnLoadDataListener loadDataListener;

    public UpdateReceiver(OnLoadDataListener loadDataListener) {
        this.loadDataListener = loadDataListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getBooleanExtra(FetchService.EXTRA_FETCHED_RESULT, false)) {
            loadDataListener.onSuccess();
        } else {
            loadDataListener.onFailure("load no results");
        }
    }
}