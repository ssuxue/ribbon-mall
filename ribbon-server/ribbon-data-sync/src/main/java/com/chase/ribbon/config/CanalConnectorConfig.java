package com.chase.ribbon.config;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

/**
 * @version 1.0
 * @Description canal连接
 * @Author chase
 * @Date 2020/10/23 14:07
 */
@Configuration
public class CanalConnectorConfig {

    @Autowired
    private CanalConfig canalConfig;

    @Bean
    public CanalConnector initConnector(){
        //目前canal server上的一个instance只能有一个client消费, 当有多个client消费时,会有bug
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(canalConfig.getHost(),
                canalConfig.getPort()), canalConfig.getDestination(), canalConfig.getUserName(), canalConfig.getPassword());
        connector.connect();
        connector.subscribe(canalConfig.getFilter());
        connector.rollback();
        return connector;
    }

}
