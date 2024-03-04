package com.intertive.http.listener;


import android.text.TextUtils;

import com.intertive.http.ErrorCons;
import com.intertive.http.exception.CustomException;
import com.intertive.http.model.ErrorInfo;

import io.reactivex.rxjava3.functions.Consumer;
import rxhttp.wrapper.exception.HttpStatusCodeException;


/**
 * @author Nevio
 * on 2022/2/1
 */
interface OnError<T extends Throwable> extends Consumer<T> {


    @Override
    default void accept(T t) throws Throwable {
        int code;
        String msg;
        if (t instanceof CustomException) {
            CustomException exception = (CustomException) t;
            code = exception.getErrorCode();
            msg = exception.getErrorMsg();
        } else if (t instanceof HttpStatusCodeException) {
            HttpStatusCodeException exception = (HttpStatusCodeException) t;
            code = exception.getStatusCode();
            msg = exception.getResult();

        } else {
            code = ErrorCons.HTTP_ERROR;
            msg = t.getMessage();
        }
        if (TextUtils.isEmpty(msg)) {
            msg = ErrorCons.MSG_UNKNOWN;
        }
        onError(new ErrorInfo(code, msg));
    }

    void onError(ErrorInfo err);

}
