package com.dante.knowledge.ui;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dante.knowledge.R;
import com.dante.knowledge.utils.Tool;

import java.io.File;
import java.util.List;

/**
 * the view in setting activity.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    public static final String CLEAR_CACHE = "clear_cache";
    public static final String FEED_BACK = "feedback";
    public static final String APP_VERSION = "version";
    public static final String SPLASH = "splash";

    private Preference clearCache;
    private Preference about;
    private Preference version;
    private CheckBoxPreference enableSister;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        clearCache = findPreference(CLEAR_CACHE);
        about = findPreference(FEED_BACK);
        version = findPreference(APP_VERSION);
        clearCache.setSummary(clearCache.getSummary() + fileDirSize());
        version.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((Boolean) newValue) {
                    Snackbar.make(rootView, "已开启", Snackbar.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        clearCache.setOnPreferenceClickListener(this);
        about.setOnPreferenceClickListener(this);
    }

    private String fileDirSize() {
        File file = getActivity().getApplicationContext().getFilesDir();
        return Tool.getFileSize(file);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (null == rootView) {
            rootView = super.onCreateView(inflater, container, savedInstanceState);
        }
        return rootView;

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        File dir = getActivity().getApplicationContext().getFilesDir();

        if (key.equals(CLEAR_CACHE)) {

            for (File file : dir.listFiles()) {
                if (!file.delete()) {
                    Snackbar.make(rootView, R.string.clear_cache_failed, Snackbar.LENGTH_SHORT).show();
                    return true;
                }
            }
            Snackbar.make(rootView, R.string.clear_cache_success, Snackbar.LENGTH_SHORT).show();
            clearCache.setSummary(getString(R.string.set_current_cache) + fileDirSize());

        } else if (key.equals(FEED_BACK)) {

        }
        return true;
    }

}
