package com.dykj.zhonganxiao.widget.web;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * @file: ProgressWebView
 * @author: guokang
 * @date: 2019-09-29
 */
public class ProgressWebView extends WebView {

    private ProgressBar progressBar;
    private Context context;
    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressBar = new ProgressBar(context,null,android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,3));//设置宽高属性
        addView(progressBar);
        //设置内部加载器
        setWebChromeClient(new MyWebChromeClient(context,progressBar));
        setWebViewClient(new MyWebViewClient());
    }

    public class MyWebChromeClient extends WebChromeClient {

        private Context context;
        private ProgressBar progressBar;


        public MyWebChromeClient(Context context, ProgressBar progressBar){
            this.context = context;
            this.progressBar = progressBar;
        }


        //监听进度的回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if(newProgress == 100){
                progressBar.setVisibility(View.GONE);
            }else{

                if(progressBar.getVisibility() == View.GONE){
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
            super.onProgressChanged(view, newProgress);
        }
    }


    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }

}