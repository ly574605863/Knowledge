package com.dante.knowledge.news.view;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.dante.knowledge.R;
import com.dante.knowledge.news.interf.NewsDetailPresenter;
import com.dante.knowledge.news.interf.NewsDetailView;
import com.dante.knowledge.news.model.FreshDetail;
import com.dante.knowledge.news.model.FreshItem;
import com.dante.knowledge.news.presenter.FreshDetailPresenter;
import com.dante.knowledge.ui.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FreshDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FreshDetailFragment extends BaseFragment implements NewsDetailView<FreshDetail> {

    private static final String FRESH_ITEM = "fresh_news";

    @Bind(R.id.progress)
    ProgressBar progress;
    @Bind(R.id.web_container)
    FrameLayout webContainer;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private WebView webView;

    private FreshItem freshItem;
    private NewsDetailPresenter<FreshItem> presenter;

    public FreshDetailFragment() {
    }

    public static FreshDetailFragment newInstance(FreshItem freshItem) {
        FreshDetailFragment fragment = new FreshDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(FRESH_ITEM, freshItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            freshItem = (FreshItem) getArguments().getSerializable(FRESH_ITEM);
        }
    }

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_fresh_detail;
    }

    @Override
    protected void initViews() {
        presenter = new FreshDetailPresenter(this);
        presenter.loadNewsDetail(freshItem);
    }

    @Override
    protected void initData() {
        toolbar.setTitle(freshItem.getTitle());
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        WebSettings settings = webView.getSettings();
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
                    }, 300);
                }
            }
        });
    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDetail(FreshDetail detailNews) {
//        webView.loadData(detailNews.getPost().getContent(), "text/html", "UTF-8");
        webView.loadDataWithBaseURL("x-data://base", detailNews.getPost().getContent(), "text/html", "UTF-8", null);

    }


    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void showLoadFailed(String msg) {

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

    @Override
    public void onDestroyView() {
        webContainer.removeView(webView);
        webView.removeAllViews();
        webView.destroy();
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
