package com.dante.knowledge.news.view;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.dante.knowledge.R;
import com.dante.knowledge.news.interf.NewsView;
import com.dante.knowledge.news.model.ZhihuNews;
import com.dante.knowledge.news.model.ZhihuItem;
import com.dante.knowledge.news.other.ZhihuListAdapter;
import com.dante.knowledge.news.presenter.ZhihuNewsPresenter;
import com.dante.knowledge.news.interf.NewsPresenter;
import com.dante.knowledge.news.interf.OnListFragmentInteractionListener;
import com.dante.knowledge.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ZhihuFragment extends BaseFragment implements NewsView<ZhihuNews>, SwipeRefreshLayout.OnRefreshListener, OnListFragmentInteractionListener {
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.list)
    RecyclerView mRecyclerView;

    private NewsPresenter mNewsPresenter;
    private List<ZhihuItem> storyEntities;
    private ZhihuListAdapter adapter;
    private ConvenientBanner banner;
    private LinearLayoutManager layoutManager;

    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    public ZhihuListAdapter getAdapter() {
        return adapter;
    }

    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }

    @Override
    protected void initData() {
        mNewsPresenter = new ZhihuNewsPresenter(this);
        onRefresh();
    }

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
        storyEntities = new ArrayList<>();
        adapter = new ZhihuListAdapter(getActivity(), this);
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
                    && lastVisibleItem + 1 == adapter.getItemCount()
                    && adapter.isHasFooter()) {

                mNewsPresenter.loadBefore();
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showProgress() {
        if (!swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(true);
        }
    }

    @Override
    public void addNews(ZhihuNews news) {
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
    public void onRefresh() {
        adapter.clear();
        adapter.setShowHeader(false);
        mNewsPresenter.loadNews();
    }

    @Override
    public void onTopLoad() {

        if (banner != null) {
            return;
        }
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (mRecyclerView.getChildCount() != 0) {
                    banner = (ConvenientBanner) layoutManager.findViewByPosition(0);
                    banner.setScrollDuration(1500);
                    banner.startTurning(5000);
                }
            }
        });
    }

    @Override
    public void onListFragmentInteraction(RecyclerView.ViewHolder viewHolder, int position) {
//        int textGrey = ContextCompat.getColor(getContext(), R.color.darker_gray);

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
