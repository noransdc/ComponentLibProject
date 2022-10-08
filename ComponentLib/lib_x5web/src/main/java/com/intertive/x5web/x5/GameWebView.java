package com.intertive.x5web.x5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.intertive.x5web.utils.LogX5;
import com.intertive.x5web.x5.tools.WebTools;
import com.tencent.smtt.sdk.WebView;

import java.util.Map;

import static com.intertive.x5web.x5.tools.WebTools.isWebHost;


/**
 * author: Rea.X
 * date: 2017/9/23.
 */

public class GameWebView extends WebView {
    public GameWebView(Context context) {
        super(context);
    }

    private OnScrollChangedCallback callback;

    public GameWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        removeSearchBoxJavaBridgeInterface();
        LogX5.d("PostWebView create::user method::0");
    }

    public GameWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        removeSearchBoxJavaBridgeInterface();
        LogX5.d("PostWebView create::user method::1");
    }

    public GameWebView(Context context, AttributeSet attributeSet, int i, boolean b) {
        super(context, attributeSet, i, b);
        removeSearchBoxJavaBridgeInterface();
        LogX5.d("PostWebView create::user method::2");
    }

    public GameWebView(Context context, AttributeSet attributeSet, int i, Map<String, Object> map, boolean b) {
        super(context, attributeSet, i, map, b);
        removeSearchBoxJavaBridgeInterface();
        LogX5.d("PostWebView create::user method::3");
    }

    @SuppressLint("NewApi")
    private void removeSearchBoxJavaBridgeInterface() {
        try {
            if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 17) {
                removeJavascriptInterface("searchBoxJavaBridge_");
                removeJavascriptInterface("accessibility");
                removeJavascriptInterface("accessibilityTraversal");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadUrl(String url) {
        loadUrl(url, null);

        getSettings();
    }

    @Override
    public void loadUrl(String url, Map<String, String> map) {
        Uri uri = Uri.parse(url);
        if (uri == null) {
            super.loadUrl(url, map);
            return;
        }
        String scheme = uri.getScheme();
        if (TextUtils.isEmpty(scheme)) {
            super.loadUrl(url, map);
            return;
        }
        if (isWebHost(url)) {
            url = WebTools.urlDeleteUserMsg(url);
        }
        super.loadUrl(url, map);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (callback != null) {
            callback.onScroll(l, oldl, t, oldt);
        }
    }

    public interface OnScrollChangedCallback {
        void onScroll(int x, int oldx, int y, int oldy);
    }

    public void setOnScrollChangedCallback(OnScrollChangedCallback callback) {
        this.callback = callback;
    }

}
