package com.intertive.http.listener;

import rxhttp.wrapper.entity.Progress;

/**
 * @author Nevio
 * on 2022/2/24
 */
public interface DownloadCallback {


    void onSuccess(String data);

    void onFailed(int code, String msg);

    void onProgress(Progress progress);

}
