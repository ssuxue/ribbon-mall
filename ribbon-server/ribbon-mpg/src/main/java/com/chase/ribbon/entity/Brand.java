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
 * 品牌表
 * </p>
 *
 * @author chase
 * @since 2020-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Brand对象", description="品牌表")
public class Brand implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "品牌id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "品牌名称")
    @TableField(condition = SqlCondition.LIKE)
    private String name;

    @ApiModelProperty(value = "品牌图片地址")
    private String image;

    @ApiModelProperty(value = "品牌的首字母")
    private String initial;


}
