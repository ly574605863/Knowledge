package com.dante.knowledge;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Dante on 2016/2/1.
 */
public class KnowledgeApplication extends Application {

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context){
        KnowledgeApplication application = (KnowledgeApplication) context.getApplicationContext();
        return application.refWatcher;
    }
}
