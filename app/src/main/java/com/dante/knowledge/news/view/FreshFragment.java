package com.dante.knowledge.news.view;


import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dante.knowledge.R;
import com.dante.knowledge.news.interf.NewsView;
import com.dante.knowledge.news.interf.OnListFragmentInteractionListener;
import com.dante.knowledge.news.model.FreshNews;
import com.dante.knowledge.news.other.FreshListAdapter;
import com.dante.knowledge.news.interf.NewsPresenter;
import com.dante.knowledge.news.presenter.FreshNewsPresenter;
import com.dante.knowledge.ui.BaseFragment;

import butterknife.Bind;


/**
 * A simple {@link Fragment} subclass.
 */
public class FreshFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, NewsView<FreshNews>, OnListFragmentInteractionListener {


    @Bind(R.id.list)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private NewsPresenter mNewsPresenter;
    private FreshListAdapter adapter;
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
        adapter = new FreshListAdapter(getActivity(), this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        swipeRefresh.setColorSchemeColors(R.color.colorPrimary,
                R.color.colorPrimaryDark, R.color.colorAccent);
        swipeRefresh.setOnRefreshListener(this);
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == adapter.getItemCount()) {

                mNewsPresenter.loadBefore();
            }
        }
    };

    @Override
    protected void initData() {
        mNewsPresenter = new FreshNewsPresenter(this);
        onRefresh();
    }


    @Override
    public void onRefresh() {
        adapter.clear();
        mNewsPresenter.loadNews();
    }

    @Override
    public void showProgress() {
        if (!swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(true);
        }
    }

    @Override
    public void addNews(FreshNews news) {
        adapter.addNews(news);
    }

    @Override
    public void hideProgress() {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showLoadFailed(String msg) {
        Snackbar.make(rootView, getString(R.string.load_fail), Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void onTopLoad() {

    }

    @Override
    public void onListFragmentInteraction(RecyclerView.ViewHolder viewHolder) {
        int grey = ContextCompat.getColor(getContext(), R.color.darker_gray);

        if (viewHolder instanceof FreshListAdapter.FreshViewHolder) {
            FreshListAdapter.FreshViewHolder holder = (FreshListAdapter.FreshViewHolder) viewHolder;
            holder.mTitle.setTextColor(grey);
            Intent intent = new Intent(getActivity(), FreshDetailActivity.class);
            intent.putExtra(FreshListAdapter.FRESH_ITEMS, holder.freshItem);
            intent.putExtra(FreshListAdapter.FRESH_ITEM_POSITION, holder.freshItem);
            startActivity(intent);
        }
    }
}
