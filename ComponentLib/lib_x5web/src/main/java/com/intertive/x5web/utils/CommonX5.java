package com.intertive.x5web.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import com.intertive.x5web.WebViewPlugin;

import java.io.File;
import java.util.List;


public class CommonX5 {

    /**
     * 获取程序下载文件夹
     *
     * @param context
     * @return
     */
    public static String getCacheDir(Context context) {
        String path = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            File f = context.getExternalFilesDir("");
            if (f == null) {
                path = context.getFilesDir().getAbsolutePath();
            } else {
                path = f.getAbsolutePath();
            }
        } else {
            path = context.getFilesDir().getAbsolutePath();
        }
        return path;
    }

    public static Intent getInstallIntent(File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String auth = WebViewPlugin.getInstance().getConfig().fileProvidedAuth();
            uri = FileProvider.getUriForFile(WebViewPlugin.getInstance().getApplication(), auth, apkFile);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(apkFile);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    public static String getNameFromUrl(String url) {
        Uri uri = Uri.parse(url);
        List<String> paths = uri.getPathSegments();
        if (paths != null && paths.size() > 0) {
            String filename = paths.get(paths.size() - 1);
            if (!TextUtils.isEmpty(filename) && filename.contains(".")) {
                String[] names = filename.split("\\.");
                if (names.length > 1) {
                    String expend = names[names.length - 1];
                    return MD5.md5(url) + "." + expend;
                }
            }
        }
        return url;
    }

    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            String versionName = info.versionName;
            if (!TextUtils.isEmpty(versionName) && versionName.contains("-")) {
                int index = versionName.indexOf("-");
                if (index > 0) {
                    versionName = versionName.substring(0, index);
                    return versionName;
                }
            }

            return info.versionName;
        } catch (PackageManager.NameNotFoundException var5) {
            var5.printStackTrace();
            return "1";
        }
    }

    public static String getAppName(Context context) {
        PackageManager pm = context.getPackageManager();

        try {
            PackageInfo e = pm.getPackageInfo(context.getPackageName(), 0);
            return (String) pm.getApplicationLabel(e.applicationInfo);
        } catch (PackageManager.NameNotFoundException var5) {
            var5.printStackTrace();
            return "1";
        }
    }

    /**
     * 删除文件
     *
     * @param file 文件
     * @return {@code true}: 删除成功<br>{@code false}: 删除失败
     */
    public static boolean deleteFile(File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }
}
