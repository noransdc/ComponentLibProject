package com.intertive.http.model;

/**
 * @author Nevio
 * on 2022/2/2
 */
public class ErrorInfo extends Throwable {

    private String code;
    private String msg;
    private Object dataObj;

    public ErrorInfo() {
    }

    public ErrorInfo(String code, String msg, Object obj) {
        super();
        this.code = code;
        this.msg = msg;
        this.dataObj = obj;
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
        return "ErrorInfo{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", dataObj=" + dataObj +
                '}';
    }
}
