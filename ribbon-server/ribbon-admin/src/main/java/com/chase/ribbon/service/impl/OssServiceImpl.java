package com.chase.ribbon.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import com.chase.ribbon.common.api.FileType;
import com.chase.ribbon.common.util.FileJudgeUtils;
import com.chase.ribbon.dto.OssCallbackParam;
import com.chase.ribbon.dto.OssCallbackResult;
import com.chase.ribbon.dto.OssPolicyResult;
import com.chase.ribbon.service.OssService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @version 1.0
 * @Description
 * @Author chase
 * @Date 2020/9/19 9:08
 */
@Service
public class OssServiceImpl implements OssService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OssServiceImpl.class);
    @Value("${aliyun.oss.policy.expire}")
    private int ossExpire;
    @Value("${aliyun.oss.maxSize}")
    private int ossMaxSize;
    @Value("${aliyun.oss.callback}")
    private String ossCallback;
    @Value("${aliyun.oss.bucketName}")
    private String ossBucketName;
    @Value("${aliyun.oss.endpoint}")
    private String ossEndPoint;
    @Value("${aliyun.oss.dir.prefix}")
    private String ossDirectoryPrefix;

    @Autowired
    private OSS ossClient;
    @Autowired
    private FileJudgeUtils fileJudgeUtils;

    /**
     * 签名生成
     */
    @Override
    public OssPolicyResult policy() {
        OssPolicyResult result = new OssPolicyResult();
        // 存储目录
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dir = ossDirectoryPrefix+sdf.format(new Date());
        // 签名有效期
        long expireEndTime = System.currentTimeMillis() + ossExpire * 1000;
        Date expiration = new Date(expireEndTime);
        // 文件大小
        long maxSize = ossMaxSize * 1024 * 1024;
        // 回调
        OssCallbackParam callback = new OssCallbackParam();
        callback.setCallbackUrl(ossCallback);
        callback.setCallbackBody("filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
        callback.setCallbackBodyType("application/x-www-form-urlencoded");
        // 提交节点
        String action = "http://" + ossBucketName + "." + ossEndPoint;
        try {
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, maxSize);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String policy = BinaryUtil.toBase64String(binaryData);
            String signature = ossClient.calculatePostSignature(postPolicy);
            System.out.println(JSON.toJSONString(callback));
            System.out.println();
            String callbackData = BinaryUtil.toBase64String(JSON.toJSONString(callback).getBytes(StandardCharsets.UTF_8));
            // 返回结果
            result.setAccessKeyId(null);
            result.setPolicy(policy);
            result.setSignature(signature);
            result.setDir(dir);
            result.setCallback(callbackData);
            result.setHost(action);
        } catch (Exception e) {
            LOGGER.error("签名生成失败", e);
        }
        return result;
    }

    @Override
    public OssCallbackResult callback(HttpServletRequest request) {
        OssCallbackResult result= new OssCallbackResult();

        String filename = request.getParameter("filename");
        filename = "http://".concat(ossBucketName).concat(".").concat(ossEndPoint).concat("/").concat(filename);
        result.setFilename(filename);
        result.setSize(request.getParameter("size"));
        result.setMimeType(request.getParameter("mimeType"));
        result.setWidth(request.getParameter("width"));
        result.setHeight(request.getParameter("height"));
        return result;
    }

    @Override
    public void upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        // TODO 根据文件类型选择上传文件根目录  例如图片就是 images/  待优化
        FileType fileType = fileJudgeUtils.getType(file.getBytes());
        String fileRoot = fileJudgeUtils.getFileRoot(fileType);

        SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd");
        String format = fileRoot + sdf.format(new Date());

        String objectName = format + "/" + UUID.randomUUID().toString().replace("-", "") + "." + suffix;

        PutObjectRequest putObjectRequest = new PutObjectRequest(ossBucketName, objectName, new ByteArrayInputStream(file.getBytes()));

        // 如果需要上传时设置存储类型与访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);

        ossClient.putObject(putObjectRequest);
    }

    @Override
    public void upload(MultipartFile file, String uploadToDirName) throws IOException {
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String suffix = "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        String objectName = uploadToDirName + "/" + UUID.randomUUID().toString().replace("-", "") + suffix;

        PutObjectRequest putObjectRequest = new PutObjectRequest(ossBucketName, objectName, new ByteArrayInputStream(file.getBytes()));

        // 如果需要上传时设置存储类型与访问权限，请参考上一个方法下的示例代码。

        ossClient.putObject(putObjectRequest);
    }

    @Override
    public void download(String objectName) throws IOException {
        // TODO 下载没错但也本地页没下载  待解决
        //  可以在前台利用下载跳转到URL进行下载
        //  比如 https://chase-oss1.oss-cn-beijing.aliyuncs.com/images//2020/09/2019fc2abe7ba44f129872deb768092e81.jpg
        OSSObject ossObject = ossClient.getObject(ossBucketName, objectName);
        InputStream content = ossObject.getObjectContent();
        if (content != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
            }
            content.close();
        }
    }

    /**
     * 判断文件是否存在
     * @param objectName 文件路径名
     * @return true -> 存在 -- false -> 不存在
     */
    public boolean isExist(String objectName) {
        return ossClient.doesObjectExist(ossBucketName, objectName);
    }

    @Override
    public List<String> listFilesByDirectory(String directory) {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(ossBucketName);
        listObjectsRequest.setPrefix(directory + "/");

        ObjectListing listing = ossClient.listObjects(listObjectsRequest);

        List<String> list = new ArrayList<>();
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            list.add(objectSummary.getKey());
        }
        return list;
    }

    @Override
    public int delete(String objectName) {
        boolean exist = isExist(objectName);
        if (exist) {
            ossClient.deleteObject(ossBucketName, objectName);
            return 1;
        }
        return 0;
    }

    @Override
    public void deletePrefixFile(String prefix) {
        String nextMarker = null;
        ObjectListing objectListing;
        do {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(ossBucketName)
                    .withPrefix(prefix)
                    .withMarker(nextMarker);
            objectListing = ossClient.listObjects(listObjectsRequest);
            if (objectListing.getObjectSummaries().size() > 0) {
                List<String> listObject = new ArrayList<>();
                for (OSSObjectSummary s : objectListing.getObjectSummaries()) {
                    if (isExist(s.getKey())) {
                        listObject.add(s.getKey());
                    }
                }
                DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(ossBucketName).withKeys(listObject);
                ossClient.deleteObjects(deleteObjectsRequest);
            }

            nextMarker = objectListing.getNextMarker();
        } while (objectListing.isTruncated());
    }

}
