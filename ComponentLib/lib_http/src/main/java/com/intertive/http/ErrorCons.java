package com.intertive.http;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Nevio
 * on 2022/2/2
 */
public class ErrorCons {

    public static Set<String> codeSuccessSet = new HashSet<>();

    /**
     * 完全成功
     */
    public static String CODE_SUCCESS = "0";
    public static String CODE_SUCCESS_1 = "0000";

    /**
     * 对应HTTP的状态码
     */
    public static final String BAD_REQUEST = "400";
    public static final String UNAUTHORIZED = "401";
    public static final String FORBIDDEN = "403";
    public static final String NOT_FOUND = "404";
    public static final String METHOD_NOT_ALLOWED = "405";
    public static final String REQUEST_TIMEOUT = "408";
    public static final String CONFLICT = "409";
    public static final String PRECONDITION_FAILED = "412";
    public static final String INTERNAL_SERVER_ERROR = "500";
    public static final String BAD_GATEWAY = "502";
    public static final String SERVICE_UNAVAILABLE = "503";
    public static final String GATEWAY_TIMEOUT = "504";

    public static final String UNKNOWN = "1500";
    public static final String NETWORK_ERROR = "1501";
    public static final String PARSE_ERROR = "1502";
    public static final String HTTP_ERROR = "1504";
    public static final String JSON_ERROR = "1505";
    public static final String CODE_TYPE_ERROR = "1506";




    public static final String MSG_UNKNOWN = "Unknown Error";

    public static final String MSG_NETWORK_ERROR = "Network error";
    public static final String MSG_PARSE_ERROR = "Parse error";
    public static final String MSG_CONNECTION_FAILED = "Connection failed";
    public static final String MSG_NETWORK_NOT_CONNECTED = "Network not connected";
    public static final String MSG_SERVER_RESPONSE_TIMEOUT = "Server response timeout";
    public static final String MSG_NO_DATA = "No Data";
    public static final String MSG_CODE_TYPE_ERROR = "CODE_TYPE_ERROR";

}
