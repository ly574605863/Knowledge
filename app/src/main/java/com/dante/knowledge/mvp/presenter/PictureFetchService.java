package com.dante.knowledge.mvp.presenter;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.dante.knowledge.mvp.model.Image;
import com.dante.knowledge.mvp.view.PictureFragment;
import com.dante.knowledge.net.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class PictureFetchService extends IntentService {

    public static final String ACTION_FETCH = "com.dante.knowledge.mvp.presenter.action.fetch";
    public static final String ACTION_BAZ = "";
    public static final String EXTRA_PARAM2 = "";
    public static final String EXTRA_FETCHED_NUM = "fetched_num";
    public static final String EXTRA_FETCHED_RESULT = "fetched_result";
    private LocalBroadcastManager localBroadcastManager;
    public long GET_DURATION = 3000;
    private int type;
    private String[] publishAts;
    private String data;

    public PictureFetchService() {
        super("PictureFetchService");
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void startActionFetch(Context context, int type, String response) {
        Intent intent = new Intent(context, PictureFetchService.class);
        intent.setAction(ACTION_FETCH);
        intent.putExtra(Constants.TYPE, type);
        intent.putExtra(Constants.DATA, response);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, PictureFetchService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FETCH.equals(action)) {
                type = intent.getIntExtra(Constants.TYPE, 0);
                data = intent.getStringExtra(Constants.DATA);
                handleActionFetch(data);
            }
        }
    }

    private void handleActionFetch(final String response) {
        parse(response);
    }


    private void parse(String response) {
        if (type == PictureFragment.TYPE_GANK) {

            parseGANK(response);

        } else if (PictureFragment.TYPE_GANK < type
                && type <= PictureFragment.TYPE_DB_RANK) {

            parseDB(response);

        } else if (PictureFragment.TYPE_DB_RANK < type
                && type < PictureFragment.TYPE_H_STREET) {
            parseH(response);
        }
    }


    private void parseH(String response) {

    }

    private void parseDB(String response) {
        Document document = Jsoup.parse(response);
        Elements elements = document.select("div[class=thumbnail] > div[class=img_single] > a > img");
        final int size = elements.size();
        String[] urls = new String[size];
        for (int i = 0; i < size; i++) {
            urls[i] = elements.get(i).attr("src");
        }
        saveImages(urls);
    }

    private void parseGANK(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray array = jsonObject.getJSONArray("results");
            final int size = array.length();
            String[] urls = new String[size];
            publishAts = new String[size];

            for (int i = 0; i < size; i++) {
                jsonObject = array.getJSONObject(i);
                urls[i] = jsonObject.getString("url");
                publishAts[i] = jsonObject.getString("publishedAt");
            }
            saveImages(urls);
        } catch (JSONException e) {
            sendResult(false);
            e.printStackTrace();
        }
    }

    private void saveImages(String[] urls) {
        List<Image> images = new ArrayList<>();
        for (int i = 0; i < urls.length; i++) {
            try {
                Image image = Image.getFixedImage(this, urls[i], type);
                if (type == PictureFragment.TYPE_GANK) {
                    image.setPublishedAt(publishAts[i]);
                }
                images.add(image);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(images);
        realm.commitTransaction();
        realm.close();

        sendResult(true);
    }

    private void sendResult(boolean isSuccess) {
        Intent broadcast = new Intent(ACTION_FETCH);
        broadcast.putExtra(EXTRA_FETCHED_RESULT, isSuccess);
        localBroadcastManager.sendBroadcast(broadcast);
    }

    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
