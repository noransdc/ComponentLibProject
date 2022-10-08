package com.intertive.componentlib;


import com.intertive.x5web.AbstractWebConfig;

public class WebViewConfig extends AbstractWebConfig {

    @Override
    public boolean isDebug() {
        return true;
    }

    @Override
    public boolean isShowLineLoading() {
        return false;
    }

    @Override
    public boolean isShowCircleLoading() {
        return false;
    }

    @Override
    public String[] getDomains() {
        return new String[]{""};
    }

    @Override
    public String getCDNHost() {
        return "";
    }

    @Override
    public String fileProvidedAuth() {
        return "";
    }

}
