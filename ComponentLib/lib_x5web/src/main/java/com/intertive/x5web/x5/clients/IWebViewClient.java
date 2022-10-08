package com.intertive.x5web.x5.clients;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.intertive.x5web.WebViewPlugin;
import com.intertive.x5web.x5.ProgressWebView;
import com.intertive.x5web.x5.clients.cache.CacheResourceClient;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;

import static com.intertive.x5web.x5.tools.WebTools.isWebHost;


/**
 * author: Rea.X
 * date: 2017/4/7.
 */
public class IWebViewClient extends CacheResourceClient {

    private boolean isLoadSuccess = true;
    private String errorMessage;
    private ProgressWebView view;
    protected Context context;
    protected ProgressBar lineProgressBar;
    protected ProgressBar circleProgressbar;
    protected boolean isShowLineProgress, isShowCircleProgress;

    public IWebViewClient(ProgressWebView view) {
        this.view = view;
        this.context = view.getContext();
        this.lineProgressBar = view.getLineProgressbar();
        this.circleProgressbar = view.getCircleProgressbar();
        this.isShowLineProgress = WebViewPlugin.getInstance().getConfig().isShowLineLoading();
        this.isShowCircleProgress = WebViewPlugin.getInstance().getConfig().isShowCircleLoading();
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (isShowLineProgress) {
            this.lineProgressBar.setVisibility(View.VISIBLE);
            this.lineProgressBar.setProgress(0);
        }
        if (isShowCircleProgress){
            this.circleProgressbar.setVisibility(View.VISIBLE);
        }
        WebViewPlugin.wvLog("onPageStarted->" + url);
        isLoadSuccess = true;
        this.view.showNormalView();
    }

    @Override
    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
//        super.onReceivedSslError(webView, sslErrorHandler, sslError);
        sslErrorHandler.proceed();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String s) {
        WebViewPlugin.wvLog("重定向:" + s);
        if (!TextUtils.isEmpty(s) && s.startsWith("tel:")) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(s));
            webView.getContext().startActivity(intent);
            return true;
        }

        if (TextUtils.isEmpty(s)) {
            return false;
        }
        if (s.contains("javascript: void(0)") || s.contains("javascript:void(0)")) {
            return false;
        }
        if (isShowLineProgress) {
            this.lineProgressBar.setVisibility(View.VISIBLE);
            this.lineProgressBar.setProgress(0);
        }
        if (isShowCircleProgress){
            this.circleProgressbar.setVisibility(View.VISIBLE);
        }
        Uri uri = Uri.parse(s);

        //try{} => catch{} added by Barry
        String schem;
        try {
            schem = uri.getScheme();
        } catch (UnsupportedOperationException e) {
            return false;
        }

        if (schem != null && schem.contains("file")) {
            return false;
        }
        if (schem != null && (schem.contains("http") || schem.contains("https"))) {
            if (isWebHost(s)) {
                webView.loadUrl(s);
                return true;
            }
            return false;
        } else {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(s));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                webView.getContext().startActivity(intent);
            } catch (Exception e) {
                WebViewPlugin.wvLog(e.toString());
            }
        }
        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        WebViewPlugin.wvLog("onPageFinished->" + url);
        WebViewPlugin.wvLog("onPageFinished->isLoadSuccess->" + isLoadSuccess);
        this.circleProgressbar.setVisibility(View.GONE);
        this.lineProgressBar.setVisibility(View.GONE);
        if (!isLoadSuccess) {
            this.view.showFailView();
        } else {
            this.view.showNormalView();
        }
    }


    @Override
    public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
        super.onReceivedError(webView, errorCode, description, failingUrl);
        WebViewPlugin.wvLog("页面加载错误：111:::" + description + "::::" + failingUrl);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return;
        }
        errorMessage = "错误码：" + errorCode + ",信息：" + description;
        this.view.showFailView();
        isLoadSuccess = false;
    }

    @Override
    public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
        super.onReceivedError(webView, webResourceRequest, webResourceError);
        WebViewPlugin.wvLog("页面加载错误：222:::" + webResourceError.getDescription() + ":::" + webResourceRequest.getUrl());
        if (webResourceRequest.isForMainFrame()) {
            errorMessage = "错误码：" + webResourceError.getErrorCode() + ",信息：" + webResourceError.getDescription();
            this.view.showFailView();
            isLoadSuccess = false;
        }
    }

}
