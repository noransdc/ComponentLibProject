package com.intertive.x5web.x5.tools;

import android.net.Uri;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.intertive.x5web.WebViewPlugin;
import com.tencent.smtt.sdk.WebView;

import java.util.Set;


/**
 * author: Rea.X
 * date: 2017/11/2.
 */

public class WebTools {

    private static final String KEY_USER_TOKEN = "userToken";
    private static final String KEY_NEW_APP = "newApi";
    private static final String KEY_APP_TOKEN = "appToken";
    private static final String KEY_ACCOUNT_NAME = "accountName";
    private static final String KEY_ACCOUNT_TOKEN = "accountToken";

    /**
     * 删除url中的用户信息
     *
     * @param url
     * @return
     */
    public static String urlDeleteUserMsg(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        Uri originUri = Uri.parse(url);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(originUri.getScheme());
        builder.authority(originUri.getAuthority());
        builder.path(originUri.getPath());
        builder.fragment(originUri.getFragment());
        Set<String> queryParams = originUri.getQueryParameterNames();
        for (String key : queryParams) {
            if (key.equals(KEY_USER_TOKEN) || key.equals(KEY_APP_TOKEN) || key.equals(KEY_ACCOUNT_NAME) || key.equals(KEY_ACCOUNT_TOKEN)) {
                continue;
            }
            builder.appendQueryParameter(key, originUri.getQueryParameter(key));
        }
        return builder.build().toString();
    }

    public static synchronized void releaseWebView(WebView webview) {
        if (webview != null) {
            try {
                if (webview.getParent() != null) {
                    ((ViewGroup) webview.getParent()).removeView(webview);
                }
                webview.stopLoading();
                webview.getSettings().setJavaScriptEnabled(false);
                webview.clearHistory();
                webview.clearView();
                webview.removeAllViews();
                webview.destroy();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isWebHost(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        Uri uri = Uri.parse(url);
        if (uri == null) {
            return false;
        }
        String originScheme = uri.getScheme();
        if (TextUtils.isEmpty(originScheme)){
            return false;
        }
        if (originScheme.equals("file")) {
            return true;
        }
        String[] hosts = WebViewPlugin.getInstance().getConfig().getDomains();
        if (hosts == null || hosts.length == 0){
            return false;
        }
        for (String host : hosts) {
            if (checkIsWbHost(url, host)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkIsWbHost(String originUrl, String host) {
        if (TextUtils.isEmpty(originUrl) || TextUtils.isEmpty(host)) {
            return false;
        }
        Uri originUri = Uri.parse(originUrl);
        Uri hostUri = Uri.parse(host);
        if (originUri == null || hostUri == null) {
            return false;
        }
        String originScheme = originUri.getScheme();
        String hostScheme = hostUri.getScheme();
        String originHost = originUri.getHost();
        String hostHost = hostUri.getHost();
        return originScheme != null && originScheme.equals(hostScheme)
                && originHost != null && originHost.equals(hostHost);
    }

}
