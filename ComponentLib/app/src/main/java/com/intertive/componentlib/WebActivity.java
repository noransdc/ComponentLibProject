package com.intertive.componentlib;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.intertive.x5web.x5.ProgressWebView;

/**
 * @author Nevio
 * on 2022/2/4
 */
public class WebActivity extends AppCompatActivity {

    private ProgressWebView pbWebView;

    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);


        pbWebView = findViewById(R.id.web_view);


        initWebView();

        load("http://www.google.com");

    }



    private void initWebView() {
//        pbWebView.getWebView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_0c12));
        pbWebView.getWebView().getSettings().setAppCacheEnabled(true);
        pbWebView.getWebView().getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
//        pbWebView.getWebView().getSettings().setAppCachePath(cachePath);
//        pbWebView.getWebView().getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//        if (Build.VERSION.SDK_INT >= 26) {
//            pbWebView.getWebView().getSettings().setSafeBrowsingEnabled(false);
//        }
//        pbWebView.getWebView().getSettings().setUserAgentString(pbWebView.getWebView().getSettings().getUserAgentString()
//                + "android_native/version_" + VersionUtil.getVersionName(getAppContext()));
//        pbWebView.setIWebChromeClient(new CustomChromeClient(pbWebView));
//        pbWebView.setIWebViewClient(new CustomWebClient(pbWebView));
//        pbWebView.getWebView().addJavascriptInterface(new JSInterface(getHostActivity()), "JSInterface");
//        pbWebView.setFailView(View.inflate(getAppContext(), R.layout.layout_web_x5_paying, null));
    }

    private void load(String url) {
        if (!TextUtils.isEmpty(url)) {
            pbWebView.getWebView().loadUrl(url);
        }
    }


}
