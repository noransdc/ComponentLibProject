package com.intertive.x5web.x5.clients;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.intertive.x5web.WebViewPlugin;
import com.intertive.x5web.utils.LogX5;
import com.intertive.x5web.x5.ProgressWebView;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.WebView;


/**
 * author: Rea.X
 * date: 2017/8/8.
 */
public class IWebChromeClient extends IWebChromeFileClient {
    private WebView webView;
    private FrameLayout fullFrameLayout;
    private ProgressBar lineProgressbar;
    private ProgressBar circleProgressbar;
    private IX5WebChromeClient.CustomViewCallback callback;
    private boolean isShowLineProgress, isShowCircleProgress;


    public IWebChromeClient(ProgressWebView view) {
        super(view);
        this.webView = view.getWebView();
        this.fullFrameLayout = view.getFullFrameLayout();
        this.lineProgressbar = view.getLineProgressbar();
        this.circleProgressbar = view.getCircleProgressbar();
        this.isShowLineProgress = WebViewPlugin.getInstance().getConfig().isShowLineLoading();
        this.isShowCircleProgress = WebViewPlugin.getInstance().getConfig().isShowCircleLoading();
    }

    @Override
    public void onProgressChanged(WebView webView, int newProgress) {
        super.onProgressChanged(webView, newProgress);
        if (isShowLineProgress) {
            this.lineProgressbar.setVisibility(View.VISIBLE);
            this.lineProgressbar.setProgress(newProgress);
        }
        if (newProgress >= 80) {
            this.circleProgressbar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        StringBuffer sb = new StringBuffer();
        sb.append("\n|");
        sb.append("|------------------------------------------------------------------------------------------------------------------|");
        sb.append("\n|");
        sb.append("|\tannounce->" + consoleMessage.message());
        sb.append("\n|");
        sb.append("|\tsourceId->" + consoleMessage.sourceId());
        sb.append("\n|");
        sb.append("|\tlineNumber->" + consoleMessage.lineNumber());
        sb.append("\n|");
        sb.append("|\tmessageLevel->" + consoleMessage.messageLevel());
        sb.append("\n|");
        sb.append("|----------------------------------------------------------------------------------------------------------------|");
        switch (consoleMessage.messageLevel()) {
            case ERROR:
                LogX5.e("consoleMessage:" + sb.toString());
                break;
            case WARNING:
                LogX5.w("consoleMessage:" + sb.toString());
                break;
            default:
                LogX5.d("consoleMessage:" + sb.toString());
                break;
        }
        return super.onConsoleMessage(consoleMessage);
    }

    @Override
    public void onHideCustomView() {
        WebViewPlugin.wvLog("onHideCustomView:");
        if (callback != null) {
            callback.onCustomViewHidden();
        }
        webView.setVisibility(View.VISIBLE);
        fullFrameLayout.removeAllViews();
        fullFrameLayout.setVisibility(View.GONE);
        super.onHideCustomView();
    }


    @Override
    public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
        WebViewPlugin.wvLog("onShowCustomView:");
        webView.setVisibility(View.GONE);
        fullFrameLayout.setVisibility(View.VISIBLE);
        fullFrameLayout.removeAllViews();
        fullFrameLayout.addView(view);
        callback = customViewCallback;
        super.onShowCustomView(view, customViewCallback);
    }

    @Override
    public void onShowCustomView(View view, int i, IX5WebChromeClient.CustomViewCallback customViewCallback) {
        WebViewPlugin.wvLog("onShowCustomView:");
        webView.setVisibility(View.GONE);
        fullFrameLayout.setVisibility(View.VISIBLE);
        fullFrameLayout.removeAllViews();
        fullFrameLayout.addView(view);
        callback = customViewCallback;
        super.onShowCustomView(view, i, customViewCallback);
    }


}
