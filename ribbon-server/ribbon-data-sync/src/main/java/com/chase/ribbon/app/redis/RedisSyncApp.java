package com.chase.ribbon.app.redis;

import com.chase.ribbon.app.DataSyncApp;
import com.chase.ribbon.config.CanalConfig;
import com.chase.ribbon.event.EventInfo;
import com.chase.ribbon.service.EventHandlerFactory;
import com.chase.ribbon.service.IEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @Description 数据同步到redis
 * @Author chase
 * @Date 2020/10/23 14:20
 */
@Component
public class RedisSyncApp extends DataSyncApp {

    @Autowired
    private EventHandlerFactory factory;

    @Autowired
    private CanalConfig canalConfig;

    @Override
    public IEventHandler createHandlerByEventType(EventInfo info) {
        Boolean custom = canalConfig.getCustom();
        if (custom){
            // 自定义的表处理 , 即不同表有不同的处理逻辑, 也有可能共用一个处理逻辑
            return factory.getHandler(info.getUnionKey());
        }else{
            // 全局的处理
            return factory.getHandler(EventHandlerFactory.createUnionKey(info.getEventType()));
        }
    }

}
