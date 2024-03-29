package com.intertive.http;


import androidx.lifecycle.MutableLiveData;

import com.intertive.http.model.DataRes;

/**
 * @author Nevio
 * on 2022/2/1
 */
public class LiveDataWrap<T> extends MutableLiveData<DataRes<T>> {


    public void setData(T data){
        DataRes<T> res = new DataRes<>();
        res.setCode(200);
        res.setData(data);
        setValue(res);
    }

    public void setError(int code, String msg){
        DataRes<T> res = new DataRes<>();
        res.setCode(code);
        res.setMsg(msg);
        setValue(res);
    }

    public void setData(T data, Object tag){
        DataRes<T> res = new DataRes<>();
        res.setCode(200);
        res.setData(data);
        res.setTag(tag);
        setValue(res);
    }

    public void setError(int code, String msg, Object tag){
        DataRes<T> res = new DataRes<>();
        res.setCode(code);
        res.setMsg(msg);
        res.setTag(tag);
        setValue(res);
    }
}
