package com.intertive.http.model;


/**
 * @author Nevio
 * on 2022/2/1
 */
public class DataRsp<T> {

    private String code;
    private String msg;
    private T data;
    private Object tag;
    private Object dataObj;

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Object getDataObj() {
        return dataObj;
    }

    public void setDataObj(Object dataObj) {
        this.dataObj = dataObj;
    }


    @Override
    public String toString() {
        return "DataRes{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", tag=" + tag +
                ", dataObj=" + dataObj +
                '}';
    }
}
