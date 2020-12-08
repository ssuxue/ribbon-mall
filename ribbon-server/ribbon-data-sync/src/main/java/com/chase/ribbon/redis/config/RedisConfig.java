package com.chase.ribbon.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @version 1.0
 * @Description
 * @Author chase
 * @Date 2020/10/23 14:14
 */
@Configuration
public class RedisConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisTemplate myRedisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        // 设置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // key-value 序列化设置
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new FastJson2JsonRedisSerializer<>(String.class));
        redisTemplate.setHashValueSerializer(new FastJson2JsonRedisSerializer<>(String.class));
        return redisTemplate;
    }
}
