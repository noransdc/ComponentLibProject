package com.intertive.http;

/**
 * @author Nevio
 * on 2022/2/2
 */
public class ErrorCons {

    /**
     * 对应HTTP的状态码
     */
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int METHOD_NOT_ALLOWED = 405;
    public static final int REQUEST_TIMEOUT = 408;
    public static final int CONFLICT = 409;
    public static final int PRECONDITION_FAILED = 412;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;

    public static final int UNKNOWN = 1500;
    public static final int NETWORK_ERROR = 1501;
    public static final int PARSE_ERROR = 1502;
    public static final int HTTP_ERROR = 1504;
    public static final int JSON_ERROR = 1505;

    /**
     * 完全成功
     */
    public static int CODE_SUCCESS = 200;
    /**
     * Token 失效
     */
    public static final int CODE_TOKEN_INVALID = 602;
    /**
     * 缺少参数
     */
    public static final int CODE_NO_MISSING_PARAMETER = 400400;
    /**
     * 其他情况
     */
    public static final int CODE_NO_OTHER = 403;
    /**
     * 统一提示
     */
    public static final int CODE_SHOW_TOAST = 400000;



    public static final String MSG_UNKNOWN = "Unknown Error";

    public static final String MSG_NETWORK_ERROR = "Network error";
    public static final String MSG_PARSE_ERROR = "Parse error";
    public static final String MSG_CONNECTION_FAILED = "Connection failed";
    public static final String MSG_NETWORK_NOT_CONNECTED = "Network not connected";
    public static final String MSG_SERVER_RESPONSE_TIMEOUT = "Server response timeout";
    public static final String MSG_NO_DATA = "No Data";

}
