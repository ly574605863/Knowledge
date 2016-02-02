package com.dante.knowledge.net;

import com.dante.knowledge.news.model.DetailNews;
import com.dante.knowledge.news.model.LatestNews;
import com.google.gson.Gson;

/**
 * Created by yons on 16/1/29.
 */
public class GsonUtil {
    public static Gson mGson = new Gson();

    public static LatestNews parseNews(String latest){
        return mGson.fromJson(latest, LatestNews.class);
    }
    public static DetailNews parseDetail(String latest){
        return mGson.fromJson(latest, DetailNews.class);
    }
}
