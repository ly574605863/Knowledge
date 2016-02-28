package com.dante.knowledge.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.dante.knowledge.MainActivity;
import com.dante.knowledge.R;
import com.dante.knowledge.mvp.interf.NewsPresenter;
import com.dante.knowledge.mvp.interf.NewsView;
import com.dante.knowledge.mvp.interf.OnListFragmentInteract;
import com.dante.knowledge.mvp.model.ZhihuJson;
import com.dante.knowledge.mvp.other.ZhihuListAdapter;
import com.dante.knowledge.mvp.presenter.ZhihuDataPresenter;
import com.dante.knowledge.net.API;
import com.dante.knowledge.utils.Constants;
import com.dante.knowledge.utils.SPUtil;
import com.dante.knowledge.utils.UI;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.ButterKnife;


public class ZhihuFragment extends RecyclerFragment implements NewsView<ZhihuJson>, SwipeRefreshLayout.OnRefreshListener, OnListFragmentInteract {

    private static final int PRELOAD_COUNT = 1;

    private NewsPresenter presenter;
    private ZhihuListAdapter adapter;
    private ConvenientBanner banner;
    private LinearLayoutManager layoutManager;

    @Override
    public void onDestroyView() {
        OkHttpUtils.getInstance().cancelTag(API.TAG_ZHIHU);
        SPUtil.save(type + Constants.POSITION, firstPosition);
        ButterKnife.unbind(this);
        super.onDestroyView();
    }



    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_recycler;
    }

    @Override
    protected void initViews() {
        super.initViews();
        type = TabsFragment.TYPE_ZHIHU;
        Context context = getActivity();
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ZhihuListAdapter(this);
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
        initBanner();
        firstPosition = layoutManager.findFirstVisibleItemPosition();
        lastPosition = layoutManager.findLastVisibleItemPosition();
        if (lastPosition + PRELOAD_COUNT == adapter.getItemCount()) {
            presenter.loadBefore();
        }

    }

    @Override
    protected void initData() {
        presenter = new ZhihuDataPresenter(this, getContext());
        initBanner();
        onRefresh();
    }

    private void initBanner() {
        if (null == banner) {
            if (recyclerView.getChildCount() != 0 && layoutManager.findFirstVisibleItemPosition() == 0) {
                banner = (ConvenientBanner) layoutManager.findViewByPosition(0);
                banner.setScrollDuration(1000);
                banner.startTurning(5000);
            }
        }
    }

    @Override
    public void showProgress() {
        changeProgress(true);
    }

    @Override
    public void addNews(ZhihuJson news) {
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
    public void onRefresh() {
        presenter.loadNews();
    }

    @Override
    public void onListFragmentInteraction(RecyclerView.ViewHolder viewHolder) {

        if (viewHolder instanceof ZhihuListAdapter.ViewHolder) {
            ZhihuListAdapter.ViewHolder holder = (ZhihuListAdapter.ViewHolder) viewHolder;
            Intent intent = new Intent(getActivity(), ZhihuDetailActivity.class);
            intent.putExtra(Constants.ID, holder.zhihuStory.getId());
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                    holder.mImage, getString(R.string.shared_img));
            ActivityCompat.startActivity(getActivity(), intent, optionsCompat.toBundle());

            holder.mTitle.setTextColor(ZhihuListAdapter.textGrey);
        }
    }

}
