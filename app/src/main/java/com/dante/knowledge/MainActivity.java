package com.dante.knowledge;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.dante.knowledge.news.view.NewsFragment;
import com.dante.knowledge.ui.BaseActivity;
import com.dante.knowledge.ui.SettingsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.content_main)
    FrameLayout contentMain;
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    private Fragment mFragment;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        super.initViews();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        initNavigationView();
        mFragment = new NewsFragment();
        replaceFragment(mFragment, "main");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "这是个萌萌的按钮( ⊙ o ⊙ )", Snackbar.LENGTH_SHORT)
                        .setAction("知道啦", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                scrollToTop();
                            }
                        }).show();
            }
        });
    }

    private void initNavigationView() {
        navView.setNavigationItemSelectedListener(this);
        navView.inflateMenu(R.menu.activity_main_drawer);
    }

    private void scrollToTop() {
        NewsFragment fragment = ((NewsFragment) mFragment);
        RecyclerView recyclerView = fragment.getmRecyclerView();
        if (null != recyclerView) {
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (manager.findLastVisibleItemPosition() < 35) {
                recyclerView.smoothScrollToPosition(0);

            } else {
                recyclerView.scrollToPosition(0);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_knowledge) {
            mFragment = new NewsFragment();
            replaceFragment(mFragment, "main");
        } else if (id == R.id.nav_fresh) {

        } else if (id == R.id.nav_beauty) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_setting) {

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
