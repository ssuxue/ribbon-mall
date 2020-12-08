package com.chase.ribbon.common.style;

import lombok.Getter;

/**
 * @version 1.0
 * @Description 商品大下枚举类
 * @Author chase
 * @Date 2020/9/2 12:09
 */
@Getter
public enum SizeEnum {
    EXTRA_SMALL(1, "XS"),
    SMALL(2, "S"),
    MIDDLE(3, "M"),
    LARGE(4, "L"),
    EXTRA_LARGE(5, "XL"),
    EXTRA_EXTRA_LARGE(6, "2XL");

    private int code;
    private String size;

    private SizeEnum(int code, String size) {
        this.code = code;
        this.size = size;
    }
}
