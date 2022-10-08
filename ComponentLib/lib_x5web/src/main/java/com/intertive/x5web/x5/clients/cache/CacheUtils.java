package com.intertive.x5web.x5.clients.cache;

import android.net.Uri;
import android.text.TextUtils;

import com.intertive.x5web.WebViewPlugin;
import com.intertive.x5web.download.DownLoad;
import com.intertive.x5web.utils.CommonX5;
import com.intertive.x5web.utils.MD5;
import com.intertive.x5web.utils.MimeType;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 静态资源缓存工具类
 * author: Rea.X
 * date: 2017/11/17.
 */

public class CacheUtils {

    private CacheUtils() {
    }

    private static final String CACHE_FOLDER = "webCache";
    private static final String TAG = "CacheUtils LOG::";
    private static final String XML = "text/xml";
    private static final String HTML = "text/html";
    private static final String GIF = "image/gif";
    private static final String JPEG = "image/jpeg";
    private static final String CSS = "text/css";
    private static final String PNG = "image/png";
    private static final String JS = "application/x-javascript";
    private static final String BMP = "application/x-bmp";

    private static final String TTF = "ttf";
    private static final String EOT = "eot";
    private static final String WOFF = "woff";
    private static final String WOFF2 = "woff2";
    private static final String MP3 = "audio/mp3";

    private static boolean isCDNHost(String url) {
        if (TextUtils.isEmpty(url)) return false;
        Uri uri = Uri.parse(url);
        if (uri == null) return false;
        String scheme = uri.getScheme();
        if (TextUtils.isEmpty(scheme)) return false;
        if (scheme.equalsIgnoreCase("file")) return false;
        String cdnHost = WebViewPlugin.getInstance().getConfig().getCDNHost();
        if (TextUtils.isEmpty(cdnHost)) return false;
        String host = Uri.parse(cdnHost).getHost();
        String needCheckHost = Uri.parse(url).getHost();
        if (needCheckHost == null) return false;
        if (!TextUtils.isEmpty(host) && needCheckHost.equals(host)) return true;
        return false;
    }

    /**
     * 是否需要缓存
     *
     * @param url url
     * @return true:需要缓存
     */
    private static boolean needCache(String url) {
        if (!isCDNHost(url)) return false;
        String extension = getUrlExtension(url);
        if (TextUtils.isEmpty(extension)) return false;
        String mimeType = MimeType.getMimeType(extension);
        WebViewPlugin.wvLog("url:" + url + "::后缀：" + extension + "::类型：" + mimeType);
        if (TextUtils.isEmpty(mimeType)) return false;
        return //mimeType.equalsIgnoreCase(XML) ||
//                mimeType.equalsIgnoreCase(HTML) ||
                mimeType.equalsIgnoreCase(GIF) ||
                        mimeType.equalsIgnoreCase(JPEG) ||
                        mimeType.equalsIgnoreCase(CSS) ||
                        mimeType.equalsIgnoreCase(PNG) ||
                        mimeType.equalsIgnoreCase(JS) ||
                        mimeType.equalsIgnoreCase(BMP) ||
                        mimeType.equalsIgnoreCase(MP3) ||
                        extension.equalsIgnoreCase(TTF) ||
                        extension.equalsIgnoreCase(EOT) ||
                        extension.equalsIgnoreCase(WOFF) ||
                        extension.equalsIgnoreCase(WOFF2)
                ;
    }

    /**
     * 根据url获取mimetype
     *
     * @param url url
     * @return mimetype
     */
    private static String getMimeType(String url) {
        if (TextUtils.isEmpty(url)) return null;
        String fileExtension = getUrlExtension(url);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
    }

    /**
     * @param url 资源url
     * @return url的mimeType
     */
    private static String getUrlExtension(String url) {
        return MimeTypeMap.getFileExtensionFromUrl(url);
    }

    /**
     * 这个url是否需要缓存
     *
     * @param url url
     * @return null:不需要缓存
     */
    private static boolean checkNeedCache(String url) {
        if (TextUtils.isEmpty(url)) return false;
        Uri uri = Uri.parse(url);
        if (uri == null) return false;
        String scheme = uri.getScheme();
        if (TextUtils.isEmpty(scheme)) return false;
        if (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https"))
            return false;
        String path = uri.getPath();
        if (TextUtils.isEmpty(path)) return false;
        if (!needCache(url)) return false;
        return true;
    }

    /**
     * 缓存资源
     *
     * @param url 需要缓存的资源url
     * @return WebResourceResponse
     */
    public static WebResourceResponse cache(String url) {
        WebViewPlugin.wvLog("拦截到加载资源：" + url);
        if (!WebViewPlugin.getInstance().getConfig().allowCacheResource()) return null;
        if (!checkNeedCache(url)) return null;
        url = removeUrlNullQuery(url);
        String filename = MD5.md5(url);
        File file = new File(CommonX5.getCacheDir(WebViewPlugin.getInstance().getApplication()) + File.separator + CACHE_FOLDER);
        if (!file.exists()) file.mkdirs();
        file = new File(CommonX5.getCacheDir(WebViewPlugin.getInstance().getApplication()) + File.separator + CACHE_FOLDER, filename);
        if (file.exists() && file.length() > 0) {
            WebViewPlugin.wvLog("从缓存读取：：" + url);

            return getResponseFromFile(url, file);
        }
        download(url, filename);
        return null;
    }

    /**
     * 移除url上空的参数（目的是为了删除随机数）
     *
     * @param url 原始的url
     * @return 经过处理后的url
     */
    private static String removeUrlNullQuery(String url) {
        Uri uri = Uri.parse(url);
        Set<String> params = uri.getQueryParameterNames();
        Uri.Builder build = uri.buildUpon();
        build.clearQuery();
        if (params != null && params.size() > 0) {
            for (String key : params) {
                String value = uri.getQueryParameter(key);
                if (!TextUtils.isEmpty(value)) {
                    build.appendQueryParameter(key, value);
                }
            }
        }
        return build.build().toString();
    }

    /**
     * 从本地文件中获取WebResourceResponse
     *
     * @param url  资源远程url，用来获取mimeType
     * @param file 需要转化的文件对象
     * @return WebResourceResponse
     */
    private static WebResourceResponse getResponseFromFile(String url, File file) {
        if (file.exists() && file.length() >= 0) {
            try {
                WebResourceResponse webResourceResponse = new WebResourceResponse();
                InputStream inputStream = new FileInputStream(file);
                webResourceResponse.setData(inputStream);
                webResourceResponse.setMimeType(getMimeType(url));
                Map<String, String> map = new HashMap<>();
                map.put("Access-Control-Allow-Origin", "*");
                webResourceResponse.setResponseHeaders(map);
                webResourceResponse.setEncoding("utf-8");
                return webResourceResponse;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 异步下载需要缓存的资源
     *
     * @param url      资源url
     * @param filename 需要缓存到本地的文件名字
     */
    private static void download(String url, String filename) {
        try {
            String path = CommonX5.getCacheDir(WebViewPlugin.getInstance().getApplication()) + File.separator + CACHE_FOLDER + File.separator;
            DownLoad.INSTANT.download(url, path, filename, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
