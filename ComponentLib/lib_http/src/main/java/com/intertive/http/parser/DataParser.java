package com.intertive.http.parser;


import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.intertive.http.ErrorCons;
import com.intertive.http.model.DataRes;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;
import rxhttp.wrapper.entity.ParameterizedTypeImpl;
import rxhttp.wrapper.exception.HttpStatusCodeException;
import rxhttp.wrapper.parse.TypeParser;


/**
 * @author Nevio
 * on 2022/2/2
 */
public class DataParser<T> extends TypeParser<T> {

    @Override
    public T onParse(@NonNull Response response) throws IOException {
        int httpCode = response.code();
        if (httpCode >= 200 && httpCode < 300){
            Type dataType = TypeTokenLocal.getSuperclassTypeParameter(this.getClass());
//            final Type type = ParameterizedTypeImpl.get(DataRes.class, dataType); //获取泛型类型
            ResponseBody responseBody = response.body();
            T data = null;
            Gson gson = new Gson();
            if (responseBody != null){
                String json = responseBody.string();

                int code = ErrorCons.UNKNOWN;
                String msg = ErrorCons.MSG_UNKNOWN;
                String dataJson = null;

                try {
                    JSONObject jsonObject = new JSONObject(json);

                    if (jsonObject.has("head")){
                        JSONObject head = jsonObject.getJSONObject("head");
                        code = ParseUtil.parseInt(head.getString("errCode"));
                        msg = head.getString("errMsg");
                        dataJson = jsonObject.getString("body");

                    } else if (jsonObject.has("resultCode")){
                        String resultCode = jsonObject.getString("resultCode");
                        code = ParseUtil.parseInt(resultCode);
                        msg = jsonObject.getString("resultDesc");
                        dataJson = jsonObject.getString("body");

                    } else if (jsonObject.has("code")){
                        code = jsonObject.getInt("code");

                        if (jsonObject.has("msg")){
                            msg = jsonObject.getString("msg");
                        }
                        if (jsonObject.has("data")){
                            dataJson = jsonObject.getString("data");
                        }

                    } else {
                        dataJson = json;
                    }

                    if ("[]".equals(dataJson)){
                        dataJson = dataJson.replace("\"data\":[]", "\"data\":{}");
                    }

                    data = gson.fromJson(dataJson, dataType);

                } catch (Exception e) {
                    response.close();
                    throw ExceptionUtil.serverException(ErrorCons.JSON_ERROR, e.getMessage());
                }

                if (code == ErrorCons.CODE_TYPE_ERROR){
                    msg = ErrorCons.MSG_CODE_TYPE_ERROR;
                }

                if (code != ErrorCons.CODE_SUCCESS) {//code不等于200，说明数据不正确，抛出异常
                    response.close();
                    throw ExceptionUtil.serverException(code, msg);
                }

            }

            if (data == null){
                data = gson.fromJson("{}", dataType);
            }
            return data;

        } else {
            throw ExceptionUtil.handleException(new HttpStatusCodeException(response));
        }
    }


}
