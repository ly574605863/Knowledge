package com.dante.knowledge.mvp.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.dante.knowledge.KnowledgeApp;
import com.dante.knowledge.R;
import com.dante.knowledge.libraries.TouchImageView;
import com.dante.knowledge.ui.BaseFragment;
import com.dante.knowledge.utils.BlurBuilder;
import com.dante.knowledge.utils.Constants;
import com.dante.knowledge.utils.Share;
import com.dante.knowledge.utils.Tool;
import com.dante.knowledge.utils.UI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;

/**
 * Photo view fragment.
 */
public class ViewerFragment extends BaseFragment implements View.OnLongClickListener, View.OnClickListener {

    @Bind(R.id.headImage)
    TouchImageView imageView;
    private String url;
    private DetailActivity activity;
    private Bitmap bitmap;
    private AsyncTask loadPicture;
    private AsyncTask share;
    private AsyncTask save;
    private Bitmap blurBitmap;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != share) {
            share.cancel(true);
        }
        if (null != loadPicture) {
            loadPicture.cancel(true);
        }
        if (null != save) {
            save.cancel(true);
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
    }

    @Override
    protected void initData() {
        imageView.setOnClickListener(this);
        imageView.setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        if (blurBitmap != null) {
            imageView.setImageBitmap(blurBitmap);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final String[] items = {getString(R.string.share_to), getString(R.string.save_img)};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    share = new ShareIntentTask().execute(bitmap);
                } else if (which == 1) {
                    activity.hideSystemUi();
                    save = new SaveImageTask().execute(bitmap);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                imageView.setImageBitmap(bitmap);
                activity.hideSystemUi();
            }
        });
        dialog.show();
        return true;
    }


    private class BlurTask extends AsyncTask<Bitmap, Void, Void> {

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            //change the 'reuseBitmap' to true to blur the image persistently
            blurBitmap = BlurBuilder.blur(bitmaps[0], BlurBuilder.BLUR_RADIUS_MEDIUM, false);
            return null;
        }

    }

    @Override
    public void onClick(View v) {
        activity.toggleSystemUI();
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
            //prepare blur bitmap
            new BlurTask().execute(bitmap);
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
            Share.shareImage(activity, result);
        }
    }

    private class SaveImageTask extends AsyncTask<Bitmap, Void, File> {

        @Override
        protected File doInBackground(Bitmap... params) {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".png");
            try {
                FileOutputStream stream = new FileOutputStream(file);
                params[0].compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.flush();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
        }

        @Override
        protected void onPostExecute(File file) {
            if (file.exists()) {
                MediaScannerConnection.scanFile(KnowledgeApp.context, new String[]{file.getPath()}, null, null);
                Snackbar.make(rootView, getString(R.string.save_img_success)
                        + file.getAbsolutePath(), Snackbar.LENGTH_SHORT).show();

            } else {
                UI.showSnack(rootView, R.string.save_img_failed);
            }
        }
    }

}
