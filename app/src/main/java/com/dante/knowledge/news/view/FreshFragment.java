package com.dante.knowledge.news.view;


import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dante.knowledge.R;
import com.dante.knowledge.net.API;
import com.dante.knowledge.news.interf.NewsPresenter;
import com.dante.knowledge.news.interf.NewsView;
import com.dante.knowledge.news.interf.OnListFragmentInteract;
import com.dante.knowledge.news.model.FreshNews;
import com.dante.knowledge.news.other.FreshListAdapter;
import com.dante.knowledge.news.other.ZhihuListAdapter;
import com.dante.knowledge.news.presenter.FreshNewsPresenter;
import com.dante.knowledge.ui.BaseFragment;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.Bind;


/**
 * A simple {@link Fragment} subclass.
 */
public class FreshFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, NewsView<FreshNews>, OnListFragmentInteract {

    @Bind(R.id.list)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private NewsPresenter presenter;
    private FreshListAdapter adapter;
    private LinearLayoutManager layoutManager;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void onDestroyView() {
        OkHttpUtils.getInstance().cancelTag(API.TAG_FRESH);
        super.onDestroyView();
    }

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_news_list;
    }

    @Override
    protected void initViews() {
        Context context = rootView.getContext();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FreshListAdapter(getActivity(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(mOnScrollListener);

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

                presenter.loadBefore();
            }
        }
    };

    @Override
    protected void initData() {
        presenter = new FreshNewsPresenter(this);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        adapter.clear();
        presenter.loadNews();
    }

    @Override
    public void showProgress() {
        if (null!=swipeRefresh) {
            swipeRefresh.setRefreshing(true);
        }
    }

    @Override
    public void addNews(FreshNews news) {
        adapter.addNews(news);
    }

    @Override
    public void hideProgress() {
        if (null!=swipeRefresh) {
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showLoadFailed(String msg) {
        Snackbar.make(rootView, getString(R.string.load_fail), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onTopLoad() {
        //no banner
    }

    @Override
    public void onListFragmentInteraction(RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof FreshListAdapter.FreshViewHolder) {
            FreshListAdapter.FreshViewHolder holder = (FreshListAdapter.FreshViewHolder) viewHolder;
            holder.mTitle.setTextColor(ZhihuListAdapter.textGrey);
            Intent intent = new Intent(getActivity(), FreshDetailActivity.class);
            intent.putExtra(FreshListAdapter.FRESH_ITEMS, adapter.getFreshItems());
            intent.putExtra(FreshListAdapter.FRESH_ITEM_POSITION, position);
            startActivity(intent);
        }
    }
}
