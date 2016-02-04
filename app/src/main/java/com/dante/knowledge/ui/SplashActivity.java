package com.dante.knowledge.ui;

import android.annotation.TargetApi;
import android.content.Intent;
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
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;

public class SplashActivity extends AppCompatActivity {

    private ImageView splash;
    private File imgFile;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(splash);
        initSplashImage();
    }

    private void initSplashImage() {

        loadImageFile();

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
                OkHttpUtils.get().url(API.SPLASH).build().execute(new FileCallBack(getFilesDir().getAbsolutePath(), "splash.jpg") {
                    @Override
                    public void inProgress(float progress) {

                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        startApp();
                    }

                    @Override
                    public void onResponse(File file) {
                        saveImageFile(file);
                    }
                });
            }

            @Overridesplasp
            public void onAnimationRepeat(Animation animation) {

            }
        });
        splash.startAnimation(animation);

    }

    private void saveImageFile(File file) {
        imgFile = file;
    }

    private void startApp() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
        finish();
    }

    private void loadImageFile() {
        splash = new ImageView(this);

        imgFile = new File(getFilesDir(), "splash.jpg");

        if (imgFile.exists()) {
            Glide.with(this).load(imgFile).crossFade().fitCenter().into(splash);
        } else {
            Glide.with(this).load(R.mipmap.splash).crossFade().fitCenter().into(splash);
        }

    }
}
