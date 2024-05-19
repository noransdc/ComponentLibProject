package com.intertive.http;

import androidx.lifecycle.Observer;

import com.intertive.http.model.DataRsp;


/**
 * @author Nevio
 * on 2022/2/1
 */
public abstract class LiveDataObserver<T> implements Observer<DataRsp<T>> {

    private Object tag;

    @Override
    public void onChanged(DataRsp<T> res) {
        if (res != null){
            tag = res.getTag();

            if ("200".equals(res.getCode())){
                onSuccess(res.getData());
            } else {
                onFailed(res.getCode(), res.getMsg(), res.getDataObj());
            }
        } else {
            onFailed(ErrorCons.UNKNOWN, ErrorCons.MSG_NO_DATA, null);
        }
    }

    public abstract void onSuccess(T data);

    public void onFailed(String code, String msg, Object dataObj){

    }

    public Object getTag(){
        return this.tag;
    }

}
