package com.chase.ribbon.service.redis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.chase.ribbon.redis.util.RedisUtil;
import com.chase.ribbon.service.DataParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Objects;

/**
 * @version 1.0
 * @Description redis通用操作
 *              通过@Configuration注入到项目里去
 * @Author chase
 * @Date 2020/10/23 14:01
 */
@Component
public class RedisHandler {

    private static Logger logger = LoggerFactory.getLogger(RedisHandler.class);

    @Autowired
    private DataParser dataParser;

    @Value("${redis.table.key}")
    private String redisTableKey;

    @Autowired
    private RedisUtil redisUtil;

    private boolean hasKey() {
        return redisUtil.haskey(redisTableKey);
    }

    /**
     * 首次添加
     *
     * @param jsonObject
     * @return
     */
    private void firstAdd(JSONObject jsonObject) {
        JSONArray arr = new JSONArray(1);
        arr.add(jsonObject);
        redisUtil.saveBean(redisTableKey, arr);
        logger.info("redis首次插入数据成功........... {}", jsonObject);
    }


    public JSONObject dataParse(CanalEntry.RowData rowData, boolean after) {
        return dataParser.parse(rowData,after);
    }

    /**
     * 列表的删除或修改
     *
     * @param update
     * @param dataJson
     * @param updateColoum
     */
    private void updateOrDeleteFromList(boolean update, JSONObject dataJson, String updateColoum) {
        JSONArray arrBean;
        if (hasKey()) {
            arrBean = redisUtil.getArrBean(redisTableKey);
            logger.info("修改或删除redis数据前为..........{}",arrBean);
            //修改的主键
            Object updateValue = dataJson.get(updateColoum);
            //iterator 遍历删除
            Iterator<Object> iterator = arrBean.iterator();
            while(iterator.hasNext()) {
                Object next = iterator.next();
                JSONObject jsonObject = (JSONObject) next;
                if (Objects.equals(updateValue, jsonObject.get(updateColoum))) {
                    //iterator 删除
                    iterator.remove();
                }
            }
            //删除还是修改
            if (update){
                arrBean.add(dataJson);
                logger.info("redis数据修改成功........... , {}",dataJson);
            }else{
                logger.info("redis数据删除成功..........., {}",dataJson);
            }
            redisUtil.saveBean(redisTableKey, arrBean);
        }
//        else {
//            //首次redis 无数据的情况
//            firstAdd(dataJson);
//        }
    }

    /**
     * 修改数据对象列表
     *
     * @param dataJson
     * @param updateColoum 根据哪一列修改
     */
    public void updateFromListByKey(JSONObject dataJson, String updateColoum) {
        updateOrDeleteFromList(true, dataJson, updateColoum);
    }

    /**
     * 增加一个对象到列表
     *
     * @param dataJson
     */
    public void addFromList(JSONObject dataJson) {
        JSONArray arrBean;
        if (hasKey()) {
            arrBean = redisUtil.getArrBean(redisTableKey);
            arrBean.add(dataJson);
            redisUtil.saveBean(redisTableKey, arrBean);
            logger.info("redis数据增加成功..........., {}", dataJson);
        } else {
            //首次redis 无数据的情况
            firstAdd(dataJson);
        }
    }

    /**
     * 删除某一行数据
     *
     * @param dataJson
     * @param updateColoum 根据哪一列修改
     */
    public void deleteFromList(JSONObject dataJson, String updateColoum) {
        updateOrDeleteFromList(false, dataJson, updateColoum);
    }


}
