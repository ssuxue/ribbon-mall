package com.chase.ribbon.controller;


import com.alibaba.fastjson.JSON;
import com.chase.ribbon.common.api.CommonResult;
import com.chase.ribbon.entity.Advertising;
import com.chase.ribbon.service.AdvertisingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chase
 * @since 2020-10-18
 */
@RestController
@RequestMapping("/advertising")
@Api(tags = "首页广告管理")
public class AdvertisingController {

    @Autowired
    private AdvertisingService advertisingService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation("根据分类ID获取到广告列表")
    @GetMapping("/list/category/{id}")
    public CommonResult getByCategoryId(@PathVariable("id") Long id) {

        /**
         * 如果redis缓存中有就从redis中取，没有再从mysql中取
         * 这里用来替代citrus中的OpenResty和Lua实现的广告换成
         * 并且这是相对于citrus中的nginx, redis和mysql实现的广告缓存与同步实在是low
         * 没办法因为我不知道在SpringBoot中怎么使用Feigin或者说我不知道怎么怎么替代
         */
        String content = stringRedisTemplate.opsForValue().get("content_" + id);
        if (content != null) {
            return CommonResult.success(JSON.parseArray(content));
        }

        return CommonResult.success(advertisingService.lambdaQuery()
                .eq(Advertising::getCategoryId, id)
                .eq(Advertising::getStatus, 1)
                .list());
    }
}

