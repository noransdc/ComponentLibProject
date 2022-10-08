package com.intertive.http.listener;

/**
 * @author Nevio
 * on 2022/2/24
 */
public interface ApiCallback<T> {


    void onSuccess(T data);

    void onFailed(int code, String msg);

}
