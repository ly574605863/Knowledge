package com.dante.knowledge.net;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by yons on 16/1/29.
 */
public class Net {
    public static void get(String url, StringCallback callback){
        OkHttpUtils.get().url(url).build().execute(callback);
    }
}
