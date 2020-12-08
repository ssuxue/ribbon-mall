package com.chase.ribbon.service;

import com.alibaba.otter.canal.protocol.CanalEntry;

/**
 * @version 1.0
 * @Description 事件处理抽象类
 * @Author chase
 * @Date 2020/10/23 13:57
 */
public interface IEventHandler {

    void handle(CanalEntry.RowData rowData);

}
