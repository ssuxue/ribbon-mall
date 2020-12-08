package com.chase.ribbon.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version 1.0
 * @Description 配置OSS的连接客户端OSSClient
 * @Author chase
 * @Date 2020/9/19 8:51
 */
@Configuration
public class OssConfig {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId ;
    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret ;

    /**
     * 把ossClient的实例化交给spring容器来做
     * 不调这个shutdown()有没有关系
     * 因为当有ossClient实例的时候 spring就从容器拿 没有才new
     * @return ossClient
     */
    @Bean
    public OSS ossClient(){
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

}
