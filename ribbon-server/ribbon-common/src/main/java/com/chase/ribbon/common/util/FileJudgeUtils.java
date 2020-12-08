package com.chase.ribbon.common.util;

import com.chase.ribbon.common.api.FileType;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @version 1.0
 * @Description 判断文件类型工具类
 * @Author chase
 * @Date 2020/9/20 13:36
 */
@Component
public class FileJudgeUtils {

    /** 判断文件类型  */
    public FileType getType(byte[] filePath) throws IOException {
        // 获取文件头
        String fileHead = getFileHeader(filePath);

        if (fileHead != null && fileHead.length() > 0) {
            fileHead = fileHead.toUpperCase();
            FileType[] fileTypes = FileType.values();

            for (FileType type : fileTypes) {
                if (fileHead.startsWith(type.getValue())) {
                    return type;
                }
            }
        }

        return null;
    }

    /** 读取文件头 */
    private String getFileHeader(byte[] fileBytes) {
        return bytesToHex(fileBytes);
    }

    /** 将字节数组转换成16进制字符串 */
    public String bytesToHex(byte[] src){
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /** 获取文件根目录 **/
    public String getFileRoot(FileType fileType) {
        // TODO 可以利用单例+策略模式进一步优化
        if (fileType.getCode() == 0) {
            return "images";
        }
        if (fileType.getCode() == 1) {
            return "multimedia";
        }
        return "other";
    }
}