[Check out English version](README.md)
--

介绍
===
Knowledge是我的MD风格和MVP框架的练习项目。你可以方便地阅读知乎日报和煎蛋网的新鲜事，后续还会增加浏览妹子图的功能，嘿嘿嘿~
可以学到的知识：
===
- Android Design库的使用
- 自定义你的基类Activity
- MVP模式的写法
- 使用Webview的注意事项
- OKhttp封装框架
- 用Gson解析网络数据
- 用Glide加载图片
- 加特效的启动页
- Activity的转换特效（对，duang的那种）

使用到的库
===
使用方法：复制到项目gradle文件下

    dependencies {
    debugCompile 'com.squareup.leakcanary:leakcanary-android:1.3.1' // or 1.4-beta1
    releaseCompile 'com.squareup.leakcanary:leakcanary-android-no-op:1.3.1' // or 1.4-beta1
    testCompile 'com.squareup.leakcanary:leakcanary-android-no-op:1.3.1'
    compile 'com.android.support:appcompat-v7:23.1.1'
    compile 'com.android.support:design:23.1.1'
    compile 'com.bigkoo:convenientbanner:2.0.5'
    compile 'com.android.support:cardview-v7:23.1.1'
    compile 'com.github.bumptech.glide:glide:3.6.0'
    compile 'com.jakewharton:butterknife:7.0.1'
    compile 'com.google.code.gson:gson:2.5'
    compile 'com.zhy:okhttputils:2.2.0'
    compile 'com.android.support:support-v4:23.1.1'
    compile 'com.android.support:recyclerview-v7:23.1.1'
    compile 'com.github.orhanobut:logger:1.12'
    }
