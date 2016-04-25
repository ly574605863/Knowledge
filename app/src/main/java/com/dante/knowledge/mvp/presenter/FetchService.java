package com.dante.knowledge.mvp.presenter;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dante.knowledge.mvp.model.HDetail;
import com.dante.knowledge.mvp.model.HItem;
import com.dante.knowledge.mvp.model.Image;
import com.dante.knowledge.mvp.other.DBParser;
import com.dante.knowledge.mvp.other.HParser;
import com.dante.knowledge.mvp.other.ImageHelper;
import com.dante.knowledge.mvp.view.HFragment;
import com.dante.knowledge.mvp.view.PictureFragment;
import com.dante.knowledge.net.API;
import com.dante.knowledge.net.Net;
import com.dante.knowledge.utils.Constants;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import okhttp3.Call;
import okhttp3.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class FetchService extends IntentService {

    public static final String ACTION_FETCH = "common_fetch";
    public static final String ACTION_FETCH_H_DETAIL = "fetch_h_detail";
    public static final String EXTRA_FETCHED_NUM = "fetched_num";
    public static final String EXTRA_FETCHED_RESULT = "fetched_result";
    private LocalBroadcastManager localBroadcastManager;
    public long GET_DURATION = 3000;
    private int type;
    private boolean stopFetchAll;
    private ImageHelper helper;

    public FetchService() {
        super("PictureFetchService");
    }


    public static void startActionFetch(Context context, int type, String response) {
        Intent intent = new Intent(context, FetchService.class);
        intent.setAction(ACTION_FETCH);
        intent.putExtra(Constants.TYPE, type);
        intent.putExtra(Constants.DATA, response);
        context.startService(intent);
    }

    public static void startFetchHDetail(Context context, String postLink) {
        Intent intent = new Intent(context, FetchService.class);
        intent.setAction(ACTION_FETCH_H_DETAIL);
        intent.putExtra(Constants.URL, postLink);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Realm realm = Realm.getDefaultInstance();
//        realm.beginTransaction();
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FETCH.equals(action)) {
                type = intent.getIntExtra(Constants.TYPE, 0);
                String data = intent.getStringExtra(Constants.DATA);
                parse(data);
            } else if (ACTION_FETCH_H_DETAIL.equals(action)) {
                String url = intent.getStringExtra(Constants.URL);
                stopFetchAll = true;
                fetchDetail(realm, url, true);
            }
        }
    }


    private void parse(String response) {
        boolean isSuccess = false;

        helper = new ImageHelper(this, type);
        if (type == PictureFragment.TYPE_GANK) {
            isSuccess = save(parseGANK(response));
        } else if (PictureFragment.TYPE_GANK < type
                && type <= PictureFragment.TYPE_DB_RANK) {
            isSuccess = save(helper.saveImages(DBParser.parse(response)));
        } else if (PictureFragment.TYPE_DB_RANK < type
                && type < HFragment.TYPE_H_ORIGINAL) {
            List<HItem> items = HParser.parseHItem(response);
            isSuccess = save(items);
            fetchAllDetail();
        }
        sendResult(isSuccess);
    }

    private <E extends RealmObject> boolean save(List<E> objects) {
        if (objects != null) {
            if (objects.size() >= 0) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(objects);
                realm.commitTransaction();
                realm.close();
                return true;
            }
        }
        return false;
    }

    private void fetchAllDetail() {
        Realm realm = Realm.getDefaultInstance();
        List<HItem> items = realm.where(HItem.class).findAll();
        for (HItem item : items) {
            final String url = item.getUrl();
            fetchDetail(realm, url, false);
        }
        realm.close();
    }

    private void fetchDetail(Realm realm, final String url, final boolean normalFetch) {
        boolean isExisted = !realm.where(HDetail.class).equalTo(Constants.URL, url).findAll().isEmpty();
        if (isExisted) {
            return;
        }

        final long lastGetTime = System.currentTimeMillis();

        Net.get(url, new Callback<HDetail>() {
            @Override
            public HDetail parseNetworkResponse(Response response) throws Exception {
                if (!normalFetch && stopFetchAll) {
                    Log.i("test", "stop ");
                    return null;
                }
                HParser parser = new HParser(FetchService.this, type);
                HDetail detail = parser.parseHDetail(response.body().string(), url);
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(detail);
                realm.commitTransaction();
                realm.close();
                sendResult(detail.getImages().size() != 0);
                return detail;
            }

            @Override
            public void onError(Call call, Exception e) {
                if (System.currentTimeMillis() - lastGetTime < GET_DURATION) {
                    Net.get(url, this, API.TAG_PICTURE);
                    return;
                }
                sendResult(false);
            }

            @Override
            public void onResponse(HDetail response) {

            }

        }, this);
    }

    private List<Image> parseGANK(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray array = jsonObject.getJSONArray("results");
            final int size = array.length();
            String[] urls = new String[size];
            String[] publishAts = new String[size];

            for (int i = 0; i < size; i++) {
                jsonObject = array.getJSONObject(i);
                urls[i] = jsonObject.getString("url");
                publishAts[i] = jsonObject.getString("publishedAt");
            }
            return helper.saveImages(urls, publishAts);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
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
