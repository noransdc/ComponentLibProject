package com.intertive.wheelview;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intertive.wheelview.model.WheelArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nevio
 * on 2022/2/10
 */
public class CityManager {


    private static final List<WheelArea> areaList = new ArrayList<>();
    private static final List<String> provinceNameList = new ArrayList<>();
    private static final List<List<String>> cityNameList = new ArrayList<>();
    private static final List<List<List<String>>> districtNameList = new ArrayList<>();


    private CityManager(){}


    public static List<WheelArea> getAreaList(Context context){
        if (areaList.isEmpty()){
            try {
                String json = getJsonStr(context, "area_cn.json");
                List<WheelArea> list = new Gson().fromJson(json, new TypeToken<List<WheelArea>>(){}.getType());
                areaList.addAll(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return areaList;
    }

    public static List<String> getProvinceNameList(Context context){
        if (provinceNameList.isEmpty()){
            parseData(context);
        }
        return provinceNameList;
    }

    public static List<List<String>> getCityNameList(Context context){
        if (cityNameList.isEmpty()){
            parseData(context);
        }
        return cityNameList;
    }

    public static List<List<List<String>>> getDistrictNameList(Context context){
        if (districtNameList.isEmpty()){
            parseData(context);
        }
        return districtNameList;
    }

    private static void parseData(Context context){
        List<WheelArea> provinceList = getAreaList(context);
        for (WheelArea provinceBean : provinceList) {
            provinceNameList.add(provinceBean.getName());
            if (provinceBean.getList() != null) {
                List<String> cityList = new ArrayList<>();
                List<List<String>> districtParentList = new ArrayList<>();
                for (WheelArea cityBean : provinceBean.getList()) {
                    cityList.add(cityBean.getName());
                    if (cityBean.getList() != null) {
                        List<String> districtChildList = new ArrayList<>();
                        for (WheelArea districtBean : cityBean.getList()) {
                            districtChildList.add(districtBean.getName());
                        }
                        districtParentList.add(districtChildList);
                    }
                }
                districtNameList.add(districtParentList);
                cityNameList.add(cityList);
            }
        }
    }

    //读取方法
    private static String getJsonStr(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
