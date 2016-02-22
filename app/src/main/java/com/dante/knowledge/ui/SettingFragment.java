package com.dante.knowledge.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dante.knowledge.R;
import com.dante.knowledge.utils.SP;
import com.dante.knowledge.utils.Tool;
import com.dante.knowledge.utils.UI;

import java.io.File;

/**
 * the view in setting activity.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    public static final String CLEAR_CACHE = "clear_cache";
    public static final String FEED_BACK = "feedback";
    public static final String APP_VERSION = "version";
    public static final String ORIGINAL_SPLASH = "original_splash";
    public static final String SECRET_MODE = "secret_mode";
    private static final long DURATION = 500;

    private Preference clearCache;
    private Preference about;
    private Preference version;
    private Preference splash;
    private CheckBoxPreference enableSister;
    private View rootView;

    private long startTime;
    private boolean first = true;
    private int secretIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        clearCache = findPreference(CLEAR_CACHE);
        about = findPreference(FEED_BACK);
        version = findPreference(APP_VERSION);
        splash = findPreference(ORIGINAL_SPLASH);
        clearCache.setSummary(clearCache.getSummary() + getCacheSize());
        splash.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if (System.currentTimeMillis() - startTime < DURATION * 3) {
                    if (secretIndex < 2) {
                        return true;
                    }
                    Log.i("test", "splash " + secretIndex);
                    secretIndex++;
                }
                if (secretIndex == 5) {
                    if (SP.getBoolean(SECRET_MODE)) {
                        SP.save(SECRET_MODE, false);
                        UI.showSnack(rootView, R.string.secret_mode_closed);
                    } else {
                        SP.save(SECRET_MODE, true);
                        UI.showSnackLong(rootView, R.string.secret_mode_opened);
                    }
                    secretIndex++;
                }
                return true;
            }
        });
        version.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (first) {
                    startTime = System.currentTimeMillis();
                    first = false;
                    Log.i("test", "first " + secretIndex);
                }
                if (System.currentTimeMillis() - startTime < DURATION) {
                    if (secretIndex > 2) {
                        return true;
                    }
                    Log.i("test", "version " + secretIndex);
                    secretIndex++;
                }

                return true;
            }
        });

        clearCache.setOnPreferenceClickListener(this);
        about.setOnPreferenceClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (null == rootView) {
            rootView = super.onCreateView(inflater, container, savedInstanceState);
        }
        return rootView;

    }

    private String getCacheSize() {
        File file = getActivity().getApplicationContext().getCacheDir();
        return Tool.getFileSize(file);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        switch (key) {
            case CLEAR_CACHE:
                if (clearCache()) {
                    Snackbar.make(rootView, R.string.clear_cache_success, Snackbar.LENGTH_SHORT).show();
                    clearCache.setSummary(getString(R.string.set_current_cache) + getCacheSize());
                } else {
                    //clear failed
                    Snackbar.make(rootView, R.string.clear_cache_failed, Snackbar.LENGTH_SHORT).show();
                }

                break;
            case FEED_BACK:
                sendEmailFeedback();
                break;
        }
        return true;
    }

    private boolean clearCache() {
        File cacheDir = getActivity().getApplicationContext().getCacheDir();
        for (File file : cacheDir.listFiles()) {
            if (!file.delete()) {
                return false;
            }
        }
        return true;
    }

    private void sendEmailFeedback() {
        Intent email = new Intent(Intent.ACTION_SENDTO);
        email.setData(Uri.parse("mailto:danteandroi@gmail.com"));
        email.putExtra(Intent.EXTRA_SUBJECT, "Knowledge Feedback");
        email.putExtra(Intent.EXTRA_TEXT, "Hi，");
        startActivity(email);
    }

}
