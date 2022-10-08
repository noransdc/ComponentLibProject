package com.intertive.http.model;

/**
 * @author Nevio
 * on 2022/2/2
 */
public class ErrorInfo extends Throwable {

    private int code;
    private String msg;

    public ErrorInfo() {
    }

    public ErrorInfo(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ErrorInfo{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
