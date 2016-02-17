package com.dante.knowledge.news.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.dante.knowledge.Images.model.Image;
import com.dante.knowledge.Images.model.PictureParser;
import com.dante.knowledge.MainActivity;
import com.dante.knowledge.R;
import com.dante.knowledge.net.API;
import com.dante.knowledge.net.Constants;
import com.dante.knowledge.net.DB;
import com.dante.knowledge.net.Net;
import com.dante.knowledge.news.interf.OnLoadDataListener;
import com.dante.knowledge.utils.Shared;
import com.dante.knowledge.utils.UiUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import io.realm.RealmResults;
import okhttp3.Call;

/**
 * Gank and DB beauty fragment.
 */
public class PictureFragment extends RecyclerFragment implements OnLoadDataListener<Image> {
    public static final int TYPE_GANK = 0;
    public static final int TYPE_DB_BREAST = 1;
    public static final int TYPE_DB_BUTT = 2;
    public static final int TYPE_DB_SILK = 3;
    public static final int TYPE_DB_LEG = 4;
    private static final int LOAD_COUNT_LARGE = 20;
    private static int LOAD_COUNT = 10;
    private static int PRELOAD_COUNT = 10;

    private String url;
    private int page = 1;
    private int type;
    private StaggeredGridLayoutManager layoutManager;
    private PictureAdapter adapter;
    private RealmResults<Image> images;
    private int lastPosition;

    @Override
    public void onDestroyView() {
        OkHttpUtils.getInstance().cancelTag(API.TAG_PICTURE);
        PictureParser.cancelTask();
        Shared.save(Constants.POSITION + type, layoutManager.findFirstCompletelyVisibleItemPositions(new int[layoutManager.getSpanCount()])[0]);
        super.onDestroyView();
    }

    public static PictureFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(Constants.TYPE, type);
        PictureFragment fragment = new PictureFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_recycler;
    }

    @Override
    protected void initViews() {
        super.initViews();
        Context context = getActivity();
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PictureAdapter(context) {
            @Override
            protected void onItemClick(View v, int position) {
                startViewerActivity(v, position);
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onListScrolled();
                }

            }
        });
        type = getArguments().getInt(Constants.TYPE);
    }

    private void startViewerActivity(View view, int position) {

    }

    private void onListScrolled() {
        lastPosition = layoutManager.findLastVisibleItemPositions(
                new int[layoutManager.getSpanCount()])[1];

        Log.i("test", layoutManager.getItemCount() + ">>>> last:" + lastPosition);
        if (isFirst) {
            if (lastPosition > images.size() / 3) {
                changeProgress(true);
                page++;
                fetch();
                isFirst = false;
            }

        } else if (lastPosition > layoutManager.getItemCount() - PRELOAD_COUNT) {
            PRELOAD_COUNT++;
            changeProgress(true);
            page++;
            fetch();
        }


    }

    private void fetch() {
        switch (type) {
            case TYPE_DB_BREAST:
                url = API.DB_BREAST + page;
                break;
            case TYPE_DB_BUTT:
                url = API.DB_BUTT + page;
                break;
            case TYPE_DB_LEG:
                url = API.DB_LEG + page;
                break;
            case TYPE_DB_SILK:
                url = API.DB_SILK + page;
                break;
            default://type = 0, 代表GANK
                if (!isFirst) {
                    //if not first load, we load more (coz user has images to see)
                    LOAD_COUNT = LOAD_COUNT_LARGE;
                }
                url = API.GANK + LOAD_COUNT + "/" + page;
                break;
        }
        getData();
    }

    private void getData() {
        Net.get(url, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                onFailure("getBoolean data failed", e);
            }

            @Override
            public void onResponse(String response) {
                new PictureParser(type, response, PictureFragment.this).parse();
            }

        }, API.TAG_PICTURE);
    }


    @Override
    protected void initData() {
        images = DB.getImages(type);
        if (images.isEmpty()) {
            swipeRefresh.post(new Runnable() {
                @Override
                public void run() {
                    changeProgress(true);
                }
            });
            fetch();
            return;
        }
        recyclerView.scrollToPosition(Shared.getInt(Constants.POSITION + type));
        adapter.addAll(images);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onRefresh() {
        Log.i("test", "onRefresh");
        fetch();
    }

    @Override
    public void onDataSuccess(Image data) {
        changeProgress(false);
        adapter.replaceWith(images);
    }

    @Override
    public void onFailure(String msg, Exception e) {
        changeProgress(false);
        adapter.replaceWith(images);
        if (isLive()) {
            UiUtils.showSnack(((MainActivity) getActivity()).getDrawerLayout(), R.string.load_fail);
        }
    }


}
