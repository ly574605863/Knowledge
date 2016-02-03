package com.dante.knowledge.net;

import com.dante.knowledge.news.model.ZhihuDetail;
import com.dante.knowledge.news.model.ZhihuNews;
import com.google.gson.Gson;

/**
 * Created by yons on 16/1/29.
 */
public class GsonUtil {
    public static Gson mGson = new Gson();

    public static ZhihuNews parseNews(String latest){
        return mGson.fromJson(latest, ZhihuNews.class);
    }
    public static ZhihuDetail parseDetail(String latest){
        return mGson.fromJson(latest, ZhihuDetail.class);
    }
}
