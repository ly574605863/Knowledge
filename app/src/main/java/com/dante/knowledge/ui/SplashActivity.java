package com.dante.knowledge.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.R.anim;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dante.knowledge.MainActivity;
import com.dante.knowledge.R;
import com.dante.knowledge.net.API;
import com.dante.knowledge.net.Net;
import com.dante.knowledge.utils.StringUtil;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;

import okhttp3.Call;

public class SplashActivity extends AppCompatActivity {

    private static final String LAST_TIME = "last_time";
    // Don't show splash too often, so give it a least duration
    private static final long SPLASH_DURATION = 10 * 1000;
    private ImageView splash;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
//        long now=System.currentTimeMillis();
//        String lastTime=getPreferences(MODE_PRIVATE).getString(LAST_TIME, now+"");
//        if (now- Long.valueOf(lastTime)< SPLASH_DURATION){
//            startApp();
//        }else {
        loadImageFile();
        setContentView(splash);
        startSplash();
//        }
    }

    private void startAnimation(final String imgUrl) {
        final ScaleAnimation animation = new ScaleAnimation(
                1.0f, 1.2f, //from x to xx
                1.0f, 1.2f, //from y to yy
                Animation.RELATIVE_TO_SELF, 0.5f,//pivotX type, value
                Animation.RELATIVE_TO_SELF, 0.5f);//pivotY type, value
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                OkHttpUtils.get().url(imgUrl).build().execute(
                        new FileCallBack(getFilesDir().getAbsolutePath(), "splash.jpg") {
                            @Override
                            public void inProgress(float progress) {

                            }

                            @Override
                            public void onError(Call call, Exception e) {
                                startApp();
                            }

                            @Override
                            public void onResponse(File file) {
                                startApp();
                            }
                        });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setFillAfter(true);
        animation.setDuration(3000);
        splash.startAnimation(animation);
    }

    private void startSplash() {
        Net.get(API.SPLASH, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                startApp();
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    startAnimation(jsonObject.getString("img"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    startApp();
                }
            }
        }, null);
    }

    private void startApp() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
        finish();
    }

    private void loadImageFile() {
        splash = new ImageView(this);
        splash.setScaleType(ImageView.ScaleType.FIT_XY);
        File imgFile = new File(getFilesDir(), "splash.jpg");

        if (imgFile.exists()) {
            Glide.with(this).load(imgFile).crossFade().into(splash);
        } else {
            Glide.with(this).load(R.mipmap.splash).crossFade().into(splash);
        }

//        SharedPreferences.Editor sp=getPreferences(MODE_PRIVATE).edit();
//        sp.putString(LAST_TIME, System.currentTimeMillis()+"");
//        sp.apply();
    }
}
