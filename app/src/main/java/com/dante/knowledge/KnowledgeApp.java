package com.dante.knowledge;

import android.app.Application;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.dante.knowledge.utils.UiUtils;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Dante on 2016/2/1.
 */
public class KnowledgeApp extends Application {

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
        new UiUtils(getApplicationContext());
    }

    public static RefWatcher getRefWatcher(Context context){
        KnowledgeApp application = (KnowledgeApp) context.getApplicationContext();
        return application.refWatcher;
    }


}
