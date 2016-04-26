[Check out English version](README_en.md)
--

#妹知，既有妹子又有知识

## 目录
1. [简介](#简介)
1. [截图](#截图)
1. [知识](#可以学到)
1. [依赖](#使用到的库)
1. [支持](#支持我)

---

##简介
Knowledge是一个[MaterialDesign](http://www.google.com/design/spec/material-design/)风格和[MVP](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0202/2397.html)模式的项目。你可以方便地阅读知乎日报和煎蛋网的新鲜事，还可以浏览妹子图，让我们一起嘿嘿嘿~（作者比较懒，只在[360手机助手](http://openbox.mobilem.360.cn/index/d/sid/3220990)上线了。如果你在学习中遇到任何bug、疑问，请[提交一个issue](https://github.com/DanteAndroid/Knowledge/issues/new)）

##截图
<a href="./screenshots/girl.jpg"><img src="./screenshots/girl.jpg" width="40%"/></a><a href="./screenshots/main.jpg"><img src="./screenshots/main.jpg" width="40%"/></a>
<a href="./screenshots/detail.jpg"><img src="./screenshots/detail.jpg" width="40%"/></a><a href="./screenshots/menu.jpg"><img src="./screenshots/menu.jpg" width="40%"/></a>
<!--![ScreenShots](screenshots/girl.jpg)
![ScreenShots](screenshots/main.jpg)
![ScreenShots](screenshots/detail.jpg)
![ScreenShots](screenshots/menu.jpg)-->

##可以学到：
- Android Design库的使用
- 自定义你的基类Activity
- 应用MVP模式到项目中
- 使用Webview时避免内存泄露
- OKhttp封装框架的用法
- 用Gson解析网络数据
- 用Glide加载图片
- 用Realm缓存数据
- 加特效的启动页的写法
- 设置页的写法
- Activity的转换特效（对，duang的那种）
- 更多惊喜等待你发现（手动斜眼）

## 使用到的库
    dependencies {
        debugCompile 'com.squareup.leakcanary:leakcanary-android:1.3.1' //检测内存泄露
        compile 'com.android.support:design:23.1.1'//design控件库
        compile 'com.bigkoo:convenientbanner:2.0.5'//一个图片轮播控件
        compile 'com.android.support:cardview-v7:23.1.1'
        compile 'com.github.bumptech.glide:glide:3.6.0'//图片加载库
        compile 'com.jakewharton:butterknife:7.0.1'//帮你省去findview
        compile 'com.google.code.gson:gson:2.5'
        compile 'com.zhy:okhttputils:2.2.0'//okhttp封装
        compile 'com.android.support:recyclerview-v7:23.1.1'
        compile 'io.realm:realm-android:0.87.4'//快速、简洁的跨平台数据库
        compile 'com.github.orhanobut:logger:1.12'//漂亮的log工具
    }

## TODO
- [ ] 隐藏模式
- [ ] 更换主题

## 支持我
如果你喜欢这个repository，请我喝杯咖啡，我会更用心让它更完美~
![打开你的微信](get_me_a_drink.jpg)
