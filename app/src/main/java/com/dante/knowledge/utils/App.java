package com.dante.knowledge.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.dante.knowledge.KnowledgeApp;
import com.dante.knowledge.R;

/**
 * Created by Dante on 2016/2/19.
 */
public class App {

    public static String getVersionName() {
        try {
            PackageManager manager = KnowledgeApp.context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(KnowledgeApp.context.getPackageName(), 0);
            String version = info.versionName;
            return KnowledgeApp.context.getString(R.string.version)+ version;
        } catch (Exception e) {
            e.printStackTrace();
            return  KnowledgeApp.context.getString(R.string.can_not_find_version_name);
        }
    }
}
