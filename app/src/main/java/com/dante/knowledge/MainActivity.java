package com.dante.knowledge;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.dante.knowledge.mvp.view.TabsFragment;
import com.dante.knowledge.ui.AboutActivity;
import com.dante.knowledge.ui.BaseActivity;
import com.dante.knowledge.ui.SettingFragment;
import com.dante.knowledge.ui.SettingsActivity;
import com.dante.knowledge.utils.Imager;
import com.dante.knowledge.utils.SPUtil;
import com.dante.knowledge.utils.Share;
import com.dante.knowledge.utils.UI;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateStatus;

import java.io.File;
import java.util.Random;

import butterknife.Bind;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    boolean backPressed;
    private String currentType;
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
        replace(TabsFragment.MENU_NEWS);
        initSDK();
    }

    private void initSDK() {
        PushAgent agent = PushAgent.getInstance(this);
        agent.enable();
        if (new Random().nextBoolean()) {
            update();
        }
        //   Bmob.initialize(this, "3478b1205772b294ac0741d0b136e25e");
    }

    private void update() {
        UmengUpdateAgent.silentUpdate(this);
        UmengUpdateAgent.setUpdateUIStyle(UpdateStatus.STYLE_NOTIFICATION);
        UmengUpdateAgent.setDownloadListener(new UmengDownloadListener() {
            @Override
            public void OnDownloadStart() {
            }

            @Override
            public void OnDownloadUpdate(int i) {
            }

            @Override
            public void OnDownloadEnd(int status, String path) {
                if (status == UpdateStatus.DOWNLOAD_COMPLETE_SUCCESS) {
                    UmengUpdateAgent.startInstall(getApplicationContext(), new File(path));
                }
            }
        });
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    private void replace(String type) {
        if (!type.equals(currentType)) {
            currentType = type;
            replaceFragment(TabsFragment.newInstance(type), type);
        }
    }

    private void setupDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavigationView() {
        //load headerView's image
        Imager.load(this, R.drawable.head, (ImageView) navView.getHeaderView(0).findViewById(R.id.headImage));
        navView.setNavigationItemSelectedListener(this);
        boolean isSecretOn = SPUtil.getBoolean(SettingFragment.SECRET_MODE);
        if (isSecretOn) {
            navView.inflateMenu(R.menu.main_menu_all);
        } else {
            navView.inflateMenu(R.menu.main_drawer);
        }
        //select the first menu at startup
        Menu menu = navView.getMenu();
        menu.getItem(0).setChecked(true);

        menu.getItem(0).setIcon(
                new IconicsDrawable(this).
                        icon(GoogleMaterial.Icon.gmd_explore));
        menu.getItem(1).setIcon(
                new IconicsDrawable(this).
                        icon(GoogleMaterial.Icon.gmd_face)
                        .color(Color.RED));
        Menu sub = menu.getItem(isSecretOn ? 3 : 2).getSubMenu();
        sub.getItem(0).setIcon(
                new IconicsDrawable(this).
                        icon(GoogleMaterial.Icon.gmd_share)
                        .color(Color.DKGRAY));
        sub.getItem(1).setIcon(
                new IconicsDrawable(this).
                        icon(GoogleMaterial.Icon.gmd_settings)
                        .color(Color.GRAY));
        if (isSecretOn) {
            menu.getItem(2).setIcon(new IconicsDrawable(this)
                    .icon(GoogleMaterial.Icon.gmd_whatshot)
                    .color(Color.WHITE));
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            doublePressBackToQuit();
        }
    }

    private void doublePressBackToQuit() {
        if (backPressed) {
            super.onBackPressed();
            return;
        }
        backPressed = true;
        UI.showSnack(drawerLayout, R.string.leave_app);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressed = false;
            }
        }, 2000);
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
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        supportPostponeEnterTransition();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_knowledge) {
            replace(TabsFragment.MENU_NEWS);
        } else if (id == R.id.nav_beauty) {
            replace(TabsFragment.MENU_PIC);

        } else if (id == R.id.nav_secret_mode) {
            replace(TabsFragment.MENU_SECRET);

        } else if (id == R.id.nav_share) {
            Share.shareText(this, getString(R.string.share_app_description));

        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
