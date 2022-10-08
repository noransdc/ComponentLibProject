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
class ExceptionUtil {


    /**
     * 这个可以处理服务器请求成功，但是业务逻辑失败，比如token失效需要重新登陆
     *
     * @param code 自定义的code码
     */
    public static CustomException serverException(int code, String msg) {
        ServerException serverException = new ServerException();
        serverException.setCode(code);
        serverException.setMessage(msg);
        return handleException(serverException);
    }

    /**
     * 这个是处理网络异常，也可以处理业务中的异常
     *
     * @param e e异常
     */
    public static CustomException handleException(Throwable e) {
        CustomException httpException;
        //HTTP错误   网络请求异常 比如常见404 500之类的等
        if (e instanceof HttpStatusCodeException) {
            HttpStatusCodeException codeException = (HttpStatusCodeException) e;
            httpException = new CustomException(codeException.getStatusCode(), codeException);
            int statusCode = codeException.getStatusCode();
            switch (statusCode) {
                case ErrorCons.BAD_REQUEST:
                case ErrorCons.UNAUTHORIZED:
                case ErrorCons.FORBIDDEN:
                case ErrorCons.NOT_FOUND:
                case ErrorCons.METHOD_NOT_ALLOWED:
                case ErrorCons.REQUEST_TIMEOUT:
                case ErrorCons.CONFLICT:
                case ErrorCons.PRECONDITION_FAILED:
                case ErrorCons.GATEWAY_TIMEOUT:
                case ErrorCons.INTERNAL_SERVER_ERROR:
                case ErrorCons.BAD_GATEWAY:
                case ErrorCons.SERVICE_UNAVAILABLE:
                default:
                    //均视为网络错误
                    String msg = codeException.getMessage();
                    if (!TextUtils.isEmpty(msg)){
                        httpException.setErrorMsg(msg);
                    } else {
                        httpException.setErrorMsg("网络错误");
                    }
                    break;
            }
        } else if (e instanceof ServerException) {
            //服务器返回的错误
            ServerException serverException = (ServerException) e;
            int code = serverException.getCode();
            String message = serverException.getMessage();
            httpException = new CustomException(code, serverException);
            switch (code) {
//                case ErrorCons.CODE_TOKEN_INVALID:
//                    httpException.setErrorMsg("token失效");
                    //下面这里可以统一处理跳转登录页面的操作逻辑
//                    break;
                case ErrorCons.CODE_NO_OTHER:
                    httpException.setErrorMsg("其他情况");
                    break;
                case ErrorCons.CODE_SHOW_TOAST:
                    httpException.setErrorMsg("吐司");
                    break;
                case ErrorCons.CODE_NO_MISSING_PARAMETER:
                    httpException.setErrorMsg("缺少参数");
                    break;
                default:
                    httpException.setErrorMsg(message);
                    break;
            }
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            httpException = new CustomException(ErrorCons.PARSE_ERROR, e);
            //均视为解析错误
            httpException.setErrorMsg("解析错误");
        } else if (e instanceof ConnectException) {
            httpException = new CustomException(ErrorCons.NETWORK_ERROR, e);
            //均视为网络错误
            httpException.setErrorMsg("连接失败");
        } else if (e instanceof java.net.UnknownHostException) {
            httpException = new CustomException(ErrorCons.NETWORK_ERROR, e);
            //网络未连接
            httpException.setErrorMsg("网络未连接");
        } else if (e instanceof SocketTimeoutException) {
            httpException = new CustomException(ErrorCons.NETWORK_ERROR, e);
            //网络未连接
            httpException.setErrorMsg("服务器响应超时");
        } else {
            httpException = new CustomException(ErrorCons.UNKNOWN, e);
            //未知错误
            httpException.setErrorMsg("未知错误");
        }

        return httpException;
    }


}
