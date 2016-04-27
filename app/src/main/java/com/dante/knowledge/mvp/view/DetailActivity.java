package com.dante.knowledge.mvp.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.dante.knowledge.R;
import com.dante.knowledge.mvp.model.FreshPost;
import com.dante.knowledge.mvp.model.HDetail;
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

@TargetApi(Build.VERSION_CODES.KITKAT)
public class DetailActivity extends BaseActivity implements PullBackLayout.Callback, RealmChangeListener {

    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.container)
    FrameLayout container;
    private DetailPagerAdapter adapter;
    private String menuType;
    private boolean isPicture;
    private int currentPosition;
    private int type;
    private List<Image> images;

    private static final int SYSTEM_UI_SHOW = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

    private static final int SYSTEM_UI_HIDE = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;

    private boolean isSystemUiShown = true;

    @Override
    protected void onPause() {
        super.onPause();
        SPUtil.save(type + Constants.POSITION, currentPosition);
    }


    @Override
    protected void onDestroy() {
        OkHttpUtils.getInstance().cancelTag(this);
        super.onDestroy();
    }

    @Override
    protected void initLayoutId() {
        menuType = getIntent().getStringExtra(Constants.MENU_TYPE);
        layoutId = R.layout.activity_detail;
        if (TabsFragment.MENU_PIC.equals(menuType) || TabsFragment.MENU_H.equals(menuType)) {
            isPicture = true;
            layoutId = R.layout.activity_detail_pulldown;
            setTheme(R.style.ViewerTheme_TransNav);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initViews() {
        super.initViews();
        supportPostponeEnterTransition();
        int position = getIntent().getIntExtra(Constants.POSITION, 0);
        currentPosition = position;
        List<Fragment> fragments = new ArrayList<>();

        if (TabsFragment.MENU_NEWS.equals(menuType)) {
            List<FreshPost> freshPosts = DB.findAllDateSorted(mRealm, FreshPost.class);
            adapter = new DetailPagerAdapter(getSupportFragmentManager(), fragments, freshPosts.size());

            for (int i = 0; i < DB.findAll(mRealm, FreshPost.class).size(); i++) {
                fragments.add(FreshDetailFragment.newInstance(i));
            }

        } else if (isPicture) {
            ((PullBackLayout) container).setCallback(this);

            if (TabsFragment.MENU_H.equals(menuType)) {
                String url = getIntent().getStringExtra(Constants.URL);
                images = DB.getByUrl(mRealm,url, HDetail.class).getImages();
            } else if (TabsFragment.MENU_PIC.equals(menuType)) {
                type = getIntent().getIntExtra(Constants.TYPE, 0);
                images = DB.getImages(mRealm, type);
            }

            for (int i = 0; i < images.size(); i++) {
                fragments.add(ViewerFragment.newInstance(images.get(i).getUrl()));
            }
            adapter = new DetailPagerAdapter(getSupportFragmentManager(), fragments, images.size());
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
                currentPosition = position;
                setEnterSharedElement(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (isPicture) {
                    hideSystemUi();
                }
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

    public String currentUrl() {
        return images.get(currentPosition).getUrl();
    }

    private class DetailPagerAdapter extends FragmentStatePagerAdapter {

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

    public void toggleSystemUI() {
        if (isSystemUiShown) {
            hideSystemUi();
        } else {
            showSystemUi();
        }
    }

    public void showSystemUi() {
        pager.setSystemUiVisibility(SYSTEM_UI_SHOW);
        isSystemUiShown = true;
    }

    public void hideSystemUi() {
        pager.setSystemUiVisibility(SYSTEM_UI_HIDE);
        isSystemUiShown = false;
    }

//    public void setShareIntent(String data) {
//        if (mShareActionProvider != null) {
//            mShareActionProvider.setShareIntent(
//                    Share.getShareIntent(data)
//            );
//        }
//    }
//
//    public void setShareImageIntent(Uri uri) {
//        if (mShareActionProvider != null) {
//            mShareActionProvider.setShareIntent(
//                    Share.getShareImageIntent(uri)
//            );
//        }
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.share_menu, menu);
//        MenuItem item = menu.findItem(R.id.menu_item_share);
//        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
//        if (!isPicture) {
//            setShareIntent(freshPosts.get(position).getUrl());
//        }
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
