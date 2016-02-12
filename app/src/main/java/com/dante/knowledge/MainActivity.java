package com.dante.knowledge;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dante.knowledge.news.view.NewsTabFragment;
import com.dante.knowledge.ui.AboutActivity;
import com.dante.knowledge.ui.BaseActivity;
import com.dante.knowledge.ui.SettingsActivity;
import com.dante.knowledge.utils.ShareUtil;
import com.dante.knowledge.utils.UiUtils;

import butterknife.Bind;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private Fragment currentFragment;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        super.initViews();
        setupDrawer();
        initNavigationView();
        initMainFragment();

    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }


    private void initMainFragment() {
        //当前fragment为空或者不是NewsTabFragment（主fragment）
        if (currentFragment == null || !(currentFragment instanceof NewsTabFragment)) {
            currentFragment = new NewsTabFragment();
        }
        replaceFragment(currentFragment, "main");
    }

    private void setupDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavigationView() {
        navView.setNavigationItemSelectedListener(this);
//        navView.inflateMenu(R.menu.main_drawer);
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
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

        if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_knowledge) {
            initMainFragment();
        } else if (id == R.id.nav_beauty) {
            UiUtils.showSnackLong(drawerLayout, R.string.column_not_available);

        } else if (id == R.id.nav_xxoo) {

        } else if (id == R.id.nav_share) {
            startActivity(
                    Intent.createChooser(
                            ShareUtil.getShareIntent(getString(R.string.share_app_description)),
                            getString(R.string.share_app)));

        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
