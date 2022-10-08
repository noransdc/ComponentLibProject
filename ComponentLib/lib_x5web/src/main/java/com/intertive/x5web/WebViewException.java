package com.intertive.x5web;

public class WebViewException extends RuntimeException {
    public WebViewException() {
    }

    public WebViewException(String message) {
        super(message);
    }

    public WebViewException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebViewException(Throwable cause) {
        super(cause);
    }
}
