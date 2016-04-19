package com.dante.knowledge.mvp.view;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dante.knowledge.MainActivity;
import com.dante.knowledge.R;
import com.dante.knowledge.mvp.interf.OnListFragmentInteract;
import com.dante.knowledge.mvp.interf.OnLoadDataListener;
import com.dante.knowledge.mvp.interf.UpdateReceiver;
import com.dante.knowledge.mvp.presenter.FetchService;
import com.dante.knowledge.net.API;
import com.dante.knowledge.net.Net;
import com.dante.knowledge.ui.BaseActivity;
import com.dante.knowledge.utils.Constants;
import com.dante.knowledge.utils.SPUtil;
import com.dante.knowledge.utils.UI;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;


/**
 * HFragment for secret mode.
 */
public class HFragment extends RecyclerFragment implements OnListFragmentInteract, OnLoadDataListener {
    private static final int PRELOAD_COUNT = 1;
    private HPostAdapter adapter;
    private LinearLayoutManager layoutManager;
    private UpdateReceiver updateReceiver;
    private LocalBroadcastManager broadcastManager;

    public static final int TYPE_H_BEAUTY = 14;
    public static final int TYPE_H_SELFIE = 15;
    public static final int TYPE_H_EXPOSURE = 16;
    public static final int TYPE_H_ORIGINAL = 49;
    private long GET_DURATION = 3000;
    private String url;
    private int page = 1;

    @Override
    public void onDestroyView() {
        OkHttpUtils.getInstance().cancelTag(API.TAG_H);
        SPUtil.save(type + Constants.PAGE, page);
        broadcastManager.unregisterReceiver(updateReceiver);
        super.onDestroyView();
    }

    @Override
    protected void initViews() {
        super.initViews();
        BaseActivity context = (BaseActivity) getActivity();
        type = getArguments().getInt(Constants.TYPE);
        layoutManager = new LinearLayoutManager(context);
        updateReceiver = new UpdateReceiver(this);
        broadcastManager = LocalBroadcastManager.getInstance(context);
        broadcastManager.registerReceiver(updateReceiver, new IntentFilter(FetchService.ACTION_FETCH));
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HPostAdapter(this, context);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onListScrolled();
                }
            }
        });

    }

    private void onListScrolled() {
        firstPosition = layoutManager.findFirstVisibleItemPosition();
        lastPosition = layoutManager.findLastVisibleItemPosition();

        if (lastPosition + PRELOAD_COUNT == adapter.getItemCount()) {
            showProgress(true);
            loadMore();
        }
    }

    private void loadMore() {
        if (isFirst && page <= 1) {
            page = SPUtil.getInt(type + Constants.PAGE);
        }
        fetch(false);
    }

    @Override
    protected void initData() {
        fetch(true);
    }

    private void fetch(boolean fresh) {
        initUrl(fresh);
        getData();
    }

    private void initUrl(boolean fresh) {
        if (fresh) {
            page = 1;
        }
        url = API.H_MAIN + type + "&page=" + page;
    }

    private void getData() {
        final long lastGetTime = System.currentTimeMillis();
        StringCallback callback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (System.currentTimeMillis() - lastGetTime < GET_DURATION) {
                    Net.get(url, this, API.TAG_H);
                    return;
                }
                onFailure("load failed");
            }

            @Override
            public void onResponse(String response) {
                if (isAlive()){
                    FetchService.startActionFetch(getActivity(), type, response);
                }
            }
        };

        Net.get(url, callback, API.TAG_H);
    }

    @Override
    public void onRefresh() {
        showProgress(true);
        fetch(true);
    }


    public static HFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(Constants.TYPE, type);
        HFragment fragment = new HFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onListFragmentInteraction(RecyclerView.ViewHolder holder) {
        if (holder instanceof HPostAdapter.ViewHolder) {
            HPostAdapter.ViewHolder viewHolder = (HPostAdapter.ViewHolder) holder;
            Intent intent = new Intent(getActivity(), HDetailActivity.class);
            intent.putExtra(Constants.MENU_TYPE, TabsFragment.MENU_H);
            intent.putExtra(Constants.URL, viewHolder.hItem.getUrl());
            intent.putExtra(Constants.TEXT, viewHolder.hItem.getTitle());
            intent.putExtra(Constants.POSITION, holder.getAdapterPosition());
            startActivity(intent);
        }
    }


    @Override
    public void onSuccess() {
        showProgress(false);
        adapter.addNews();
        page++;
    }

    @Override
    public void onFailure(String msg) {
        showProgress(false);
        if (isAlive()) {
            UI.showSnack(((MainActivity) getActivity()).getDrawerLayout(), R.string.load_fail);
        }
    }
}
