package com.dante.knowledge.Images.model;

import android.os.AsyncTask;

import com.dante.knowledge.net.DB;
import com.dante.knowledge.news.interf.OnLoadDataListener;
import com.dante.knowledge.news.view.PictureFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by yons on 16/2/17.
 */
public class PictureParser {
    private int type;
    private String response;
    private OnLoadDataListener<Image> listener;
    public static AsyncTask<String, Void, List<Image>> asyncTask;

    public PictureParser(int type, String response, OnLoadDataListener<Image> listener) {
        this.listener = listener;
        this.type = type;
        this.response = response;
    }


    public void parse() {
        if (type == PictureFragment.TYPE_GANK) {
            parseGANK();
        } else {
            parseDB();
        }
    }

    private void parseDB() {

    }

    public static void cancelTask() {
        if (null != asyncTask && !asyncTask.isCancelled()) {
            asyncTask.cancel(true);
        }
    }

    private void parseGANK() {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray array = jsonObject.getJSONArray("results");
            final int size = array.length();
            String[] urls = new String[size];
            for (int i = 0; i < size; i++) {
                jsonObject = array.getJSONObject(i);
                urls[i] = jsonObject.getString("url");
            }
            saveImages(urls);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveImages(String[] urls) {
        asyncTask = new PictureTask();
        asyncTask.execute(urls);

    }

    public class PictureTask extends AsyncTask<String, Void, List<Image>> {
        @Override
        protected List<Image> doInBackground(String... strings) {
            List<Image> images = new ArrayList<>();
            if (!isCancelled()) {
                for (String url : strings) {
                    try {
                        images.add(Image.getFixedImage(url));
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return images;
        }

        @Override
        protected void onPostExecute(List<Image> images) {
            if (images.size()==0) {
                listener.onFailure("image task failed", null);
                return;
            }
            DB.saveList(images);
            listener.onDataSuccess(images.get(0));
        }

    }
}
