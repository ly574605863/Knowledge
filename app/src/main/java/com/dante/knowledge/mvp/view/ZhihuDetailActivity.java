package com.dante.knowledge.mvp.view;

import android.annotation.SuppressLint;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.dante.knowledge.R;
import com.dante.knowledge.mvp.interf.NewsDetailPresenter;
import com.dante.knowledge.mvp.interf.NewsDetailView;
import com.dante.knowledge.mvp.model.ZhihuDetail;
import com.dante.knowledge.mvp.model.ZhihuStory;
import com.dante.knowledge.mvp.model.ZhihuTop;
import com.dante.knowledge.mvp.presenter.ZhihuDetailPresenter;
import com.dante.knowledge.net.DB;
import com.dante.knowledge.ui.BaseActivity;
import com.dante.knowledge.utils.Constants;
import com.dante.knowledge.utils.Imager;
import com.dante.knowledge.utils.Share;
import com.dante.knowledge.utils.UI;

import butterknife.Bind;

public class ZhihuDetailActivity extends BaseActivity implements NewsDetailView<ZhihuDetail> {
    @Bind(R.id.detail_img)
    ImageView detailImg;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.progress)
    ProgressBar progress;
    @Bind(R.id.web_container)
    FrameLayout webContainer;
    private WebView webView;
    private ZhihuDetail zhihuDetail;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_news_detail;
    }

    @Override
    protected void initViews() {
        super.initViews();
        int id = getIntent().getIntExtra(Constants.ID, 0);
        ZhihuStory story = DB.getById(mRealm, id, ZhihuStory.class);
        zhihuDetail = DB.getById(mRealm, id, ZhihuDetail.class);
        if (story == null) {
            //can't find zhihuItem, so this id is passed by Zhihutop
            toolbarLayout.setTitle(DB.getById(mRealm, id, ZhihuTop.class).getTitle());
        } else {
            toolbarLayout.setTitle(story.getTitle());
        }
        NewsDetailPresenter<ZhihuStory> presenter = new ZhihuDetailPresenter(this, this);
        initWebView();
        if (zhihuDetail == null) {
            presenter.loadNewsDetail(story);
        } else {
            showDetail(zhihuDetail);
        }
    }

    private void initFAB() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share.shareText(ZhihuDetailActivity.this, zhihuDetail.getShare_url());
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        webView = new WebView(this);
        webContainer.addView(webView);
        webView.setVisibility(View.INVISIBLE);
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
    public void onBackPressed() {
        webView.setVisibility(View.INVISIBLE);
        super.onBackPressed();
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
        System.exit(0);
    }


    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDetail(ZhihuDetail detailNews) {
        zhihuDetail = detailNews;
        Imager.load(this, detailNews.getImage(), detailImg);
        //add css style to webView
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + detailNews.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        webView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
        initFAB();
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void showLoadFailed(String msg) {
        UI.showSnackLong(webContainer, R.string.load_fail);

    }

}
