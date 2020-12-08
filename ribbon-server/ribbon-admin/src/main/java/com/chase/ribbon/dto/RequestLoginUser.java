package com.chase.ribbon.dto;

import com.sun.istack.internal.NotNull;
import lombok.Data;

/**
 * @version 1.0
 * @Description
 * @Author chase
 * @Date 2020/10/18 15:55
 */
@Data
public class RequestLoginUser {

    @NotNull
    private String username;
    @NotNull
    private String password;

}