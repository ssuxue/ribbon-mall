package com.chase.ribbon.event;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.chase.ribbon.service.EventHandlerFactory;

/**
 * @version 1.0
 * @Description mysql事件变换行为
 * @Author chase
 * @Date 2020/10/23 13:52
 */
public class EventInfo{

    /**
     * 行数据
     */
    private CanalEntry.RowData rowData;

    /**
     * 数据库信息
     */
    private TableInfo tableInfo;

    /**
     * mysql里事件类型
     */
    private CanalEntry.EventType eventType;

    public EventInfo(CanalEntry.RowData rowData,String schemaName,String tableName,CanalEntry.EventType eventType) {
        this.rowData = rowData;
        this.tableInfo = new TableInfo(schemaName,tableName);
        this.eventType = eventType;
    }

    public CanalEntry.RowData getRowData() {
        return rowData;
    }

    public CanalEntry.EventType getEventType() {
        return eventType;
    }

    /**
     *  数据库+表名+mysql里事件类型 组合成一个唯一的key, 可以对应为一个EventHandler
     * @return
     */
    public String getUnionKey(){
        return EventHandlerFactory.createUnionKey(tableInfo,eventType);
    }

}
