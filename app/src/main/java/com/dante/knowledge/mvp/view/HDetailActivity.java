package com.dante.knowledge.mvp.view;

import android.view.MenuItem;
import android.view.View;

import com.dante.knowledge.R;
import com.dante.knowledge.ui.BaseActivity;
import com.dante.knowledge.utils.Constants;

/**
 * Container for HDetailFragment.
 */
public class HDetailActivity extends BaseActivity {
    @Override
    protected void initLayoutId() {
        layoutId = R.layout.md_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        String url = getIntent().getStringExtra(Constants.URL);
        String title = getIntent().getStringExtra(Constants.TEXT);
        toolbar.setTitle(purifyTitle(title));
        //this method doesn't work, DKY
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        replaceFragment(HDetailFragment.newInstance(url), TabsFragment.MENU_H);
    }

    private String purifyTitle(String title) {
        String pattern = "\\[(.{0,6})\\]";
        return title.replaceAll(pattern, "");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
