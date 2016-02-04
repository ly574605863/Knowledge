package com.dante.knowledge.ui;

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

/**
 * the view in setting activity.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    public static final String CLEAR_CACHE = "clear_cache";
    public static final String ABOUT_APP = "about";
    public static final String APP_VERSION = "version";

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
        about = findPreference(ABOUT_APP);
        version = findPreference(APP_VERSION);
        Log.i("test",">>>>>>>>>>>>");
        clearCache.setSummary(clearCache.getSummary() + "1.2 M");
        version.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((Boolean) newValue){
                    Snackbar.make(rootView, "已开启", Snackbar.LENGTH_SHORT).show();
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
        if (null==rootView){
            rootView = super.onCreateView(inflater, container, savedInstanceState);
        }
        return rootView;

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (key.equals(CLEAR_CACHE)) {

        } else if (key.equals(ABOUT_APP)) {

        }
        return true;
    }

}
