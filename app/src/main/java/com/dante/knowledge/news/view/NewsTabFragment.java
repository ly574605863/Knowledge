package com.dante.knowledge.news.view;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dante.knowledge.R;
import com.dante.knowledge.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A fragment {@link Fragment} contains different news fragment.
 */
public class NewsTabFragment extends BaseFragment {


    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.tabs)
    TabLayout tabs;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_news_tab;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {
        NewsTabPagerAdapter adapter = new NewsTabPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new NewsFragment(),"知乎日報");
        adapter.addFragment(new FreshFragment(),"新鮮事");
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
    }

//    private void scrollToTop() {
//        if (null != recyclerView) {
//            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
//            if (manager.findLastVisibleItemPosition() < 35) {
//                recyclerView.smoothScrollToPosition(0);
//
//            } else {
//                recyclerView.scrollToPosition(0);
//            }
//        }
//    }
//
    public static class NewsTabPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> titles = new ArrayList<>();

        public NewsTabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
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
