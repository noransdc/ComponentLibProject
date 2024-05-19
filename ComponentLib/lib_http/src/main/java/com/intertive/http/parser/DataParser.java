package com.intertive.http.parser;


import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.intertive.http.ErrorCons;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;
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

        if (httpCode < 200 || httpCode >= 300){
            throw ExceptionUtil.handleException(new HttpStatusCodeException(response));
        }

        Type dataType = TypeTokenLocal.getSuperclassTypeParameter(this.getClass());
//            final Type type = ParameterizedTypeImpl.get(DataRes.class, dataType); //获取泛型类型
        ResponseBody responseBody = response.body();
        T data = null;
        Gson gson = new Gson();
        if (responseBody != null){
            String json = responseBody.string();

            String code = ErrorCons.UNKNOWN;
            String msg = ErrorCons.MSG_UNKNOWN;
            String dataJson = null;

            try {
                JSONObject jsonObject = new JSONObject(json);

                if (jsonObject.has("head")){
                    JSONObject head = jsonObject.getJSONObject("head");
                    code = head.getString("errCode");
                    msg = head.getString("errMsg");
                    dataJson = jsonObject.getString("body");

                } else if (jsonObject.has("resultCode")){
                    code = jsonObject.getString("resultCode");
                    msg = jsonObject.getString("resultDesc");
                    dataJson = jsonObject.getString("body");

                } else if (jsonObject.has("code")){
                    code = jsonObject.getString("code");

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
                throw ExceptionUtil.handleException(e);
            }

            ErrorCons.codeSuccessSet.add(ErrorCons.CODE_SUCCESS);
            ErrorCons.codeSuccessSet.add(ErrorCons.CODE_SUCCESS_1);

            //code不等于200，说明数据不正确，抛出异常
            if (!ErrorCons.codeSuccessSet.contains(code)) {
                response.close();
                throw ExceptionUtil.postServerException(code, msg, data);
            }

        }

        if (data == null){
            data = gson.fromJson("{}", dataType);
        }

        return data;


    }


}
