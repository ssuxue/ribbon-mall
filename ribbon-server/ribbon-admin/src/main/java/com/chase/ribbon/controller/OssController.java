package com.chase.ribbon.controller;

import com.chase.ribbon.common.api.CommonResult;
import com.chase.ribbon.dto.OssCallbackResult;
import com.chase.ribbon.dto.OssPolicyResult;
import com.chase.ribbon.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @version 1.0
 * @Description
 * @Author chase
 * @Date 2020/10/18 16:11
 */
@RestController
@Api(tags = "OSS管理")
@RequestMapping("/aliyun/oss")
public class OssController {

    @Autowired
    private OssService ossService;

    @ApiOperation(value = "oss上传签名生成")
    @GetMapping("/policy")
    public CommonResult<OssPolicyResult> policy() {
        OssPolicyResult result = ossService.policy();
        return CommonResult.success(result);
    }

    @ApiOperation(value = "oss上传成功回调")
    @PostMapping("/callback")
    public CommonResult<OssCallbackResult> callback(HttpServletRequest request) {
        OssCallbackResult ossCallbackResult = ossService.callback(request);
        return CommonResult.success(ossCallbackResult);
    }

    @ApiOperation(value = "oss列举指定目录下所有文件")
    @GetMapping("/list/{directory}")
    public CommonResult<List<String>> listFiles(@PathVariable("directory") String directory) {
        List<String> list = ossService.listFilesByDirectory(directory);
        return CommonResult.success(list);
    }

    @ApiOperation(value = "oss删除指定文件或文件夹")
    @DeleteMapping("/delete")
    public CommonResult delete(@RequestParam("objectName") String objectName) {
        int delete = ossService.delete(objectName);
        if (delete > 0) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("删除失败");
    }

    @ApiOperation(value = "oss删除指定目录")
    @DeleteMapping("/deleteDirectory")
    public CommonResult deleteDirectory(@RequestParam("directory") String directory) {
        ossService.deletePrefixFile(directory);
        return CommonResult.success(null);
    }

    @ApiOperation(value = "oos上传文件")
    @PostMapping(value = "/upload", headers="content-type=multipart/form-data")
    public CommonResult upload(@RequestParam("file") MultipartFile file) throws IOException {
        ossService.upload(file);
        return CommonResult.success(null);
    }

    @ApiOperation(value = "oos指定上传路径进行文件上传")
    @PostMapping(value = "/upload/{uploadToDirName}", headers="content-type=multipart/form-data")
    public CommonResult upload(@RequestParam("file") MultipartFile file,
                               @PathVariable("uploadToDirName") String uploadToDirName) throws IOException {
        ossService.upload(file, uploadToDirName);
        return CommonResult.success(null);
    }

    @ApiOperation(value = "下载文件")
    @GetMapping("/download")
    public CommonResult upload(@RequestParam("objectName") @ApiParam("文件名") String objectName) throws IOException {
        ossService.download(objectName);
        return CommonResult.success(null);
    }
}
