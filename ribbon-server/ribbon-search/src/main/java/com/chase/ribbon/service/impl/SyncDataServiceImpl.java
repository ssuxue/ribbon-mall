package com.chase.ribbon.service.impl;

import com.alibaba.fastjson.JSON;
import com.chase.ribbon.common.util.HtmlParseUtil;
import com.chase.ribbon.domain.Content;
import com.chase.ribbon.domain.SkuInfo;
import com.chase.ribbon.entity.Sku;
import com.chase.ribbon.service.SkuService;
import com.chase.ribbon.service.SyncDataService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @Description
 * @Author chase
 * @Date 2020/8/27 21:55
 */
@Service
public class SyncDataServiceImpl implements SyncDataService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private HtmlParseUtil htmlParseUtil;
    @Autowired
    private SkuService skuService;

    @Override
    public Boolean parseContent(String keyword) throws IOException {
        List<Content> contents = htmlParseUtil.parseJingDong(keyword);
        // 把查询到的数据放入到ElasticSearch中
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m");

        for (int i = 0; i < contents.size(); i++) {
            bulkRequest.add(
                    new IndexRequest("ribbon-mall-jk")
                    .source(JSON.toJSONString(contents.get(i)), XContentType.JSON ));
        }

        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }

    @Override
    public List<Map<String, Object>> searchPage(String keyword, int pageNo, int pageSize) throws IOException {
        if(pageNo <= 1) {
            pageNo = 1;
        }

        // 条件搜索
        SearchRequest searchRequest = new SearchRequest("jd_goods");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 分页
        searchSourceBuilder.from(pageNo).size(pageSize);

        // 精准匹配
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", keyword);
        searchSourceBuilder.query(termQueryBuilder).timeout(new TimeValue(60, TimeUnit.SECONDS));

        // 高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title").preTags("<span style='color: red'>").postTags("</span>");

        // 执行搜索
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        //解析结果
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            list.add(hit.getSourceAsMap());
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> searchPageHighlightBuilder(String keyword, int pageNo, int pageSize) throws IOException {
        if(pageNo <= 1) {
            pageNo = 1;
        }

        // 条件搜索
        SearchRequest searchRequest = new SearchRequest("jd_goods");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 分页
        searchSourceBuilder.from(pageNo).size(pageSize);

        // 精准匹配
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", keyword);
        searchSourceBuilder.query(termQueryBuilder).timeout(new TimeValue(60, TimeUnit.SECONDS));

        // 高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title").preTags("<span style='color: red'>").postTags("</span>");
        // 是否开启多个高亮
        highlightBuilder.requireFieldMatch(false);
        searchSourceBuilder.highlighter(highlightBuilder);

        // 执行搜索
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        //解析结果
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField title = highlightFields.get("title");
            // 原来的结果
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();

            // 解析高亮字段
            if (title != null) {
                Text[] fragments = title.fragments();
                StringBuilder sb = new StringBuilder();
                for (Text fragment : fragments) {
                    sb.append(fragment);
                }
                //高亮字段替换原来字段
                sourceAsMap.put("title", sb.toString());
            }

            list.add(sourceAsMap);
        }
        return list;
    }

    @Override
    public Boolean syncMysqlData() throws IOException {
        //1.调用 goods微服务的fegin 查询 符合条件的sku的数据
        List<Sku> skuList = skuService.list();
        //将sku的列表 转换成es中的skuinfo的列表
        List<SkuInfo> skuInfos = JSON.parseArray(JSON.toJSONString(skuList), SkuInfo.class);
        for (SkuInfo skuInfo : skuInfos) {
            // 获取规格的数据  {"电视音响效果":"立体声","电视屏幕尺寸":"20英寸","尺码":"165"}

            // 转成MAP  key: 规格的名称  value:规格的选项的值
            Map<String, Object> map = JSON.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(map);
        }

        // 把查询到的数据放入到ElasticSearch中
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m");

        for (int i = 0; i < skuList.size(); i++) {
            bulkRequest.add(
                    new IndexRequest("ribbon-mall")
                            .source(JSON.toJSONString(skuList.get(i)), XContentType.JSON ));
        }

        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }

}
