package com.dante.knowledge.net;

import com.dante.knowledge.news.model.FreshDetail;
import com.dante.knowledge.news.model.FreshNews;
import com.dante.knowledge.news.model.ZhihuDetail;
import com.dante.knowledge.news.model.ZhihuNews;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.RealmObject;

/**
 * Created by yons on 16/1/29.
 */
public class Json {
    public static Gson mGson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaringClass().equals(RealmObject.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }).create();

    public static ZhihuNews parseZhihuNews(String latest) {
        return mGson.fromJson(latest, ZhihuNews.class);
    }

    public static ZhihuDetail parseZhihuDetail(String detail) {
        return mGson.fromJson(detail, ZhihuDetail.class);
    }

    public static FreshNews parseFreshNews(String fresh) {
        return mGson.fromJson(fresh, FreshNews.class);
    }

    public static FreshDetail parseFreshDetail(String detail) {
        return mGson.fromJson(detail, FreshDetail.class);
    }

    public static <News> News parseNews(String response, Class<News> clz) {
        return mGson.fromJson(response, clz);
    }

}
