package com.dante.knowledge.net;

import com.dante.knowledge.bean.LatestNews;
import com.google.gson.Gson;

/**
 * Created by yons on 16/1/29.
 */
public class GsonUtil {
    public static Gson mGson = new Gson();

    public static LatestNews deserialized(String latest){

        return mGson.fromJson(latest, LatestNews.class);
    }
}
