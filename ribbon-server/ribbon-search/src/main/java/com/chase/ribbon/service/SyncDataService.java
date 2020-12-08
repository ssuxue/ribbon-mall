package com.chase.ribbon.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @Description
 * @Author chase
 * @Date 2020/8/27 21:53
 */
public interface SyncDataService {

    /**
     * 解析数据放入到ElasticSearch索引中
     * @param keyword 搜索关键字
     * @return
     */
    Boolean parseContent(String keyword) throws IOException;

    /**
     * 分页搜索数据
     * @param keyword 搜索关键词
     * @param pageNo 开始页
     * @param pageSize 每页大小
     * @return
     */
    List<Map<String, Object>> searchPage(String keyword, int pageNo, int pageSize) throws IOException;

    /**
     * 分页搜索数据--高亮关键字
     * @param keyword 搜索关键词
     * @param pageNo 开始页
     * @param pageSize 每页大小
     * @return
     */
    List<Map<String, Object>> searchPageHighlightBuilder(String keyword, int pageNo, int pageSize) throws IOException;

    /**
     * 同步MySQL数据到ES
     * @return 同步结果 true -> 成功 :: false -> 失败
     * @throws IOException
     */
    Boolean syncMysqlData() throws IOException;
}
