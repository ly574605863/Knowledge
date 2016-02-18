package com.dante.knowledge.mvp.view;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.widget.ImageView;

import com.dante.knowledge.R;
import com.dante.knowledge.net.Constants;
import com.dante.knowledge.ui.BaseFragment;
import com.dante.knowledge.utils.ImageUtil;

import butterknife.Bind;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by yons on 16/2/18.
 */
public class ViewerFragment extends BaseFragment {

    @Bind(R.id.imageView)
    ImageView imageView;
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
        url=getArguments().getString(Constants.URL);
        PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
        ImageUtil.load(getContext(), url, imageView);
    }

    @Override
    protected void initData() {
        ViewCompat.setTransitionName(imageView, getString(R.string.shared_img));
    }


}
