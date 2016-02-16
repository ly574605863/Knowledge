package com.dante.knowledge.net;

import com.dante.knowledge.news.model.ZhihuNews;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by yons on 16/2/15.
 */
public class DB {

    public static Realm realm = Realm.getDefaultInstance();

    public static void save(RealmObject realmObject) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmObject);
        realm.commitTransaction();
    }

    public static <T extends RealmObject> T getById(int id, Class<T> realmObjectClass) {
        return realm.where(realmObjectClass).equalTo("id", id).findFirst();
    }

    public static ZhihuNews getZhihuNews(String date) {
        return realm.where(ZhihuNews.class).equalTo(Constants.DATE, date).findFirst();
    }

    public static <T extends RealmObject> RealmResults<T> findAll(Class<T> realmObjectClass) {
        return realm.where(realmObjectClass).findAll();
    }
}
