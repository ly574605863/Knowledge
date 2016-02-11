package com.dante.knowledge.news.view;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.dante.knowledge.MainActivity;
import com.dante.knowledge.R;
import com.dante.knowledge.net.API;
import com.dante.knowledge.news.interf.NewsPresenter;
import com.dante.knowledge.news.interf.NewsView;
import com.dante.knowledge.news.interf.OnListFragmentInteract;
import com.dante.knowledge.news.model.ZhihuNews;
import com.dante.knowledge.news.other.ZhihuListAdapter;
import com.dante.knowledge.news.presenter.ZhihuNewsPresenter;
import com.dante.knowledge.ui.BaseFragment;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.Bind;


public class ZhihuFragment extends BaseFragment implements NewsView<ZhihuNews>, SwipeRefreshLayout.OnRefreshListener, OnListFragmentInteract {
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.list)
    RecyclerView recyclerView;

    private NewsPresenter presenter;
    private ZhihuListAdapter adapter;
    private ConvenientBanner banner;
    private LinearLayoutManager layoutManager;

    @Override
    public void onDestroyView() {
        OkHttpUtils.getInstance().cancelTag(API.TAG_ZHIHU_LATEST);
        super.onDestroyView();
    }

    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    public ZhihuListAdapter getAdapter() {
        return adapter;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_news_list;
    }

    @Override
    protected void initViews() {
        Context context = rootView.getContext();
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ZhihuListAdapter(getActivity(), this);
        recyclerView.setAdapter(adapter);
        //fix recyclerView's bug (crash when scrolling fast after adapter cleared)
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return null != swipeRefresh && swipeRefresh.isRefreshing();
            }
        });
        recyclerView.addOnScrollListener(mOnScrollListener);

        swipeRefresh.setColorSchemeColors(R.color.colorPrimary,
                R.color.colorPrimaryDark, R.color.colorAccent);
        swipeRefresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        presenter = new ZhihuNewsPresenter(this);
        onRefresh();
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
                    && lastVisibleItem + 1 == adapter.getItemCount()
                    && adapter.isHasFooter()) {
                presenter.loadBefore();
            }
        }
    };

    @Override
    public void showProgress() {
        if (null != swipeRefresh&& !swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(true);
        }
    }

    @Override
    public void addNews(ZhihuNews news) {
        adapter.addNews(news);
    }

    @Override
    public void hideProgress() {
        if (null != swipeRefresh) {
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showLoadFailed(String msg) {
        Snackbar.make(((MainActivity) getActivity()).getToolbar(), getString(R.string.load_fail), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        adapter.clear();
        presenter.loadNews();
    }

    @Override
    public void onTopLoad() {
        if (null == banner) {
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    if (recyclerView.getChildCount() != 0) {
                        banner = (ConvenientBanner) layoutManager.findViewByPosition(0);
                        banner.setScrollDuration(1500);
                        banner.startTurning(5000);
                    }
                }
            });
        }

    }

    @Override
    public void onListFragmentInteraction(RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ZhihuListAdapter.ViewHolder) {
            ZhihuListAdapter.ViewHolder holder = (ZhihuListAdapter.ViewHolder) viewHolder;

            holder.mTitle.setTextColor(ZhihuListAdapter.textGrey);

            Intent intent = new Intent(getActivity(), ZhihuDetailActivity.class);
            intent.putExtra(ZhihuListAdapter.ZHIHU_ITEM, holder.zhihuItem);
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                    holder.mImage, getString(R.string.shared_img));
            ActivityCompat.startActivity(getActivity(), intent, optionsCompat.toBundle());
        }
    }

}
