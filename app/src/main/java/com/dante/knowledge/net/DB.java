package com.dante.knowledge.net;

import com.dante.knowledge.Images.model.Image;
import com.dante.knowledge.news.model.FreshData;
import com.dante.knowledge.news.model.ZhihuData;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by yons on 16/2/15.
 */
public class DB {

    public static Realm realm = Realm.getDefaultInstance();

    public static void saveOrUpdate(RealmObject realmObject) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmObject);
        realm.commitTransaction();
    }

    public static <T extends RealmObject> void saveList(List<T> realmObjects) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmObjects);
        realm.commitTransaction();
    }

    public static void save(RealmObject realmObject) {
        realm.beginTransaction();
        realm.copyToRealm(realmObject);
        realm.commitTransaction();
    }

    public static <T extends RealmObject> T getById(int id, Class<T> realmObjectClass) {
        return realm.where(realmObjectClass).equalTo("id", id).findFirst();
    }

    public static ZhihuData getZhihuNews(String date) {
        return realm.where(ZhihuData.class).equalTo(Constants.DATE, date).findFirst();
    }

    public static <T extends RealmObject> RealmResults<T> findAll(Class<T> realmObjectClass) {
        return realm.where(realmObjectClass).findAll();
    }

    public static <T extends RealmObject> void deleteAll(Class<T> realmObjectClass) {
        realm.beginTransaction();
        realm.where(realmObjectClass).findAll().clear();
        realm.commitTransaction();
    }

    public static FreshData getFreshNews(int page) {
        return realm.where(FreshData.class).findFirst();
    }
    public static RealmResults<Image> getImages(int type) {
        return realm.where(Image.class).equalTo(Constants.TYPE, type).findAll();
    }
}
