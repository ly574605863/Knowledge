package com.dante.knowledge.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.appcompat.R.anim;

import com.dante.knowledge.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yons on 16/1/29.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected int layoutId = R.layout.activity_base;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayoutId();
        initViews();
    }

    protected abstract void initLayoutId();

    protected void initViews() {
        setContentView(layoutId);
        ButterKnife.bind(this);
        initBar();
    }

    private void initBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    public void replaceFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(anim.abc_grow_fade_in_from_bottom, anim.abc_fade_out,
                anim.abc_fade_in, anim.abc_shrink_fade_out_from_bottom);
        transaction.replace(R.id.content_main, fragment, tag);
//        transaction.addToBackStack("");
        transaction.commit();
    }
}
