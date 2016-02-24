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
import com.dante.knowledge.mvp.model.FreshPost;
import com.dante.knowledge.mvp.model.Image;
import com.dante.knowledge.net.DB;
import com.dante.knowledge.ui.BaseActivity;
import com.dante.knowledge.utils.Constants;
import com.dante.knowledge.utils.SPUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import io.realm.RealmChangeListener;
import ooo.oxo.library.widget.PullBackLayout;

public class DetailActivity extends BaseActivity implements PullBackLayout.Callback, RealmChangeListener {

    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.container)
    FrameLayout container;
    private int position;
    private DetailPagerAdapter adapter;
    private List<Image> images;
    private String menuType;
    private boolean isPicture;
    private int currentPosition;
    private int type;


    @Override
    protected void initLayoutId() {
        menuType = getIntent().getStringExtra(Constants.MENU_TYPE);
        layoutId = R.layout.activity_detail;
        if (TabsFragment.MENU_PIC.equals(menuType)) {
            isPicture = true;
            layoutId = R.layout.activity_detail_pulldown;
            setTheme(R.style.ViewerTheme_TransNav);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initViews() {
        supportPostponeEnterTransition();
        super.initViews();
        position = getIntent().getIntExtra(Constants.POSITION, 0);

        List<Fragment> fragments = new ArrayList<>();

        if (TabsFragment.MENU_NEWS.equals(menuType)) {
            adapter = new DetailPagerAdapter(getSupportFragmentManager(), fragments, DB.findAll(FreshPost.class).size());

            for (int i = 0; i < DB.findAll(FreshPost.class).size(); i++) {
                fragments.add(FreshDetailFragment.newInstance(i));
            }
        } else if (isPicture) {
            ((PullBackLayout) container).setCallback(this);
            type = getIntent().getIntExtra(Constants.TYPE, 0);
            images = DB.getImages(type);
            for (int i = 0; i < images.size(); i++) {
                fragments.add(ViewerFragment.newInstance(images.get(i).getUrl()));
            }
            adapter = new DetailPagerAdapter(getSupportFragmentManager(), fragments,images.size());

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
                currentPosition=position;
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
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
            }

            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                Log.i("test", "pager position " + position);
                SPUtil.save("shared_index", position);
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
        getWindow().getDecorView().getBackground().setAlpha(0xff - (int) Math.floor(0xff * v));

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
        super.supportFinishAfterTransition();
    }

    @Override
    public void onChange() {
        //May fix one PagerAdapter bug as following:
        //      ---The application's PagerAdapter changed the adapter's contents
        //      ---without calling PagerAdapter#notifyDataSetChanged!]
        adapter.notifyDataSetChanged();
    }

    private class DetailPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;
        private int size;

        public DetailPagerAdapter(FragmentManager fm, List<Fragment> fragments, int dataSize) {
            super(fm);
            this.fragments = fragments;
            this.size = dataSize;
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
            return size;
        }

        public ViewerFragment getCurrent(int position) {
            return (ViewerFragment) adapter.instantiateItem(pager, position);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SPUtil.save(type + Constants.POSITION, currentPosition);
    }

    @Override
    protected void onDestroy() {
        OkHttpUtils.getInstance().cancelTag(this);
        super.onDestroy();
//        System.exit(0);
    }


}
