package com.dante.knowledge.mvp.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.dante.knowledge.R;
import com.dante.knowledge.mvp.model.FreshItem;
import com.dante.knowledge.mvp.model.Image;
import com.dante.knowledge.mvp.other.Data;
import com.dante.knowledge.utils.Constants;
import com.dante.knowledge.net.DB;
import com.dante.knowledge.ui.BaseActivity;
import com.dante.knowledge.utils.SP;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import ooo.oxo.library.widget.PullBackLayout;

public class DetailActivity extends BaseActivity implements PullBackLayout.Callback {

    @Bind(R.id.pager)
    ViewPager pager;
    public List<FreshItem> freshItems;
    @Bind(R.id.container)
    FrameLayout container;
    private int position;
    private DetailPagerAdapter adapter;
    private List<Image> images;
    private String menuType;
    private boolean isPicture;

    @Override
    protected void initLayoutId() {
        menuType = getIntent().getStringExtra(Constants.MENU_TYPE);
        layoutId = R.layout.activity_detail;
        if (MenuTabFragment.MENU_PIC.equals(menuType)) {
            isPicture = true;
            layoutId = R.layout.activity_detail_pulldown;
            setTheme(R.style.AppTheme_NoActionBar_TransNav);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initViews() {
        supportPostponeEnterTransition();
        super.initViews();
        position = getIntent().getIntExtra(Constants.POSITION, 0);

        List<Fragment> fragments = new ArrayList<>();

        if (MenuTabFragment.MENU_NEWS.equals(menuType)) {
            freshItems = DB.findAllDateSorted(FreshItem.class);
            adapter = new DetailPagerAdapter(getSupportFragmentManager(), fragments, freshItems);

            for (int i = 0; i < freshItems.size(); i++) {
                fragments.add(FreshDetailFragment.newInstance(freshItems.get(i)));
            }
        } else if (isPicture) {
            ((PullBackLayout) container).setCallback(this);
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
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setEnterSharedElement(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setEnterSharedElement(final int position) {
        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                Log.i("test", "end");
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
            }

            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                Log.i("test", "pager position " + position);
                SP.save("shared_index", position);
                names.clear();
                names.add(images.get(position).getUrl());
                super.onMapSharedElements(names, sharedElements);
            }
        });
    }

    @Override
    public void onPullStart() {

    }

    @Override
    public void onPull(float v) {

    }

    @Override
    public void onPullCancel() {

    }

    @Override
    public void onPullComplete() {
        supportFinishAfterTransition();
    }

    @Override
    public void supportFinishAfterTransition() {
//        getWindow().setExitTransition(null);
        super.supportFinishAfterTransition();
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

        public ViewerFragment getCurrent(int position) {
            return (ViewerFragment) adapter.instantiateItem(pager, position);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
//        System.exit(0);
    }


}
