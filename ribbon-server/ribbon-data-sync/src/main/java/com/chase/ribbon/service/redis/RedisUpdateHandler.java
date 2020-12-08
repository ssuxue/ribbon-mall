package com.chase.ribbon.service.redis;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.chase.ribbon.service.IEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @Description mysql数据修改 redis修改
 * @Author chase
 * @Date 2020/10/23 14:04
 */
@Service
public class RedisUpdateHandler implements IEventHandler {

    @Autowired
    private RedisHandler redisHandler;

    @Value("${redis.sync.update.coloum}")
    private String updateColoum;


    @Override
    public void handle(CanalEntry.RowData rowData) {
        JSONObject dataJson = redisHandler.dataParse(rowData,true);
        redisHandler.updateFromListByKey(dataJson,updateColoum);
    }


}
