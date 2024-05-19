package com.intertive.http;


import androidx.lifecycle.MutableLiveData;

import com.intertive.http.model.DataRsp;

/**
 * @author Nevio
 * on 2022/2/1
 */
public class WrapLiveData<T> extends MutableLiveData<DataRsp<T>> {


    public void setData(T data){
        DataRsp<T> res = new DataRsp<>();
        res.setCode("200");
        res.setData(data);
        setValue(res);
    }

    public void setData(T data, Object tag){
        DataRsp<T> res = new DataRsp<>();
        res.setCode("200");
        res.setData(data);
        res.setTag(tag);
        setValue(res);
    }

    public void postData(T data){
        DataRsp<T> res = new DataRsp<>();
        res.setCode("200");
        res.setData(data);
        postValue(res);
    }

    public void postData(T data, Object tag){
        DataRsp<T> res = new DataRsp<>();
        res.setCode("200");
        res.setData(data);
        res.setTag(tag);
        postValue(res);
    }

    public void setError(String code, String msg, Object dataObj){
        DataRsp<T> res = new DataRsp<>();
        res.setCode(code);
        res.setMsg(msg);
        res.setDataObj(dataObj);
        setValue(res);
    }

    public void setError(String code, String msg, Object dataObj, Object tag){
        DataRsp<T> res = new DataRsp<>();
        res.setCode(code);
        res.setMsg(msg);
        res.setTag(tag);
        res.setDataObj(dataObj);
        setValue(res);
    }

    public void postError(String code, String msg, Object dataObj){
        DataRsp<T> res = new DataRsp<>();
        res.setCode(code);
        res.setMsg(msg);
        res.setDataObj(dataObj);
        postValue(res);
    }

    public void postError(String code, String msg, Object dataObj, Object tag){
        DataRsp<T> res = new DataRsp<>();
        res.setCode(code);
        res.setMsg(msg);
        res.setTag(tag);
        res.setDataObj(dataObj);
        postValue(res);
    }
    
}
