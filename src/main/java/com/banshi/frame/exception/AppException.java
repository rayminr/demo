package com.banshi.frame.exception;

/**
 * Created by rayminr on 11/1/15.
 */
public class AppException extends RuntimeException {

    private String code;
    private String message;

    public AppException() {
        super();
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(Throwable cause) {
        super(cause);
    }

    public AppException(String code, String message) {
        super(message);

        this.code = code;
        this.message = message;
    }

    public AppException(String code, String message, Throwable cause) {
        super(message, cause);

        this.code = code;
        this.message = message;
    }

}
