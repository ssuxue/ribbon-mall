package com.chase.ribbon.app;

import com.chase.ribbon.event.EventInfo;
import com.chase.ribbon.service.IEventHandler;

/**
 * @version 1.0
 * @Description 根据数据库+表名+事件类型 产生handler 抽象接口层
 * @Author chase
 * @Date 2020/10/23 13:44
 */
public interface ActionByEventType {

    IEventHandler createHandlerByEventType(EventInfo info);
}
