package com.intertive.http.parser;


import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.intertive.http.ErrorCons;
import com.intertive.http.model.DataListRes;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;
import rxhttp.wrapper.entity.ParameterizedTypeImpl;
import rxhttp.wrapper.exception.HttpStatusCodeException;
import rxhttp.wrapper.parse.TypeParser;


/**
 * @author Nevio
 * on 2022/2/2
 */
public class ListParser<T> extends TypeParser<List<T>> {

    @Override
    public List<T> onParse(@NonNull Response response) throws IOException {
        int httpCode = response.code();
        if (httpCode >= 200 && httpCode < 300){
            Type dataType = TypeTokenLocal.getSuperclassTypeParameter(this.getClass());
            final Type type = ParameterizedTypeImpl.get(DataListRes.class, dataType); //获取泛型类型
            ResponseBody responseBody = response.body();
            List<T> data = null;
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

                    } else {
                        if (jsonObject.has("code")){
                            code = jsonObject.getInt("code");
                        }
                        if (jsonObject.has("msg")){
                            msg = jsonObject.getString("msg");
                        }
                        if (jsonObject.has("data")){
                            dataJson = jsonObject.getString("data");
                        }

                    }

                    if ("[]".equals(dataJson)){
                        json = json.replace("\"data\":[]", "\"data\":{}");
                    }

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

                DataListRes<T> dataListRes = gson.fromJson(json, type);
                data = dataListRes.getData(); //获取data字段

            }

            if (data == null){
                data = gson.fromJson("[]", List.class);
            }

            return data;

        } else {
            throw ExceptionUtil.handleException(new HttpStatusCodeException(response));
        }
    }

}
