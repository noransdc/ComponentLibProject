package com.intertive.x5web.x5.tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;


import com.intertive.x5web.WebViewPlugin;
import com.intertive.x5web.dialog.AbstractUpdateProgressCustomDialog;
import com.intertive.x5web.download.DownLoad;
import com.intertive.x5web.utils.CommonX5;

import java.io.File;

import static com.intertive.x5web.utils.CommonX5.deleteFile;


public enum DownloadAndInstallApk {
    INSTANT;

    private AbstractUpdateProgressCustomDialog downloadProgressDialog;

    public void downloadApkAndInstall(Context context, String url) {
        File apkFile = new File(CommonX5.getCacheDir(context), CommonX5.getNameFromUrl(url));
        if (apkFile.exists()) {
            boolean f = install(apkFile);
            if (!f) {
                startDownload(context, url);
                return;
            }
        }
        startDownload(context, url);
    }

    /**
     * 开始下载
     *
     * @param completeUrl 下载的地址
     */
    private void startDownload(Context context, String completeUrl) {
        if (context instanceof FragmentActivity) {
            if(downloadProgressDialog != null){
                return;
            }
            FragmentActivity activity = (FragmentActivity) context;
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            downloadProgressDialog = WebViewPlugin.getInstance().getConfig().downloadApkProgressDialog();
//            downloadProgressDialog.showWithException(fragmentManager.beginTransaction(), completeUrl);
            downloadProgressDialog.setOnDismissListener(new AbstractUpdateProgressCustomDialog.onDismissListener() {
                @Override
                public void onDismiss() {
                    downloadProgressDialog = null;
                }
            });
        }
        DownLoad.INSTANT.download(completeUrl, downloadCallback);
    }

    private DownLoad.DownloadCallback downloadCallback = new DownLoad.DownloadCallback() {
        @Override
        public void requestSuccess(File file) {
            if (downloadProgressDialog != null) {
                try {
                    downloadProgressDialog.dismissAllowingStateLoss();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            install(file);
        }

        @Override
        public void requestFail() {
            Toast.makeText(WebViewPlugin.getInstance().getApplication(), "下载失败", Toast.LENGTH_SHORT).show();
            if (downloadProgressDialog != null) {
                try {
                    downloadProgressDialog.dismissAllowingStateLoss();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void downProgress(long readLength, long contentLength, int progress) {
            try {
                if (downloadProgressDialog != null)
                    downloadProgressDialog.downloadProgress(progress);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 调用系统Intent安装apk包
     */
    private boolean install(File apkFile) {
        if (apkFile == null) {
            return false;
        }
        try {
            if (!checkApkFile(apkFile.getAbsolutePath())) {
//                Toast.makeText(WebViewPlugin.getInstance().getApplication(), "安装包下载出错", Toast.LENGTH_SHORT).show();
                return false;
            }
            Intent intent = CommonX5.getInstallIntent(apkFile);
            WebViewPlugin.getInstance().getApplication().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 检查apk文件是否有效(是正确下载,没有损坏的)
     */
    public boolean checkApkFile(String apkFilePath) {
        boolean result;
        try {
            PackageManager pManager = WebViewPlugin.getInstance().getApplication().getPackageManager();
            PackageInfo pInfo = pManager.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
            if (pInfo == null) {
                delFile(apkFilePath);
                result = false;
            } else {
                result = true;
            }
        } catch (Exception e) {
            delFile(apkFilePath);
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    private void delFile(String path) {
        File f = new File(path);
        deleteFile(f);
    }

}
