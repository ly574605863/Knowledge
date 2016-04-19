package com.dante.knowledge.mvp.view;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.dante.knowledge.R;
import com.dante.knowledge.mvp.interf.OnLoadDataListener;
import com.dante.knowledge.mvp.interf.UpdateReceiver;
import com.dante.knowledge.mvp.model.HDetail;
import com.dante.knowledge.mvp.model.Image;
import com.dante.knowledge.mvp.other.PictureAdapter;
import com.dante.knowledge.mvp.presenter.FetchService;
import com.dante.knowledge.net.DB;
import com.dante.knowledge.ui.BaseActivity;
import com.dante.knowledge.utils.Constants;

import io.realm.RealmList;

/**
 * Created by yons on 16/3/2.
 */
public class HDetailFragment extends RecyclerFragment implements OnLoadDataListener {
    public static final int TYPE_GANK = 0;
    public static final int TYPE_DB_BREAST = 1;
    public static final int TYPE_DB_BUTT = 2;
    public static final int TYPE_DB_SILK = 3;
    public static final int TYPE_DB_LEG = 4;
    public static final int TYPE_DB_RANK = 5;

    private static final int LOAD_COUNT_LARGE = 15;
    private static int LOAD_COUNT = 10;
    private static int PRELOAD_COUNT = 10;

    private String url;
    private StaggeredGridLayoutManager layoutManager;
    private PictureAdapter adapter;
    private HDetail detail;
    private RealmList<Image> images;
    private long GET_DURATION = 3000;
    private UpdateReceiver updateReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private BaseActivity context;

    @Override
    public void onPause() {
        firstPosition = layoutManager.findFirstVisibleItemPositions(new int[layoutManager.getSpanCount()])[0];
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        localBroadcastManager.unregisterReceiver(updateReceiver);
        super.onDestroyView();
    }

    public static HDetailFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(Constants.URL, url);
        HDetailFragment fragment = new HDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViews() {
        super.initViews();
        updateReceiver = new UpdateReceiver(this);
        context =(BaseActivity)getActivity();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.registerReceiver(updateReceiver, new IntentFilter(FetchService.ACTION_FETCH));
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PictureAdapter(context) {
            @Override
            protected void onItemClick(View v, int position) {
                startViewer(v, position);
            }
        };
        recyclerView.setAdapter(adapter);
        url = getArguments().getString(Constants.URL);

    }

    private void startViewer(View view, int position) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(Constants.MENU_TYPE, TabsFragment.MENU_H);
        intent.putExtra(Constants.URL, url);
        intent.putExtra(Constants.POSITION, position);

        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(context, view, adapter.get(position).getUrl());
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }

    private void fetch() {
        FetchService.startFetchHDetail(getContext(), url);
    }


    @Override
    protected void initData() {
        detail = DB.getByUrl(context.mRealm, url, HDetail.class);
        if (detail == null) {
            showProgress(true);
            fetch();
        } else {
            images = detail.getImages();
        }
    }

    @Override
    public void onRefresh() {
        fetch();
    }

    @Override
    public void onSuccess() {
        showProgress(false);
//        adapter.replaceWith(images);
    }

    @Override
    public void onFailure(String msg) {
        showProgress(false);
        if (isAlive()) {
            Snackbar.make(rootView, getString(R.string.load_no_result), Snackbar.LENGTH_LONG)
                    .setAction(R.string.try_again, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fetch();
                        }
                    }).show();
        }
    }


}