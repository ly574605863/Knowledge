package com.dante.knowledge.mvp.other;

import android.content.Context;
import android.util.Log;

import com.dante.knowledge.mvp.model.HDetail;
import com.dante.knowledge.mvp.model.HItem;
import com.dante.knowledge.mvp.model.Image;
import com.dante.knowledge.utils.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import io.realm.RealmList;


/**
 * H post parser.
 */
public class HParser {

    private Context context;
    private int type;

    public HParser(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    public static boolean parseHItem(String response) {
        Realm realm = Realm.getDefaultInstance();
        List<HItem> items = new ArrayList<>();
        Document document = Jsoup.parse(response);
        Elements elements = document.select(".tr3 > td > h3 > a");
        for (int i = 3; i < elements.size(); i++) {
            Element post = elements.get(i);
            String url = post.attr("abs:href");
            String title = post.text();
            boolean notExisted = realm.where(HItem.class).equalTo(Constants.URL, url).findAll().isEmpty();
            if (notExisted) {
                HItem item = new HItem(System.currentTimeMillis() + "", url, title);
                items.add(item);
            }
            Log.i("test", "href>>>" + url + " text>>>" + title);
        }
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(items);
        realm.commitTransaction();
        realm.close();
        return items.size() != 0;
    }

    public boolean parseHDetail(String response, String postLink) {
        RealmList<Image> images = new RealmList<>();
        Document document = Jsoup.parse(response);
        Elements elements = document.select(".tpc_content > img[src]");
        for (int i = 0; i < elements.size(); i++) {
            Element img = elements.get(i);
            String src = img.attr("src");
            Log.i("test", " src>>>" + src);
            try {
                Image image = Image.getFixedImage(context, src, type);
                images.add(image);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
        HDetail detail = new HDetail(postLink, images);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(detail);
        realm.commitTransaction();
        realm.close();
        return !images.isEmpty();
    }
}
