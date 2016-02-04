package com.dante.knowledge.news.view;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

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

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_news_tab;
    }

    @Override
    protected void initViews() {
        NewsTabPagerAdapter adapter = new NewsTabPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ZhihuFragment(), getString(R.string.zhihu_news));
        adapter.addFragment(new FreshFragment(), getString(R.string.fresh_news));
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
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
