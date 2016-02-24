package com.dante.knowledge.mvp.view;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dante.knowledge.MainActivity;
import com.dante.knowledge.R;
import com.dante.knowledge.mvp.interf.NewsPresenter;
import com.dante.knowledge.mvp.interf.NewsView;
import com.dante.knowledge.mvp.interf.OnListFragmentInteract;
import com.dante.knowledge.mvp.model.FreshJson;
import com.dante.knowledge.mvp.other.NewsListAdapter;
import com.dante.knowledge.mvp.other.ZhihuListAdapter;
import com.dante.knowledge.mvp.presenter.FreshDataPresenter;
import com.dante.knowledge.net.API;
import com.dante.knowledge.utils.Constants;
import com.dante.knowledge.utils.SPUtil;
import com.dante.knowledge.utils.UI;
import com.zhy.http.okhttp.OkHttpUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class FreshFragment extends RecyclerFragment implements SwipeRefreshLayout.OnRefreshListener, NewsView<FreshJson>, OnListFragmentInteract {

    private static final int PRELOAD_COUNT = 1;
    private NewsPresenter presenter;
    private NewsListAdapter adapter;
    private LinearLayoutManager layoutManager;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }


    @Override
    public void onDestroyView() {
        OkHttpUtils.getInstance().cancelTag(API.TAG_FRESH);
        SPUtil.save(type + Constants.POSITION, firstPosition);
        super.onDestroyView();
    }

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_recycler;
    }

    @Override
    protected void initViews() {
        super.initViews();
        Context context = getActivity();
        type = TabsFragment.TYPE_FRESH;
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsListAdapter( this);
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
            presenter.loadBefore();
        }
    }

    @Override
    protected void initData() {
        presenter = new FreshDataPresenter(this);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        presenter.loadNews();
    }

    @Override
    public void showProgress() {
        changeProgress(true);
    }

    @Override
    public void addNews(FreshJson news) {
        adapter.addNews(news);
    }

    @Override
    public void hideProgress() {
        changeProgress(false);
    }

    @Override
    public void loadFailed(String msg) {
        if (isLive()) {
            UI.showSnack(((MainActivity) getActivity()).getDrawerLayout(), R.string.load_fail);
        }
    }

    @Override
    public void onListFragmentInteraction(RecyclerView.ViewHolder viewHolder) {

        if (viewHolder instanceof NewsListAdapter.ViewHolder) {
            NewsListAdapter.ViewHolder holder = (NewsListAdapter.ViewHolder) viewHolder;
            holder.mTitle.setTextColor(ZhihuListAdapter.textGrey);
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(Constants.MENU_TYPE, TabsFragment.MENU_NEWS);
            intent.putExtra(Constants.POSITION, holder.getAdapterPosition());
            startActivity(intent);
        }
    }
}
