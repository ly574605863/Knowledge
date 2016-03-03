package com.dante.knowledge.mvp.view;

import com.dante.knowledge.R;
import com.dante.knowledge.ui.BaseActivity;
import com.dante.knowledge.utils.Constants;

/**
 * Container for HDetailFragment.
 */
public class HDetailActivity extends BaseActivity {
    @Override
    protected void initLayoutId() {
        layoutId = R.layout.content_main;
    }

    @Override
    protected void initViews() {
        super.initViews();
        String url = getIntent().getStringExtra(Constants.URL);
        replaceFragment(HDetailFragment.newInstance(url), TabsFragment.MENU_H);
    }
}
