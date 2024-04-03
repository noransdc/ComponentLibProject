package com.intertive.http.parser;

import com.intertive.http.ErrorCons;

public class ParseUtil {


    public static int parseInt(String str){
        int r = ErrorCons.CODE_TYPE_ERROR;

        try {

            r = Integer.parseInt(str);

        } catch (Exception e){
            e.printStackTrace();
        }

        return r;
    }


}
