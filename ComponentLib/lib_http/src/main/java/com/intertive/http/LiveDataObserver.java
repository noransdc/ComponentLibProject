package com.intertive.http;

import androidx.lifecycle.Observer;

import com.intertive.http.model.DataRes;


/**
 * @author Nevio
 * on 2022/2/1
 */
public abstract class LiveDataObserver<T> implements Observer<DataRes<T>> {

    private Object tag;

    @Override
    public void onChanged(DataRes<T> res) {
        if (res != null){
            tag = res.getTag();

            if (res.getCode() == 200){
                onSuccess(res.getData());
            } else {
                onFailed(res.getCode(), res.getMsg());
            }
        } else {
            onFailed(-1, "未获取到数据");
        }
    }

    public abstract void onSuccess(T data);

    public void onFailed(int code, String msg){

    }

    public Object getTag(){
        return this.tag;
    }

}
