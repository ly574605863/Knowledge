package com.dante.knowledge.news.view;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dante.knowledge.R;
import com.dante.knowledge.news.other.NewsListAdapter;
import com.dante.knowledge.news.interf.NewsPresenter;
import com.dante.knowledge.ui.BaseFragment;

import butterknife.Bind;


/**
 * A simple {@link Fragment} subclass.
 */
public class FreshFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.list)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private NewsPresenter mNewsPresenter;
    private NewsListAdapter adapter;
    private LinearLayoutManager layoutManager;
    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_news_list;
    }

    @Override
    protected void initViews() {
        Context context = rootView.getContext();
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
//        adapter = new NewsListAdapter(this);
        mRecyclerView.setAdapter(adapter);
//        mRecyclerView.addOnScrollListener(mOnScrollListener);
        swipeRefresh.setColorSchemeColors(R.color.colorPrimary,
                R.color.colorPrimaryDark, R.color.colorAccent);
        swipeRefresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onRefresh() {

    }
}
