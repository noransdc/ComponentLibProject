package com.intertive.http.parser;

import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.intertive.http.ErrorCons;
import com.intertive.http.exception.CustomException;
import com.intertive.http.exception.ServerException;


import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.ParseException;

import rxhttp.wrapper.exception.HttpStatusCodeException;


/**
 * @author Nevio
 * on 2022/2/2
 */
public class ExceptionUtil {


    /**
     * 这个可以处理服务器请求成功，但是业务逻辑失败，比如token失效需要重新登陆
     *
     * @param code 自定义的code码
     */
    public static CustomException postServerException(String code, String msg, Object dataObj) {
        ServerException serverException = new ServerException();
        serverException.setCode(code);
        serverException.setMessage(msg);
        serverException.setDataObj(dataObj);
        return handleException(serverException);
    }

    /**
     * 这个是处理网络异常，也可以处理业务中的异常
     *
     * @param e e异常
     */
    public static CustomException handleException(Throwable e) {
        CustomException customException;
        //HTTP错误   网络请求异常 比如常见404 500之类的等
        if (e instanceof HttpStatusCodeException) {
            HttpStatusCodeException httpException = (HttpStatusCodeException) e;
            customException = new CustomException(String.valueOf(httpException.getStatusCode()), httpException);

            //均视为网络错误
            String msg = httpException.getMessage();
            if (!TextUtils.isEmpty(msg)){
                customException.setMsg(msg);
            } else {
                customException.setMsg(ErrorCons.MSG_NETWORK_ERROR);
            }

        } else if (e instanceof ServerException) {
            //服务器返回的错误
            ServerException serverException = (ServerException) e;
            String code = serverException.getCode();
            String message = serverException.getMessage();
            customException = new CustomException(code, serverException);
            customException.setMsg(message);
            customException.setDataObj(serverException.getDataObj());

        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            customException = new CustomException(ErrorCons.PARSE_ERROR, e);
            //均视为解析错误
            customException.setMsg(ErrorCons.MSG_PARSE_ERROR);
        } else if (e instanceof ConnectException) {
            customException = new CustomException(ErrorCons.NETWORK_ERROR, e);
            //均视为网络错误
            customException.setMsg(ErrorCons.MSG_CONNECTION_FAILED);
        } else if (e instanceof java.net.UnknownHostException) {
            customException = new CustomException(ErrorCons.NETWORK_ERROR, e);
            //网络未连接
            customException.setMsg(ErrorCons.MSG_NETWORK_NOT_CONNECTED);
        } else if (e instanceof SocketTimeoutException) {
            customException = new CustomException(ErrorCons.NETWORK_ERROR, e);
            //网络未连接
            customException.setMsg(ErrorCons.MSG_SERVER_RESPONSE_TIMEOUT);
        } else {
            customException = new CustomException(ErrorCons.UNKNOWN, e);
            //未知错误
            customException.setMsg(ErrorCons.MSG_UNKNOWN);
        }

        return customException;
    }


}
