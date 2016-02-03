package com.dante.knowledge.news.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.dante.knowledge.R;
import com.dante.knowledge.news.model.FreshItem;
import com.dante.knowledge.news.other.FreshListAdapter;
import com.dante.knowledge.ui.BaseActivity;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FreshDetailActivity extends BaseActivity {

    @Bind(R.id.pager)
    ViewPager pager;
    public ArrayList<FreshItem> freshItems;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_fresh_detail;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initViews() {
        super.initViews();
        freshItems = (ArrayList<FreshItem>) getIntent().getSerializableExtra(FreshListAdapter.FRESH_ITEMS);
        int position = getIntent().getIntExtra(FreshListAdapter.FRESH_ITEM_POSITION, 0);
        pager.setAdapter(new FreshDetailPagerAdapter(getSupportFragmentManager()));
        pager.setOffscreenPageLimit(2);
        pager.setCurrentItem(position);
    }

    private class FreshDetailPagerAdapter extends FragmentPagerAdapter {

        public FreshDetailPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FreshDetailFragment.newInstance(freshItems.get(position));
        }

        @Override
        public int getCount() {
            return freshItems.size();
        }
    }


}
