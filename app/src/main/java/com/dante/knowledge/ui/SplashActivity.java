package com.dante.knowledge.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.R.anim;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dante.knowledge.MainActivity;
import com.dante.knowledge.R;
import com.dante.knowledge.net.API;
import com.dante.knowledge.net.Net;
import com.dante.knowledge.utils.StringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;

import okhttp3.Call;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DURATION = 1500;
    private ImageView splash;
    private SharedPreferences sp;
    private String today;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        loadImageFile();
        setContentView(splash);

        today = StringUtil.formatDate(new Date());
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //if today is not last get splash date, get new splash.
        if (!today.equals(sp.getString(StringUtil.LAST_DATE, ""))) {
            getSplash();
        }

    }


    private void getSplash() {
        Net.get(API.SPLASH, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                startApp();
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    getSplashFile(jsonObject.getString("img"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    startApp();
                }
            }
        }, API.TAG_SPLASH);
    }

    private void getSplashFile(final String imgUrl) {
        OkHttpUtils.get().url(imgUrl).build().execute(
                new FileCallBack(getFilesDir().getAbsolutePath(), "splash.jpg") {
                    @Override
                    public void inProgress(float progress) {

                    }

                    @Override
                    public void onError(Call call, Exception e) {
                    }

                    @Override
                    public void onResponse(File file) {
                        sp.edit().putString(StringUtil.LAST_DATE, today).apply();
                    }
                });

    }

    private void startApp() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(anim.abc_fade_in, anim.abc_fade_out);
        finish();
    }

    private void loadImageFile() {
        splash = new ImageView(this);
        splash.setScaleType(ImageView.ScaleType.CENTER_CROP);
        File imgFile = new File(getFilesDir(), "splash.jpg");

        if (imgFile.exists()) {
            Glide.with(this).load(imgFile).animate(R.anim.scale_anim).into(splash);
        } else {
            Glide.with(this).load(R.mipmap.splash).animate(R.anim.scale_anim).into(splash);
        }
        splash.postDelayed(new Runnable() {
            @Override
            public void run() {
                startApp();
            }
        }, SPLASH_DURATION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        OkHttpUtils.getInstance().cancelTag(API.TAG_SPLASH);
    }
}
