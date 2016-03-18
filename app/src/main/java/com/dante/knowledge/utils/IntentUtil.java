package com.dante.knowledge.utils;

import android.content.Intent;
import android.content.pm.PackageManager;

import com.dante.knowledge.KnowledgeApp;

import java.util.List;

/**
 * Intent utils, like check if intent safe.
 */
public class IntentUtil {

    public static boolean isIntentSafe(Intent intent) {
        PackageManager packageManager = KnowledgeApp.context.getPackageManager();
        List activities = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return activities.size() > 0;
    }
}
