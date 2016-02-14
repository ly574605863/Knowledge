package com.dante.knowledge;

import android.app.Application;
import android.content.Context;

import com.dante.knowledge.utils.UiUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Init LeakCanary, Utils.
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
