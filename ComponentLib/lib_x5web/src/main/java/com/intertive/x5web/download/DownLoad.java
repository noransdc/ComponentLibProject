package com.intertive.x5web.download;


import com.intertive.x5web.WebViewPlugin;
import com.intertive.x5web.utils.ClientManager;
import com.intertive.x5web.utils.CommonX5;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;

import java.io.File;


public enum DownLoad {
    INSTANT;


    /**
     * 下载服务，暂时不支持下载到指定位置，因为没有权限
     *
     * @param downloadUrl 下载地址
     * @param callBack    下载监听
     */
    public void download(final String downloadUrl, final DownloadCallback callBack) {
        download(downloadUrl, CommonX5.getNameFromUrl(downloadUrl), callBack);
    }

    public void download(final String downloadUrl, final String path, final String filename, final DownloadCallback callBack) {
        OkGo.<File>get(downloadUrl)
                .cacheMode(CacheMode.NO_CACHE)
                .client(ClientManager.getDownloadClient())
                .tag("download")
                .execute(new FileCallback(path, filename) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<File> response) {
                        if(response == null)return;
                        File file = response.body();
                        if (callBack != null)
                            callBack.requestSuccess(file);
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        if (callBack != null)
                            callBack.downProgress(progress.currentSize, progress.totalSize, (int) (progress.fraction * 100));
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<File> response) {
                        super.onError(response);
                        if (callBack != null)
                            callBack.requestFail();
                    }
                });
    }

    public void download(final String downloadUrl, final String filename, DownloadCallback callBack) {
        download(downloadUrl, CommonX5.getCacheDir(WebViewPlugin.getInstance().getApplication()), filename, callBack);
    }

    public void cancelDownload() {
        OkGo.cancelTag(ClientManager.getDownloadClient(), "download");
    }


    public interface DownloadCallback {
        void requestSuccess(File file);

        void requestFail();

        void downProgress(long readLength, long contentLength, int progress);
    }
}
