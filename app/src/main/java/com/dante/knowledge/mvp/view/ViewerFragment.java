package com.dante.knowledge.mvp.view;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dante.knowledge.R;
import com.dante.knowledge.net.Constants;
import com.dante.knowledge.ui.BaseFragment;

import butterknife.Bind;
import ooo.oxo.library.widget.TouchImageView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by yons on 16/2/18.
 */
public class ViewerFragment extends BaseFragment implements RequestListener<String, GlideDrawable> {

    @Bind(R.id.image)
    TouchImageView imageView;
    private String url;
    private PhotoViewAttacher attacher;

    public static ViewerFragment newInstance(String url) {
        ViewerFragment fragment = new ViewerFragment();
        Bundle args = new Bundle();
        args.putString(Constants.URL, url);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onResume() {
        super.onResume();
        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(this)
                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    }
    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_viewer;
    }

    @Override
    protected void initViews() {
        url=getArguments().getString(Constants.URL);
        imageView.setTransitionName(url);

    }

    @Override
    protected void initData() {

    }


    @Override
    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
        getActivity().supportStartPostponedEnterTransition();
        return true;
    }

    @Override
    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
        imageView.setImageDrawable(resource);
//        attacher = new PhotoViewAttacher(imageView);
//        attacher.setScaleLevels(1f, 1.5f, 2.0f);
        getActivity().supportStartPostponedEnterTransition();
        return true;
    }
}
