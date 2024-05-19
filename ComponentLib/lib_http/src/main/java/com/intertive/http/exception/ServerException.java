package com.intertive.http.exception;

/**
 * @author Nevio
 * on 2022/2/2
 */
public class ServerException extends RuntimeException{


    public String code;
    public String message;
    public Object dataObj;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getDataObj() {
        return dataObj;
    }

    public void setDataObj(Object dataObj) {
        this.dataObj = dataObj;
    }

    @Override
    public String toString() {
        return "ServerException{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", dataObj=" + dataObj +
                '}';
    }
}
