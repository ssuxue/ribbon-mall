package com.chase.ribbon.service;

import com.chase.ribbon.dto.OssCallbackResult;
import com.chase.ribbon.dto.OssPolicyResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @version 1.0
 * @Description oss上传管理Service
 * @Author chase
 * @Date 2020/9/19 9:05
 */
public interface OssService {

    /**
     * oss上传策略生成
     * @return OSS上传文件授权返回结果
     */
    OssPolicyResult policy();

    /**
     * oss上传成功回调
     * @param request
     * @return @return oss上传文件的回调结果
     */
    OssCallbackResult callback(HttpServletRequest request);

    /**
     * 上传文件
     * @param file 上传的文件
     * @throws IOException
     */
    void upload(MultipartFile file) throws IOException;

    /**
     * 上传文件
     * @param file 指定上传文件的路径上传
     * @param uploadToDirName 上传的文件名
     */
    void upload(MultipartFile file, String uploadToDirName) throws IOException;

    /**
     * 流式下载文件
     * @param objectName @param objectName 包含文件后缀在内的完整路径
     * @throws IOException
     */
    void download(String objectName) throws IOException;

    /**
     * 列举指定目录下所有文件
     * @param directory 指定目录名称
     * @return 文件列表
     */
    List<String> listFilesByDirectory(String directory);

    /**
     * 删除单个文件或文件夹
     * 如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
     * @param objectName 文件全路径名或文件夹全路径名
     * @return 删除结果：1 -> 成功 -- 0 -> 失败
     */
    int delete(String objectName);

    /**
     * 删除指定前缀的文件
     * @param prefix 前缀名
     */
    void deletePrefixFile(String prefix);
}
