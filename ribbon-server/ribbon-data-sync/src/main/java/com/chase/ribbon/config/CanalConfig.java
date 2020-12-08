package com.chase.ribbon.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @Description canal配置信息
 * @Author chase
 * @Date 2020/10/23 14:05
 */
@Setter
@Component
@ConfigurationProperties(prefix ="canal")
public class CanalConfig {

    private String host;

    private Integer port;

    private String destination;

    private String userName;

    private String password;

    private Integer batchSize;

    private String filter;

    private Boolean custom;

    public String getHost() {
        return host == null ? CanalConstants.DEFAULT_HOST : host;
    }

    public Integer getPort() {
        return port == null ? CanalConstants.DEFAULT_PORT : port;
    }

    public String getDestination() {
        return destination == null ? CanalConstants.DEFAULT_DESTINATION : destination;
    }

    public String getUserName() {
        return userName == null ? CanalConstants.DEFAULT_USERNAME : userName;
    }

    public String getPassword() {
        return password == null ? CanalConstants.DEFAULT_PASSWORD : password;
    }

    public Integer getBatchSize() {
        return batchSize == null ? CanalConstants.DEFAULT_BATCHSIZE : batchSize;
    }

    public String getFilter() {
        return filter == null ? CanalConstants.DEFAULT_FILTER : filter;
    }

    public Boolean getCustom() {
        return custom == null ? CanalConstants.DEFAULT_CUSTOM : custom;
    }

}
