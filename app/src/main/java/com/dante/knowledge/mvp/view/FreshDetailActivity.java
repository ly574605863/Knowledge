package com.dante.knowledge.mvp.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.dante.knowledge.R;
import com.dante.knowledge.mvp.model.FreshItem;
import com.dante.knowledge.mvp.model.Image;
import com.dante.knowledge.mvp.other.Data;
import com.dante.knowledge.net.Constants;
import com.dante.knowledge.net.DB;
import com.dante.knowledge.ui.BaseActivity;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class FreshDetailActivity extends BaseActivity {

    @Bind(R.id.pager)
    ViewPager pager;
    public List<FreshItem> freshItems;
    private int position;
    private DetailPagerAdapter adapter;
    private List<Image> images;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_fresh_detail;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initViews() {
        super.initViews();
        String menuType = getIntent().getStringExtra(Constants.MENU_TYPE);
        position = getIntent().getIntExtra(Constants.POSITION, 0);

        List<Fragment> fragments = new ArrayList<>();

        if (MenuTabFragment.MENU_NEWS.equals(menuType)) {
            freshItems = DB.findAllDateSorted(FreshItem.class);
            adapter = new DetailPagerAdapter(getSupportFragmentManager(), fragments, freshItems);

            for (int i = 0; i < freshItems.size(); i++) {
                fragments.add(FreshDetailFragment.newInstance(freshItems.get(i)));
            }
        } else if (MenuTabFragment.MENU_PIC.equals(menuType)) {
            int type = getIntent().getIntExtra(Constants.TYPE, 0);

            images = DB.getImages(type);
            for (int i = 0; i < images.size(); i++) {
                fragments.add(ViewerFragment.newInstance(images.get(i).getUrl()));
            }
            adapter = new DetailPagerAdapter(getSupportFragmentManager(), fragments, images);

        }
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(2);
        pager.setCurrentItem(position);
    }

    private class DetailPagerAdapter<T extends Data> extends FragmentPagerAdapter {

        private List<Fragment> fragments;
        private List<T> items;

        public DetailPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<T> items) {
            super(fm);
            this.fragments = fragments;
            this.items = items;
        }

        public void addFragment(Fragment fragment) {
            fragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
        System.exit(0);
    }
}
