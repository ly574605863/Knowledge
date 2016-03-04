package com.dante.knowledge.mvp.other;

import android.content.Context;

import com.dante.knowledge.mvp.model.Image;
import com.dante.knowledge.mvp.presenter.FetchService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A helper of Image model transactions.
 */
public class ImageHelper {

    private int type;
    private Context context;

    public ImageHelper(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    public boolean saveImages(String[] urls, String[] publishAts) {
        List<Image> images = new ArrayList<>();
        for (int i = 0; i < urls.length; i++) {
            try {
                Image image = Image.getFixedImage(context, urls[i], type);
                if (publishAts != null) {
                    image.setPublishedAt(publishAts[i]);
                }
                images.add(image);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (images.size() >= 0) {
            FetchService.realm.copyToRealmOrUpdate(images);
            FetchService.realm.commitTransaction();
        }
        return images.size() != 0;
    }

    public boolean saveImages(String[] urls) {
        return saveImages(urls, null);
    }

}
