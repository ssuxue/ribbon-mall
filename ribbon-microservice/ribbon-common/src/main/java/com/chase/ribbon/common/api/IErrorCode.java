package com.chase.ribbon.common.api;

/**
 *@Description 封装API的错误码
 * @Author chase
 * @Date 2020/7/27
 */
public interface IErrorCode {
    long getCode();

    String getMessage();
}
