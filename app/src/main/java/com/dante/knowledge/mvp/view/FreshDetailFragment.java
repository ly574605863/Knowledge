package com.dante.knowledge.mvp.view;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.dante.knowledge.R;
import com.dante.knowledge.mvp.interf.NewsDetailPresenter;
import com.dante.knowledge.mvp.interf.NewsDetailView;
import com.dante.knowledge.mvp.model.FreshDetailJson;
import com.dante.knowledge.mvp.model.FreshPost;
import com.dante.knowledge.mvp.presenter.FreshDetailPresenter;
import com.dante.knowledge.net.DB;
import com.dante.knowledge.ui.BaseActivity;
import com.dante.knowledge.ui.BaseFragment;
import com.dante.knowledge.utils.Constants;
import com.dante.knowledge.utils.Share;
import com.dante.knowledge.utils.UI;

import java.util.List;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FreshDetailFragment#newInstance} factory method to
 * createAPI an instance of this fragment.
 */
public class FreshDetailFragment extends BaseFragment implements NewsDetailView<FreshDetailJson> {

    private static final String FRESH_ITEM = "fresh_news";
    private static final String FRESH_PREVIOUS_ITEM = "previous_news";

    @Bind(R.id.progress)
    ProgressBar progress;
    @Bind(R.id.web_container)
    FrameLayout webContainer;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    public List<FreshPost> freshPosts;

    private WebView webView;

    private FreshPost freshPost;
    private NewsDetailPresenter<FreshPost> presenter;
    private ShareActionProvider mShareActionProvider;
    private int position;
    private BaseActivity context;

    public FreshDetailFragment() {
    }

    public static FreshDetailFragment newInstance(int position) {
        FreshDetailFragment fragment = new FreshDetailFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (BaseActivity) getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_fresh_detail;
    }

    @Override
    protected void initViews() {
        presenter = new FreshDetailPresenter(this, context);
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            position = getArguments().getInt(Constants.POSITION);
            freshPosts = DB.findAllDateSorted(context.mRealm, FreshPost.class);
            freshPost = freshPosts.get(position);
        }
        presenter.loadNewsDetail(freshPost);
        toolbar.setTitle(freshPost.getTitle());
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

    }

    @Override
    protected void AlwaysInit() {
        super.AlwaysInit();
        initWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        webView = new WebView(getActivity());
        webContainer.addView(webView);
        webView.setVisibility(View.INVISIBLE);
        WebSettings settings = webView.getSettings();
        settings.setTextZoom(110);
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(final WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            view.setVisibility(View.VISIBLE);
                            hideProgress();
                        }
                    }, 200);
                }
            }
        });
    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDetail(FreshDetailJson detailNews) {
        webView.loadDataWithBaseURL("x-data://base", detailNews.getPost().getContent(), "text/html", "UTF-8", null);
    }


    @Override
    public void hideProgress() {
        if (null != progress) {
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void showLoadFailed(String msg) {
        if (rootView != null) {
            UI.showSnackLong(rootView, R.string.load_fail);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    private void setShareIntent() {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(
                    Share.getShareIntent(freshPost.getUrl()));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.share_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        setShareIntent();
    }
}
