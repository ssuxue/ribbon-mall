package com.chase.ribbon.service.redis;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.chase.ribbon.service.IEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @Description mysql数据新增, redis新增
 * @Author chase
 * @Date 2020/10/23 14:00
 */
@Service
public class RedisAddHandler implements IEventHandler {

    @Autowired
    private RedisHandler redisHandler;

    @Override
    public void handle(CanalEntry.RowData rowData) {
        JSONObject dataJson = redisHandler.dataParse(rowData,true);
        redisHandler.addFromList(dataJson);
    }


}
