package com.dante.knowledge.news.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dante.knowledge.MainActivity;
import com.dante.knowledge.R;

/**
 * Created by yons on 16/2/16.
 */
public class PictureFragment extends RecyclerFragment {

    private LinearLayoutManager layoutManager;

    public static NewsTabFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(MainActivity.TYPE, type);
        NewsTabFragment fragment = new NewsTabFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_recycler;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }

    @Override
    RecyclerView getRecyclerView() {
        return null;
    }
}
