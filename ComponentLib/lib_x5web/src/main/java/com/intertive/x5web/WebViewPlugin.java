package com.intertive.x5web;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;

import com.intertive.x5web.cache.CacheManager;
import com.intertive.x5web.utils.LogX5;
import com.tencent.smtt.sdk.QbSdk;


public class WebViewPlugin {

    private static final String LOG_TAG = "WebViewPlugin logger tag:";
    private static WebViewPlugin instance;
    private static Object object = new Object();
    private Application application;
    private AbstractWebConfig config;
//    private View failView;

    public static void wvLog(String msg) {
        LogX5.d(LOG_TAG + ":::" + msg);
    }

    private WebViewPlugin() {
    }

    public static WebViewPlugin getInstance() {
        if (instance == null) {
            synchronized (WebViewPlugin.class) {
                if (instance == null)
                    instance = new WebViewPlugin();
            }
        }
        return instance;
    }

    public void init(Application application, AbstractWebConfig config) {
        init(application, config, null);
    }

    private void init(Application application, AbstractWebConfig config, final QbSdk.PreInitCallback callback) {
        this.application = application;
        LogX5.setLogEnable(config.isDebug());
        CacheManager.init(application);
        this.config = config;
        initX5(callback);
    }

    /*public void setCommonWebFailView(View failView) {
        this.failView = failView;
    }*/

    private void initX5(QbSdk.PreInitCallback callback) {
        try {
            QbSdk.setDownloadWithoutWifi(true);
            QbSdk.initX5Environment(application, callback == null ? new QbSdk.PreInitCallback() {
                public void onCoreInitFinished() {
                    WebViewPlugin.wvLog("onCoreInitFinished x5 core load success--------");
                }

                public void onViewInitFinished(boolean b) {
                    WebViewPlugin.wvLog("onViewInitFinished b--------" + b);
                }
            } : callback);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public Application getApplication() {
        return application;
    }

    public AbstractWebConfig getConfig() {
        return config;
    }

    public View getFailView() {
        return LayoutInflater.from(application).inflate(R.layout.lib_x5_failview, null, false);
    }

}
