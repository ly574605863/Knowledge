package com.dante.knowledge.mvp.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.dante.knowledge.R;
import com.dante.knowledge.ui.BaseFragment;
import com.dante.knowledge.utils.Constants;
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
public class ViewerFragment extends BaseFragment implements View.OnLongClickListener, View.OnClickListener {

    @Bind(R.id.image)
    ImageView imageView;
    private String url;
    private SaveImageTask task;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null!=task){
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
        url = getArguments().getString(Constants.URL);
        ViewCompat.setTransitionName(imageView, url);
        loadPicture();
    }

    private void loadPicture() {
        Glide.with(this)
                .load(url)
                .dontAnimate()
                .into(new SimpleTarget<GlideDrawable>() {

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
                } );
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
        task=new SaveImageTask();
        task.execute(url);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (! SP.getBoolean(Constants.HAS_HINT)){
            Toast.makeText(getContext(), getString(R.string.view_img_hint) , Toast.LENGTH_LONG).show();
            SP.save(Constants.HAS_HINT, true);
        }
    }

    private class SaveImageTask extends AsyncTask<String, Void, File>{

        @Override
        protected File doInBackground(String... params) {
            if (isCancelled()){
                return null;
            }
            int saveNum=SP.getInt(Constants.SAVE_NUM);
            File dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            Bitmap bitmap = null;
            for (String param : params) {
                File file = new File(dir, SP.getString(Constants.DATE) + "_" + (++saveNum) + ".jpg");
                try {
                    bitmap = Glide.with(ViewerFragment.this).load(param)
                            .asBitmap()
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                    OutputStream os = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    SP.save(Constants.SAVE_NUM, saveNum);
                    return file;

                } catch (InterruptedException | FileNotFoundException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    UI.showSnack(rootView, R.string.save_img_failed);
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(File file) {
            if (isCancelled()){
                return;
            }
            Toast.makeText(getContext(), getString(R.string.save_img_success)+ file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        }
    }
}
