package com.chase.ribbon.common.api;

/**
 * @version 1.0
 * @Description 枚举一些文件类型  0:图片 -- 1：音视频
 * @Author chase
 * @Date 2020/9/20 13:30
 */
public enum FileType {

    /** JPEG */
    JPEG("FFD8FF", 0),

    /** PNG */
    PNG("89504E47", 0),

    /** GIF */
    GIF("47494638", 0),

    /** TIFF */
    TIFF("49492A00", 0),

    /** Windows bitmap */
    BMP("424D", 0),

    /** Adobe photoshop */
    PSD("38425053", 0),

    /** WAVE */
    WAV("57415645", 1),

    /** AVI */
    AVI("41564920", 1),

    /** Real Audio */
    RAM("2E7261FD", 1),

    /** Real Media */
    RM("2E524D46", 1),

    /** Quicktime */
    MOV("6D6F6F76", 1),

    /** Windows Media */
    ASF("3026B2758E66CF11", 1),

    /** MIDI */
    MID("4D546864", 1);

    private String value;
    private int code;

    FileType(String value, int code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }
}
