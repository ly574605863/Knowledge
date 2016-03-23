package com.dante.knowledge.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dante.knowledge.R;
import com.dante.knowledge.utils.App;
import com.dante.knowledge.utils.FileUtil;
import com.dante.knowledge.utils.IntentUtil;
import com.dante.knowledge.utils.SPUtil;
import com.dante.knowledge.utils.UI;

import java.io.File;


/**
 * the view in setting activity.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    public static final String CLEAR_CACHE = "clear_cache";
    public static final String FEED_BACK = "feedback";
    public static final String APP_VERSION = "check_version";
    public static final String ORIGINAL_SPLASH = "original_splash";
    public static final String SECRET_MODE = "secret_mode";
    private static final long DURATION = 300;

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
        clearCache.setSummary(clearCache.getSummary() + " " + getCacheSize());
        splash.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                secretStepOne();
                return true;
            }
        });
        version.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.i("test", secretIndex + ">>>>");
                if ((Boolean) newValue && secretIndex < 3) {
//                    BmobUpdateAgent.forceUpdate(getActivity());
                }
                secretStepTwo();
                return true;
            }
        });

        clearCache.setOnPreferenceClickListener(this);
        about.setOnPreferenceClickListener(this);
    }

    private void secretStepTwo() {
        if (System.currentTimeMillis() - startTime < DURATION * (secretIndex + 1)) {
            if (secretIndex > 2) {
                Log.i("test", "splash " + secretIndex);
                secretIndex++;
            }
        }
        if (secretIndex == 6) {
            if (SPUtil.getBoolean(SECRET_MODE)) {
                SPUtil.save(SECRET_MODE, false);
                secretIndex = 0;
                UI.showSnack(rootView, R.string.secret_mode_closed);
            } else {
                SPUtil.save(SECRET_MODE, true);
                secretIndex = 0;
                UI.showSnackLong(rootView, R.string.secret_mode_opened);
            }
            secretIndex++;
        }
    }

    private void secretStepOne() {
        if (first) {
            startTime = System.currentTimeMillis();
            first = false;
        }
        if (System.currentTimeMillis() - startTime < DURATION * (secretIndex + 1)) {
            if (secretIndex < 3) {
                secretIndex++;
            }

        }
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
        return FileUtil.getFileSize(file);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        switch (key) {
            case CLEAR_CACHE:
                App.openAppInfo(getActivity());
                break;
            case FEED_BACK:
                sendEmailFeedback();
                break;
        }
        return true;
    }


    private void sendEmailFeedback() {
        Intent email = new Intent(Intent.ACTION_SENDTO);
        if (IntentUtil.isIntentSafe(email)) {
            email.setData(Uri.parse("mailto:danteandroi@gmail.com"));
            email.putExtra(Intent.EXTRA_SUBJECT, "Knowledge Feedback");
            email.putExtra(Intent.EXTRA_TEXT, "Hi，");
            startActivity(email);
        } else {
            UI.showSnack(rootView, R.string.email_not_install);
        }
    }

}
