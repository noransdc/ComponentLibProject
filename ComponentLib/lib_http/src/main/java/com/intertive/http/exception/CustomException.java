package com.intertive.http.exception;

import androidx.annotation.NonNull;

import java.io.IOException;

/**
 * @author Nevio
 * on 2022/2/2
 */
public class CustomException extends IOException {


    private String code;
    private String msg;
    public Object dataObj;


    public CustomException(String code, String msg, Throwable cause){
        super(msg, cause);
        this.code = code;
        this.msg = msg;
    }

    public CustomException(String code, @NonNull Throwable cause){
        this(code, cause.getMessage(), cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getDataObj() {
        return dataObj;
    }

    public void setDataObj(Object dataObj) {
        this.dataObj = dataObj;
    }

    @Override
    public String toString() {
        return "CustomException{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", dataObj=" + dataObj +
                '}';
    }
}
