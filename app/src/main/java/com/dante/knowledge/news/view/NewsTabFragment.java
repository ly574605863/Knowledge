package com.dante.knowledge.news.view;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

    private ZhihuFragment zhihuFragment = new ZhihuFragment();
    private FreshFragment freshFragment = new FreshFragment();

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_news_tab;
    }

    @Override
    protected void initViews() {
        NewsTabPagerAdapter adapter = new NewsTabPagerAdapter(getChildFragmentManager());
        adapter.addFragment(zhihuFragment, getString(R.string.zhihu_news));
        adapter.addFragment(freshFragment, getString(R.string.fresh_news));
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
                if (TAG_ZHIHU == tab.getPosition()) {
                    scrollToTop(zhihuFragment.getRecyclerView());

                } else if (TAG_FRESH == tab.getPosition()) {
                    scrollToTop(freshFragment.getRecyclerView());
                }
            }
        });
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
        //after activity created
    }


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
