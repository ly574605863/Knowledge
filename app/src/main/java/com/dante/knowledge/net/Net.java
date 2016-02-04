package com.dante.knowledge.net;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * Net request encapsulation
 */
public class Net {

    public static void get(String url, StringCallback callback, Object tag) {
        OkHttpUtils.get().url(url).tag(tag)
                .build().execute(callback);
    }

}
