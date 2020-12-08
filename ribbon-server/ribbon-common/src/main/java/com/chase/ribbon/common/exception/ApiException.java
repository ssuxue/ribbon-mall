package com.chase.ribbon.common.exception;

import com.chase.ribbon.common.api.IErrorCode;

/**
 * @version 1.0
 * @Description 自定义API异常
 * @Author chase
 * @Date 2020/9/7 17:03
 */
public class ApiException extends RuntimeException {
    private IErrorCode errorCode;

    public ApiException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}
