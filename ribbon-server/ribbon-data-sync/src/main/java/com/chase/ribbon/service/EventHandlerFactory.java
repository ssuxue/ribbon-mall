package com.chase.ribbon.service;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.chase.ribbon.config.CanalConstants;
import com.chase.ribbon.event.TableInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @Description 事件处理 工厂
 * @Author chase
 * @Date 2020/10/23 13:56
 */
public class EventHandlerFactory {

    private Map<String,IEventHandler> handlerMap = new HashMap<>();

    public IEventHandler getHandler(String key){
        return handlerMap.get(key);
    }

    public void setHandler(String key,IEventHandler iEventHandler){
        handlerMap.put(key,iEventHandler);
    }

    public static String createUnionKey(TableInfo tableInfo, CanalEntry.EventType eventType){
        return createUnionKey(tableInfo.getSchemaName(), tableInfo.getTableName(), eventType);
    }

    public static String createUnionKey(String schemaName,String tableName,CanalEntry.EventType eventType){
        return schemaName + "-" + tableName + "-" + eventType.getValueDescriptor().getName();
    }

    /**
     * 统一的处理器
     * @param eventType
     * @return
     */
    public static String createUnionKey(CanalEntry.EventType eventType){
        return createUnionKey(CanalConstants.UNITY_SCHEMA,CanalConstants.UNITY_TABLE,eventType);
    }

}
