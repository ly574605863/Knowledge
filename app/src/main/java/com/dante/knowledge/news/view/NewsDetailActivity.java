package com.dante.knowledge.news.view;

import android.os.Bundle;
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
import com.dante.knowledge.news.model.DetailNews;
import com.dante.knowledge.news.model.StoryEntity;
import com.dante.knowledge.news.model.TopStoryEntity;
import com.dante.knowledge.news.other.NewsListAdapter;
import com.dante.knowledge.news.presenter.NewsDetailPresenter;
import com.dante.knowledge.news.presenter.NewsDetailPresenterImpl;
import com.dante.knowledge.ui.BaseActivity;
import com.dante.knowledge.utils.ImageUtil;
import com.dante.knowledge.utils.Tool;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsDetailActivity extends BaseActivity implements NewsDetailView {
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
    private StoryEntity story;
    private NewsDetailPresenter presenter;

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.activity_news_detail;
    }

    @Override
    protected void initViews() {
        super.initViews();
        Object object = getIntent().getSerializableExtra(NewsListAdapter.STORY);
        if (object instanceof TopStoryEntity) {
            story = new StoryEntity();
            story.setId(((TopStoryEntity) object).getId());
            story.setTitle(((TopStoryEntity) object).getTitle());
        } else {
            story = (StoryEntity) object;
        }
        toolbarLayout.setTitle(story.getTitle());
        presenter = new NewsDetailPresenterImpl(this);
        initWebView();
        presenter.loadNewsDetail(story);
    }


    private void initWebView() {
        webView = new WebView(getApplicationContext());
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
    public void onBackPressed() {
        super.onBackPressed();
        webView.setVisibility(View.GONE);
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
        Tool.removeActivityFromTransitionManager(this);
    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDetail(final DetailNews detailNews) {
        ImageUtil.load(this, detailNews.getImage(), detailImg);
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + detailNews.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        webView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void showLoadFailed(String msg) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
