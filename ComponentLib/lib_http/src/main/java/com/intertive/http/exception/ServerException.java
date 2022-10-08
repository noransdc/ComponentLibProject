package com.intertive.http.exception;

/**
 * @author Nevio
 * on 2022/2/2
 */
public class ServerException extends RuntimeException{


    public int code;
    public String message;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
