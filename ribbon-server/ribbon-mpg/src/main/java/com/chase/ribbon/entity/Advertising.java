package com.chase.ribbon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author chase
 * @since 2020-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Advertising对象", description="广告")
public class Advertising implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "内容类目ID")
    private Long categoryId;

    @ApiModelProperty(value = "内容标题")
    @TableField(condition = SqlCondition.LIKE)
    private String title;

    @ApiModelProperty(value = "链接")
    private String url;

    @ApiModelProperty(value = "图片绝对路径")
    private String img;

    @ApiModelProperty(value = "状态,0无效，1有效")
    private String status;

    @ApiModelProperty(value = "排序")
    private Integer sortOrder;


}
