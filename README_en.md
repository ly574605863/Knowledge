[中文版点我](README_cn.md)
--

Introduction
===
Knowledge is an app you can read Zhihu daily and JianDan science articles with. (and browse cute girls pictures :D)
It conforms to Material Design.

You can learn
==
- Android Desgn library
- BaseActivity and BaseFragment
- MVP pattern
- Use webview and avoid memory leak
- Okhttp request encapsulation
- Parse response with Gson
- Load image with Glide library
- Splash page with scale animation
- Activity shared element transition animation

Repositories
===
how to use: copy the following to app.gradle

    dependencies {
    debugCompile 'com.squareup.leakcanary:leakcanary-android:1.3.1' //help you to find memory leak
    compile 'com.android.support:design:23.1.1'//design widgets
    compile 'com.bigkoo:convenientbanner:2.0.5'//a auto-scroll viewPager banner
    compile 'com.android.support:cardview-v7:23.1.1'
    compile 'com.github.bumptech.glide:glide:3.6.0'//load pictures
    compile 'com.jakewharton:butterknife:7.0.1'//help you saving "findViewById"
    compile 'com.google.code.gson:gson:2.5'
    compile 'com.zhy:okhttputils:2.2.0'
    compile 'com.android.support:recyclerview-v7:23.1.1'
    compile 'com.github.orhanobut:logger:1.12'//a beautiful log util
    }

