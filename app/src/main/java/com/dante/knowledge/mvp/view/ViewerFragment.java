package com.dante.knowledge.mvp.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.dante.knowledge.R;
import com.dante.knowledge.libraries.TouchImageView;
import com.dante.knowledge.ui.BaseFragment;
import com.dante.knowledge.utils.Constants;
import com.dante.knowledge.utils.SPUtil;
import com.dante.knowledge.utils.Tool;

import java.util.concurrent.ExecutionException;

import butterknife.Bind;

/**
 * Photo view fragment.
 */
public class ViewerFragment extends BaseFragment implements View.OnLongClickListener, View.OnClickListener {

    @Bind(R.id.image)
    TouchImageView imageView;
    private String url;
    private DetailActivity activity;
    private Bitmap bitmap;
    private AsyncTask loadPicture;
    private AsyncTask task;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != task) {
            task.cancel(true);
        }
        if (null != loadPicture) {
            loadPicture.cancel(true);
        }
        if (null != bitmap && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

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
        activity = (DetailActivity) getActivity();
        url = getArguments().getString(Constants.URL);
        ViewCompat.setTransitionName(imageView, url);

        loadPicture = new LoadPictureTask().execute();
//        loadPicture();
    }

    private Uri uri;
    //Make target a field member instead of an anonymous inner class
    //to avoid being GC the moment after loading the picture
    private SimpleTarget<GlideDrawable> target = new SimpleTarget<GlideDrawable>() {

        @Override
        public void onLoadFailed(Exception e, Drawable errorDrawable) {
            super.onLoadFailed(e, errorDrawable);
            loadPicture();
        }

        @Override
        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
            imageView.setImageDrawable(resource);
            activity.supportStartPostponedEnterTransition();
        }
    };
    private void loadPicture() {
        Glide.with(this)
                .load(url)
                .dontAnimate()
                .into(target);
    }

    @Override
    protected void initData() {
        imageView.setOnClickListener(this);
        imageView.setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        return true;
    }

    @Override
    public void onClick(View v) {
        activity.toggleUI();
        if (!SPUtil.getBoolean(Constants.HAS_HINT)) {
            Toast.makeText(getContext(), getString(R.string.view_img_hint), Toast.LENGTH_LONG).show();
            SPUtil.save(Constants.HAS_HINT, true);
        }
    }

    private class LoadPictureTask extends AsyncTask<Void, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Void... voids) {
            if (isCancelled()) {
                return null;
            }
            try {
                bitmap = Glide.with(ViewerFragment.this).load(url)
                        .asBitmap()
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap picture) {
            if (isCancelled()) {
                return;
            }
            imageView.setImageBitmap(picture);
            activity.supportStartPostponedEnterTransition();
            task = new ShareIntentTask().execute(picture);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && uri != null) {
            activity.setShareImageIntent(uri);
        }
    }

    private class ShareIntentTask extends AsyncTask<Bitmap, Void, Uri> {

        @Override
        protected Uri doInBackground(Bitmap... params) {
            if (isCancelled()) {
                return null;
            }
            return Tool.bitmapToUri(params[0]);
        }

        @Override
        protected void onPostExecute(Uri result) {
            uri = result;
            setUserVisibleHint(getUserVisibleHint());
        }
    }

}
