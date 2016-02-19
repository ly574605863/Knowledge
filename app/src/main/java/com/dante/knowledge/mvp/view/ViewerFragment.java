package com.dante.knowledge.mvp.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.dante.knowledge.R;
import com.dante.knowledge.utils.Constants;
import com.dante.knowledge.ui.BaseFragment;
import com.dante.knowledge.utils.SP;
import com.dante.knowledge.utils.UI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;

/**
 * Created by yons on 16/2/18.
 */
public class ViewerFragment extends BaseFragment implements View.OnLongClickListener {

    @Bind(R.id.image)
    com.dante.knowledge.libraries.TouchImageView imageView;
    private String url;

    public static ViewerFragment newInstance(String url) {
        ViewerFragment fragment = new ViewerFragment();
        Bundle args = new Bundle();
        args.putString(Constants.URL, url);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_viewer;
    }

    @Override
    protected void initViews() {
        url = getArguments().getString(Constants.URL);
        ViewCompat.setTransitionName(imageView, url);
        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        imageView.setImageDrawable(resource);
                        imageView.setDrawingCacheEnabled(true);
                        getActivity().supportStartPostponedEnterTransition();

                    }
                });
    }

    @Override
    protected void initData() {
        imageView.setOnLongClickListener(this);
    }


    public View getSharedElement() {
        return imageView;
    }

    @Override
    public boolean onLongClick(View v) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                int saveNum=SP.getInt(Constants.SAVE_NUM);
                File dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File file = new File(dir, SP.getString(Constants.DATE)+"_"+(++saveNum) + ".jpg");
                Bitmap bitmap = null;
                try {
                    bitmap = Glide.with(ViewerFragment.this).load(url)
                            .asBitmap()
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                    OutputStream os = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    SP.save(Constants.SAVE_NUM, saveNum);
                } catch (InterruptedException | FileNotFoundException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    UI.showSnack(rootView, R.string.save_img_failed);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void saveNum) {
                super.onPostExecute(saveNum);
                UI.showSnack(rootView, R.string.save_img_success);
            }
        }.execute();

        return true;
    }
}
