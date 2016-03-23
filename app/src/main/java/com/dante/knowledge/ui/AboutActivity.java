package com.dante.knowledge.ui;

import android.content.Intent;
import android.widget.TextView;

import com.dante.knowledge.BuildConfig;
import com.dante.knowledge.R;
import com.dante.knowledge.utils.IntentUtil;
import com.dante.knowledge.utils.UI;

import butterknife.Bind;

/**
 * about the author and so on.
 */
public class AboutActivity extends BaseActivity {
    @Bind(R.id.versionName)
    TextView versionName;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_about;
    }

    @Override
    protected void initViews() {
        super.initViews();
        versionName.append(" " + BuildConfig.VERSION_NAME);
    }

    @Override
    public void startActivity(Intent intent) {
        if (IntentUtil.isIntentSafe(intent)) {
            super.startActivity(intent);
        } else {
            UI.showSnack(versionName, R.string.email_not_install);
        }
    }

}
