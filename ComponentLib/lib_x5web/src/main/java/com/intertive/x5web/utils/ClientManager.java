package com.intertive.x5web.utils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;


/**
 * author: Rea.X
 * date: 2017/7/19.
 */

public class ClientManager {

    //网络超时时间
    public static final long TIMEOUT = 20 * 1000;
    private static OkHttpClient defaultClient, checkDomainClient, downloadClient, getDomainClient;

    private static OkHttpClient getOkHttpClient(long timeout, boolean isCheckDomain, boolean isDownload) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.followRedirects(isDownload);
        builder.followSslRedirects(isDownload);
        builder.readTimeout(timeout, TimeUnit.MILLISECONDS);
        builder.writeTimeout(timeout, TimeUnit.MILLISECONDS);
        builder.retryOnConnectionFailure(false);
        builder.connectTimeout(timeout, TimeUnit.MILLISECONDS);
        builder.connectionPool(new ConnectionPool(10, 2 * 60 * 1000, TimeUnit.MILLISECONDS));
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
//        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        builder.sslSocketFactory(new SSL(sslParams1.trustManager), sslParams1.trustManager);
//        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
//        builder.hostnameVerifier(new HostnameVerifier() {
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                return true;
//            }
//        });
        return builder.build();
    }


    private static class SafeTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {

        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
            for (java.security.cert.X509Certificate certificate : x509Certificates) {
                certificate.checkValidity(); //检查证书是否过期，签名是否通过等
            }
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }
    }

    public static OkHttpClient getDefaultHttpClient() {
        if (defaultClient == null) {
            defaultClient = getOkHttpClient(TIMEOUT, false, false);
        }
        return defaultClient;
    }

    public static OkHttpClient getCheckDomainClient() {
        if (checkDomainClient == null) {
            checkDomainClient = getOkHttpClient(15 * 1000, true, false);
        }
        return checkDomainClient;
    }

    public static OkHttpClient getDomainClient() {
        if (getDomainClient == null) {
            getDomainClient = getOkHttpClient(15 * 1000, false, false);
        }
        return getDomainClient;
    }

    public static OkHttpClient getDownloadClient() {
        if (downloadClient == null) {
            downloadClient = getOkHttpClient(TIMEOUT, false, true);
        }
        return downloadClient;
    }

    public static Request<String, PostRequest<String>> post(Map<String, String> params, String url, OkHttpClient OkHttpClient, String tag) {
        return OkGo.<String>post(url).params(params).client(OkHttpClient).tag(tag);
    }
}
