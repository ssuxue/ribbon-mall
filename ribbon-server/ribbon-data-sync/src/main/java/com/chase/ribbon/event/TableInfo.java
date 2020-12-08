package com.chase.ribbon.event;

import lombok.Getter;

/**
 * @version 1.0
 * @Description
 * @Author chase
 * @Date 2020/10/23 13:53
 */
@Getter
public class TableInfo {

    /**
     * 数据库
     */
    private String schemaName;

    /**
     * 表名
     */
    private String tableName;

    public TableInfo(String schemaName, String tableName) {
        this.schemaName = schemaName;
        this.tableName = tableName;
    }
}
