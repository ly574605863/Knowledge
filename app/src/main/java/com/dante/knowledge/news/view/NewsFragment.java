package com.dante.knowledge.news.view;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.dante.knowledge.KnowledgeApplication;
import com.dante.knowledge.R;
import com.dante.knowledge.news.model.LatestNews;
import com.dante.knowledge.news.model.StoryEntity;
import com.dante.knowledge.news.other.NewsListAdapter;
import com.dante.knowledge.news.presenter.NewsPresenterImpl;
import com.dante.knowledge.news.presenter.NewsPresenter;
import com.dante.knowledge.news.other.OnListFragmentInteractionListener;
import com.dante.knowledge.ui.BaseFragment;
import com.dante.knowledge.utils.Tool;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class NewsFragment extends BaseFragment implements NewsView, SwipeRefreshLayout.OnRefreshListener, OnListFragmentInteractionListener {
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.list)
    RecyclerView mRecyclerView;

    private NewsPresenter mNewsPresenter;
    private List<StoryEntity> storyEntities;
    private NewsListAdapter adapter;
    private ConvenientBanner banner;
    private LinearLayoutManager layoutManager;

    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    public NewsListAdapter getAdapter() {
        return adapter;
    }

    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }

    @Override
    protected void initData() {
        mNewsPresenter = new NewsPresenterImpl(this);
        GetData();
    }

    private void GetData() {
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
        adapter = new NewsListAdapter(this);
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
                    && lastVisibleItem +1== adapter.getItemCount()
                    && adapter.isHasFooter()) {

                mNewsPresenter.loadBefore();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher watcher = KnowledgeApplication.getRefWatcher(getActivity());
        watcher.watch(this);
//        Tool.removeActivityFromTransitionManager(getActivity());解决内存泄露

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showProgress() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void addNews(LatestNews news) {
        adapter.addNews(news);

    }

    @Override
    public void hideProgress() {
        swipeRefresh.setRefreshing(false);
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
        Logger.init("test");
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
    public void onListFragmentInteraction(RecyclerView.ViewHolder viewHolder) {
        int grey = ContextCompat.getColor(getContext(), R.color.darker_gray);
        if (viewHolder instanceof NewsListAdapter.ViewHolder) {
            NewsListAdapter.ViewHolder holder = (NewsListAdapter.ViewHolder) viewHolder;
            holder.mTitle.setTextColor(grey);
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            intent.putExtra(NewsListAdapter.STORY, holder.story);
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(),
                            Pair.create((View) holder.mImage, getString(R.string.shared_img))
                    );
            ActivityCompat.startActivity(getActivity(), intent, optionsCompat.toBundle());
        }
    }

}
