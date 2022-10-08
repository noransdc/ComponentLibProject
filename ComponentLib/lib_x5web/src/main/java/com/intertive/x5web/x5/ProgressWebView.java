package com.intertive.x5web.x5;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.intertive.x5web.R;
import com.intertive.x5web.WebViewPlugin;
import com.intertive.x5web.x5.clients.IDownloadListener;
import com.intertive.x5web.x5.clients.IWebChromeClient;
import com.intertive.x5web.x5.clients.IWebLongClickListener;
import com.intertive.x5web.x5.clients.IWebViewClient;
import com.intertive.x5web.x5.tools.IWebSetting;


/**
 * author: Rea.X
 * date: 2017/11/2.
 */
public class ProgressWebView extends RelativeLayout {

    private IWebChromeClient chromeClient;
    private GameWebView mPostWv;
    private ProgressBar mLineLoadPb;
    private ProgressBar mCircleLoadPb;
    private FrameLayout mFullFl;
    private LinearLayout failLl;
    private View failView = WebViewPlugin.getInstance().getFailView();
    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);

    public ProgressWebView(Context context) {
        super(context);
        init(context);
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProgressWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.lib_x5_progress_webview, this, true);
        mPostWv = findViewById(R.id.wv_progress_game);
        mLineLoadPb = findViewById(R.id.pb_progress_line_loading);
        failLl = findViewById(R.id.ll_progress_wv_fail);
        mCircleLoadPb = findViewById(R.id.pb_progress_circle_loading);
        mFullFl = findViewById(R.id.fl_progress_wv_full);
        IWebSetting.init(mPostWv);
        chromeClient = new IWebChromeClient(this);
        mPostWv.setWebChromeClient(chromeClient);
        mPostWv.setWebViewClient(new IWebViewClient(this));
//        mPostWv.setOnLongClickListener(new IWebLongClickListener(mPostWv));
        mPostWv.setDownloadListener(new IDownloadListener(getContext()));
    }

    public GameWebView getWebView() {
        return mPostWv;
    }

    public ProgressBar getLineProgressbar() {
        return mLineLoadPb;
    }

    public ProgressBar getCircleProgressbar() {
        return mCircleLoadPb;
    }

    public FrameLayout getFullFrameLayout() {
        return mFullFl;
    }

    public LinearLayout getFailLayout() {
        return failLl;
    }

    public View getFailView() {
        return failView;
    }

    /**
     * 添加异常时显示的View
     *
     * @param failView 异常时显示的View
     */
    public void setFailView(View failView) {
        resetView(failView);
        this.failView = failView;
    }

    public void showFailView() {
        if (this.failView == null) {
            return;
        }
        failLl.removeAllViews();
        failLl.addView(this.failView, params);
        this.failLl.setVisibility(View.VISIBLE);
        this.mPostWv.setVisibility(View.GONE);
        this.mFullFl.setVisibility(View.GONE);
        this.mCircleLoadPb.setVisibility(View.GONE);
        this.mLineLoadPb.setVisibility(View.GONE);
    }

    public void showNormalView() {
        if (this.failView == null) {
            return;
        }
        this.failLl.setVisibility(View.GONE);
        this.mPostWv.setVisibility(View.VISIBLE);
        this.mFullFl.setVisibility(View.GONE);
    }

    private void resetView(View view) {
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent != null) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            }
        }
    }

    public void setIWebChromeClient(IWebChromeClient chromeClient) {
        mPostWv.setWebChromeClient(chromeClient);
    }

    public void setIWebViewClient(IWebViewClient client) {
        mPostWv.setWebViewClient(client);
    }

    public void setWebLongClickListener(IWebLongClickListener listener) {
        mPostWv.setOnLongClickListener(listener);
    }

    /**
     * 使用此webView，必须在Activity或者Fragment的onActivityResult回调中调用该方法，在super前调用，否则选择文件将不起效
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        chromeClient.onActivityResult(requestCode, resultCode, data);
    }


}
