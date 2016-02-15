package com.dante.knowledge.net;

import com.dante.knowledge.news.model.PostEntity;
import com.dante.knowledge.news.model.ZhihuDetail;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by yons on 16/2/15.
 */
public class DB {

    public static Realm realm = Realm.getDefaultInstance();

    public static void save(RealmObject detailNews) {
        realm.beginTransaction();
        realm.copyToRealm(detailNews);
        realm.commitTransaction();
    }

    public static ZhihuDetail getZhihuDetail(int id) {
        return realm.where(ZhihuDetail.class).equalTo("id", id).findFirst();
    }

    public static PostEntity getFreshDetail(int id) {
        return realm.where(PostEntity.class).equalTo("id", id).findFirst();
    }

}
