package com.chase.ribbon.domain;

import lombok.Builder;
import lombok.Data;

/**
 * @version 1.0
 * @Description
 * @Author chase
 * @Date 2020/8/27 21:32
 */
@Data
@Builder
public class Content {
    private String title;
    private String img;
    private String price;
}
