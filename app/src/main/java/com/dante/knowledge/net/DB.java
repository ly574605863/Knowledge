package com.dante.knowledge.net;

import com.dante.knowledge.mvp.model.Image;
import com.dante.knowledge.utils.Constants;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Deals with cache, data
 */
public class DB {

    public static Realm realm;

    public static void saveOrUpdate(RealmObject realmObject) {
        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
        }
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmObject);
        realm.commitTransaction();
    }

    public static <T extends RealmObject> void saveList(List<T> realmObjects) {
        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
        }
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmObjects);
        realm.commitTransaction();
    }

    public static void save(RealmObject realmObject) {
        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
        }
        realm.beginTransaction();
        realm.copyToRealm(realmObject);
        realm.commitTransaction();
    }

    public static <T extends RealmObject> T getById(int id, Class<T> realmObjectClass) {
        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
        }
        return realm.where(realmObjectClass).equalTo("id", id).findFirst();
    }

    public static <T extends RealmObject> T getByUrl(String url, Class<T> realmObjectClass) {
        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
        }
        return realm.where(realmObjectClass).equalTo(Constants.URL, url).findFirst();
    }

    public static <T extends RealmObject> boolean isUrlExisted(String url, Class<T> realmObjectClass) {
        return getByUrl(url, realmObjectClass) != null;
    }


    public static <T extends RealmObject> RealmResults<T> findAll(Class<T> realmObjectClass) {
        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
        }
        return realm.where(realmObjectClass).findAll();
    }

    public static <T extends RealmObject> RealmResults<T> findAllDateSorted(Class<T> realmObjectClass) {
        RealmResults<T> results = findAll(realmObjectClass);
        results.sort(Constants.DATE, Sort.DESCENDING);
        return results;
    }

    public static <T extends RealmObject> void clear(Class<T> realmObjectClass) {
        realm.beginTransaction();
        findAll(realmObjectClass).clear();
        realm.commitTransaction();
    }

    public static RealmResults<Image> getImages(int type) {
        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
        }
        RealmResults<Image> results = realm.where(Image.class).equalTo("type", type).findAll();
        results.sort("publishedAt", Sort.DESCENDING);
        return results;
    }
}
