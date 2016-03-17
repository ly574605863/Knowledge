package com.dante.knowledge;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.dante.knowledge.mvp.view.TabsFragment;
import com.dante.knowledge.net.DB;
import com.dante.knowledge.ui.AboutActivity;
import com.dante.knowledge.ui.BaseActivity;
import com.dante.knowledge.ui.SettingFragment;
import com.dante.knowledge.ui.SettingsActivity;
import com.dante.knowledge.utils.Imager;
import com.dante.knowledge.utils.SPUtil;
import com.dante.knowledge.utils.Share;
import com.dante.knowledge.utils.UI;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UHandler;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateStatus;

import butterknife.Bind;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private String currentType;
    private Fragment currentFragment;

    boolean backPressed;

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
        agent.enable(new IUmengRegisterCallback() {
            @Override
            public void onRegistered(String s) {
                Log.i("device_token", s);
            }
        });
        UmengUpdateAgent.silentUpdate(this);
        UmengUpdateAgent.setUpdateUIStyle(UpdateStatus.STYLE_NOTIFICATION);
        //        Bmob.initialize(this, "3478b1205772b294ac0741d0b136e25e");
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
        View header = navView.getHeaderView(0);
        ImageView head = (ImageView) header.findViewById(R.id.headImage);
        Imager.load(this, R.drawable.head, head);
        navView.setNavigationItemSelectedListener(this);
        if (SPUtil.getBoolean(SettingFragment.SECRET_MODE)) {
            navView.inflateMenu(R.menu.main_menu_all);
        } else {
            navView.inflateMenu(R.menu.main_drawer);
        }
        //select the first menu at startup
        navView.getMenu().getItem(0).setChecked(true);
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
        UI.showSnack(drawerLayout, R.string.press_back_twice);
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
        DB.realm.close();
    }
}
