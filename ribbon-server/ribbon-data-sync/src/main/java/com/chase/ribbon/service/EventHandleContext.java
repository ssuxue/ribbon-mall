package com.chase.ribbon.service;

import com.chase.ribbon.event.EventInfo;

/**
 * @version 1.0
 * @Description 事件处理上下文对象 -- 策略模式
 * @Author chase
 * @Date 2020/10/23 13:56
 */
public class EventHandleContext {

    private IEventHandler handler;

    private EventInfo eventInfo;

    public EventHandleContext(IEventHandler handler, EventInfo eventInfo) {
        this.handler = handler;
        this.eventInfo = eventInfo;
    }

    public void setHandler(IEventHandler handler) {
        this.handler = handler;
    }

    public void handle(){
        handler.handle(eventInfo.getRowData());
    }

}
