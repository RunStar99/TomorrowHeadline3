package com.example.eight.scannews.view;

import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.eight.scannews.R;
import com.example.eight.scannews.beans.NewsBean;
import com.example.eight.scannews.utils.HttpUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NewsDetailActivity extends AppCompatActivity{

    private NewsBean newsBean;
    private ArrayList<String> news;
    private String newsTitle;
    private String newsUrl;
    private String pictureUrl;

    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.detail_toolbar)
    Toolbar detailToolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.news_web_content)
    WebView newsWebContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        setSupportActionBar(detailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        detailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = this.getIntent().getExtras();
        news= bundle.getStringArrayList("news");
        assert news != null;
        newsTitle = news.get(0);
        newsUrl = news.get(1);
        pictureUrl = news.get(2);

        Log.e("NewsDetailActivity--->", "onCreate: " + newsTitle + "/"
                                + newsUrl + "/"
                                + pictureUrl);

        WebSettings webSettings = newsWebContent.getSettings();
        webSettings.setJavaScriptEnabled(true);                                                             //支持JS
        webSettings.setUseWideViewPort(true);                                                                   //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true);                                                              // 缩放至屏幕的大小

        HttpUtils.showPicture(getApplicationContext(), ivImage, pictureUrl);

        progress.setVisibility(View.VISIBLE);

        newsWebContent.loadUrl(newsUrl);
        newsWebContent.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e("------>", "onReceivedError: " + request + " --- " + error.toString());
                progress.setVisibility(View.GONE);
                if (view == null) {
                    Snackbar.make(view, "加载失败", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}
