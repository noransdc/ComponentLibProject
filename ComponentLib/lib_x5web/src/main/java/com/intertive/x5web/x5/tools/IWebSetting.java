package com.intertive.x5web.x5.tools;

import android.os.Build;

import com.intertive.x5web.WebViewPlugin;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import static com.intertive.x5web.x5.tools.UserAgent.getUserAgent;


/**
 * date: 2017/8/8.
 */

public class IWebSetting {

    /**
     * 设置webSetting
     *
     * @param webView
     */
    public static void init(WebView webView) {
        try {
            WebSettings webSettings = webView.getSettings();
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSettings.setAppCacheEnabled(true);
            webSettings.setAllowContentAccess(true);
            webSettings.setAllowFileAccess(true);
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
            webSettings.setBlockNetworkImage(false);
            webSettings.setBlockNetworkLoads(false);
            webSettings.setBuiltInZoomControls(false);
            webSettings.setCursiveFontFamily("cursive");
            webSettings.setDisplayZoomControls(true);
            webSettings.setDatabaseEnabled(true);
            webSettings.setSavePassword(false);
            webSettings.setDomStorageEnabled(true);
            webSettings.setFantasyFontFamily("fantasy");
            webSettings.setFixedFontFamily("monospace");
            webSettings.setTextZoom(100);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setLightTouchEnabled(false);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setLoadsImagesAutomatically(true);
            webSettings.setMediaPlaybackRequiresUserGesture(true);
            webSettings.setNavDump(false);
            webSettings.setPluginsEnabled(false);
            webSettings.setSansSerifFontFamily("sans-serif");
            webSettings.setSaveFormData(true);
            webSettings.setSavePassword(false);
            webSettings.setSerifFontFamily("serif");
            webSettings.setStandardFontFamily("sans-serif");
            webSettings.setUseWebViewBackgroundForOverscrollBackground(false);
            webSettings.setSupportMultipleWindows(false);
            webSettings.setSupportZoom(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                webSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    WebView.setWebContentsDebuggingEnabled(WebViewPlugin.getInstance().getConfig().isDebug());
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            webSettings.setEnableSmoothTransition(false);
            webSettings.setGeolocationEnabled(true);

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            if (Build.VERSION.SDK_INT >= 21) {
                cookieManager.setAcceptThirdPartyCookies(webView, true);
            }

            webSettings.setUserAgentString(getUserAgent(webView));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
