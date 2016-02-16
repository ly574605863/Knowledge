package com.dante.knowledge.news.view;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dante.knowledge.MainActivity;
import com.dante.knowledge.R;
import com.dante.knowledge.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * A tab fragment {@link Fragment} contains different news fragment.
 */
public class NewsTabFragment extends BaseFragment {

    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.tabs)
    TabLayout tabs;
    public static final int TAG_ZHIHU = 0;
    public static final int TAG_FRESH = 1;

    public static final String TYPE_NEWS = "news";
    public static final String TYPE_PIC = "pic";

    private List<RecyclerFragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private NewsTabPagerAdapter adapter;
    private String type;

    public static NewsTabFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(MainActivity.TYPE, type);
        NewsTabFragment fragment = new NewsTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_news_tab;
    }

    @Override
    protected void initViews() {
        adapter = new NewsTabPagerAdapter(getChildFragmentManager());
        initFragments();
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                scrollToTop(fragments.get(tab.getPosition()).getRecyclerView());
            }
        });
    }

    private void initFragments() {
        type = getArguments().getString(MainActivity.TYPE);

        if (TYPE_PIC.equals(type)) {
            fragments.add(new PictureFragment());
            // TODO: 16/2/16
        } else {
            fragments.add(new ZhihuFragment());
            fragments.add(new FreshFragment());
            titles.add(getString(R.string.zhihu_news));
            titles.add(getString(R.string.fresh_news));
        }

        adapter.setFragments(fragments, titles);
    }

    private void scrollToTop(RecyclerView list) {
        if (null != list) {
            LinearLayoutManager manager = (LinearLayoutManager) list.getLayoutManager();
            if (manager.findLastVisibleItemPosition() < 50) {
                list.smoothScrollToPosition(0);
            } else {
                list.scrollToPosition(0);
            }
        }
    }

    @Override
    protected void initData() {

    }


    public static class NewsTabPagerAdapter extends FragmentPagerAdapter {

        private List<RecyclerFragment> fragments;
        private List<String> titles;

        public NewsTabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setFragments(List<RecyclerFragment> fragments, List<String> titles) {
            this.fragments = fragments;
            this.titles = titles;
        }

        public void addFragment(RecyclerFragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}
