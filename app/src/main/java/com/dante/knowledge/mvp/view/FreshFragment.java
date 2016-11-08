package com.dante.knowledge.mvp.view;


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
import com.dante.knowledge.ui.BaseActivity;
import com.dante.knowledge.utils.Constants;
import com.dante.knowledge.utils.SPUtil;
import com.dante.knowledge.utils.UI;


/**
 * A simple {@link Fragment} subclass.
 */
public class FreshFragment extends RecyclerFragment implements SwipeRefreshLayout.OnRefreshListener, NewsView<FreshJson>, OnListFragmentInteract {
    private static final String TAG = "test";
    private static final int PRELOAD_COUNT = 1;
    private NewsPresenter presenter;
    private NewsListAdapter adapter;
    private LinearLayoutManager layoutManager;
    private BaseActivity mActivity;

    @Override
    public void onDestroyView() {
        SPUtil.save(type + Constants.POSITION, firstPosition);
        super.onDestroyView();
    }

    @Override
    protected void initViews() {
        super.initViews();
        mActivity = (BaseActivity) getActivity();
        type = TabsFragment.TYPE_FRESH;
        layoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsListAdapter(this, mActivity);
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
        presenter = new FreshDataPresenter(this, mActivity);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        presenter.loadNews();
    }

    @Override
    public void showProgress() {
        showProgress(true);
    }

    @Override
    public void addNews(FreshJson news) {
        adapter.addNews();
    }

    @Override
    public void hideProgress() {
        showProgress(false);
    }

    @Override
    public void loadFailed(String msg) {
        if (isAlive()) {
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
