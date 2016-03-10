package com.dante.knowledge.mvp.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.dante.knowledge.utils.Share;
import com.dante.knowledge.utils.UI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;

/**
 * Photo view fragment.
 */
public class ViewerFragment extends BaseFragment implements View.OnLongClickListener, View.OnClickListener {

    @Bind(R.id.image)
    TouchImageView imageView;
    private String url;
    private SaveImageTask task;
    private DetailActivity activity;
    private ShareActionProvider mShareActionProvider;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != task) {
            task.cancel(true);
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
        loadPicture();
    }

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
            getActivity().supportStartPostponedEnterTransition();
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


    public View getSharedElement() {
        return imageView;
    }

    @Override
    public boolean onLongClick(View v) {
        task = new SaveImageTask();
        task.execute(url);
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

    private class SaveImageTask extends AsyncTask<String, Void, File> {

        @Override
        protected File doInBackground(String... params) {
            if (isCancelled()) {
                return null;
            }
            int saveNum = SPUtil.getInt(Constants.SAVE_NUM);
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            Bitmap bitmap;
            for (String param : params) {
                File file = new File(dir, SPUtil.getString(Constants.DATE) + "_" + (++saveNum) + ".jpg");
                try {
                    bitmap = Glide.with(ViewerFragment.this).load(param)
                            .asBitmap()
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                    OutputStream os = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    SPUtil.save(Constants.SAVE_NUM, saveNum);
                    return file;

                } catch (InterruptedException | FileNotFoundException e) {
                    e.printStackTrace();
                    UI.showSnack(rootView, R.string.save_img_failed);
                } catch (ExecutionException e) {
                    UI.showSnack(rootView, R.string.save_img_failed);
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(File file) {
            if (isCancelled()) {
                return;
            }
            Toast.makeText(getContext(), getString(R.string.save_img_success) + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        }
    }

    private void setShareIntent() {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(
                    Share.getShareIntent(getString(R.string.share_app_description))
            );
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.share_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        setShareIntent();
    }
}
