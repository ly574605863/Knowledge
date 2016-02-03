package com.dante.knowledge.news.view;

import android.view.View;
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
import com.dante.knowledge.news.other.FreshListAdapter;
import com.dante.knowledge.news.presenter.FreshDetailPresenter;
import com.dante.knowledge.ui.BaseActivity;

import butterknife.Bind;

public class FreshDetailActivity extends BaseActivity implements NewsDetailView<FreshDetail> {


    @Bind(R.id.web_container)
    FrameLayout webContainer;
    @Bind(R.id.progress)
    ProgressBar progress;
    private WebView webView;
    private FreshItem freshItem;
    private NewsDetailPresenter<FreshItem> presenter;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_fresh_detail;
    }


    @Override
    protected void initViews() {
        super.initViews();
        Object object = getIntent().getSerializableExtra(FreshListAdapter.FRESH_ITEMS);
        freshItem = (FreshItem) object;

        toolbar.setTitle(freshItem.getTitle());
        presenter = new FreshDetailPresenter(this);
        initWebView();
        presenter.loadNewsDetail(freshItem);

    }

    private void initWebView() {
        webView = new WebView(this);
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
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webContainer.removeView(webView);
        webView.removeAllViews();
        webView.destroy();
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


}
