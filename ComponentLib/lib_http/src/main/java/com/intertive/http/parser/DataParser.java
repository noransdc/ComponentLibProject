package com.intertive.http.parser;


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
            final Type type = ParameterizedTypeImpl.get(DataRes.class, dataType); //获取泛型类型
            ResponseBody responseBody = response.body();
            T data = null;
            Gson gson = new Gson();
            if (responseBody != null){
                String json = responseBody.string();

                int code = ErrorCons.UNKNOWN;
                String msg = ErrorCons.MSG_UNKNOWN;
                String dataJson;

                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.has("code")){
                        code = jsonObject.getInt("code");
                    }
                    if (jsonObject.has("msg")){
                        msg = jsonObject.getString("msg");
                    }
                    if (jsonObject.has("data")){
                        dataJson = jsonObject.getString("data");
                        if ("[]".equals(dataJson)){
                            json = json.replace("\"data\":[]", "\"data\":{}");
                        }
                    }

                } catch (Exception e) {
                    response.close();
                    throw ExceptionUtil.serverException(ErrorCons.JSON_ERROR, e.getMessage());
                }

                if (code != ErrorCons.CODE_SUCCESS) {//code不等于200，说明数据不正确，抛出异常
                    response.close();
                    throw ExceptionUtil.serverException(code, msg);
                }

                DataRes<T> dataRes = gson.fromJson(json, type);
                data = dataRes.getData(); //获取data字段

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
