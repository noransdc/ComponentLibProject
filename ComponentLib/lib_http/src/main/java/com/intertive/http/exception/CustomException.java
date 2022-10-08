package com.intertive.http.exception;

import androidx.annotation.NonNull;

import java.io.IOException;

/**
 * @author Nevio
 * on 2022/2/2
 */
public class CustomException extends IOException {


    private int errorCode;
    private String errorMsg;


    public CustomException(int code, String msg, Throwable cause){
        super(msg, cause);
        this.errorCode = code;
        this.errorMsg = msg;
    }

    public CustomException(int code, @NonNull Throwable cause){
        this(code, cause.getMessage(), cause);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "CustomException{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
