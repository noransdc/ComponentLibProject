package com.intertive.x5web.x5.tools;

import android.text.TextUtils;

import com.intertive.x5web.WebViewPlugin;
import com.intertive.x5web.utils.CommonX5;
import com.tencent.smtt.sdk.WebView;


/**
 * author: Rea.X
 * date: 2017/11/2.
 */
public class UserAgent {


    //Mozilla/5.0 (Linux; Android 8.0; SM-G950F Build/R16NW; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/043808 Mobile Safari/537.36
    public static String getUserAgent(WebView webview) {
        String defaultAgent = webview.getSettings().getUserAgentString();
        String [] strs = defaultAgent.split(" ");
        StringBuffer sb = new StringBuffer();
        boolean consVersion = false;
        boolean consGreat = false;
        if(!TextUtils.isEmpty(customerUserAgent)){
            sb.append(customerUserAgent);
            sb.append(" ");
        }else{
            for(String s : strs){
                if(s.startsWith("MQQBrowser") || s.startsWith("TBS"))continue;
                if(s.startsWith("app_version"))consVersion = true;
                if(s.startsWith("great-winner,Mobile"))consGreat = true;
                sb.append(s);
                sb.append(" ");
            }
        }
        if(!consVersion){
            sb.append("app_version=");
            sb.append(CommonX5.getVersionName(WebViewPlugin.getInstance().getApplication()));
            sb.append(" ");
        }
        if(!consGreat){
            sb.append("great-winner,Mobile");
        }
        return sb.toString();
    }

    private static String customerUserAgent;
    public static void setCustomerUserAgent(String userAgent){
        customerUserAgent = userAgent;
    }

}
