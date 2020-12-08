package com.chase.ribbon.controller;

import com.chase.ribbon.common.api.CommonResult;
import com.chase.ribbon.service.SyncDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @version 1.0
 * @Description
 * @Author chase
 * @Date 2020/10/23 22:08
 */
@RestController
@RequestMapping("/synchronize")
@Api(tags = "MySQL与ES同步管理")
public class SyncMysqlDataController {

    @Autowired
    private SyncDataService syncDataService;

    @ApiOperation("同步MySQL数据到ES")
    @GetMapping("/syncMysqlData")
    public CommonResult syncMysqlData() throws IOException {
        Boolean result = syncDataService.syncMysqlData();
        return result ? CommonResult.success(null) : CommonResult.failed();
    }
}
