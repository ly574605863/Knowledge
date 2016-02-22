package com.dante.knowledge.ui;

import android.widget.TextView;

import com.dante.knowledge.BuildConfig;
import com.dante.knowledge.R;

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
        versionName.append(" "+BuildConfig.VERSION_NAME);
    }

}
