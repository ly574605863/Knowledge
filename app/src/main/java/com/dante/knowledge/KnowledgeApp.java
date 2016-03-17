package com.dante.knowledge;

import android.app.Application;
import android.content.Context;

import com.bugtags.library.Bugtags;
import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Init LeakCanary, Utils.
 */
public class KnowledgeApp extends Application {

    private RefWatcher refWatcher;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        refWatcher = LeakCanary.install(this);
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
        setupRealm();
        Bugtags.start("17483b5ba7f71cf806b0707f3b1542c1", this, Bugtags.BTGInvocationEventShake);
    }


    private void setupRealm() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public static RefWatcher getWatcher(Context context) {
        KnowledgeApp application = (KnowledgeApp) context.getApplicationContext();
        return application.refWatcher;
    }

    public class AppBlockCanaryContext extends BlockCanaryContext {
        // override to provide context like app qualifier, uid, network type, block threshold, log save path
        // this is default block threshold, you can set it by phone's performance
        @Override
        public int getConfigBlockThreshold() {
            return 500;
        }

        // if set true, notification will be shown, else only write log file
        @Override
        public boolean isNeedDisplay() {
            return BuildConfig.DEBUG;
        }

        // path to save log file
        @Override
        public String getLogPath() {
            return "/blockcanary/performance";
        }
    }

}
