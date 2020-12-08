package com.chase.ribbon.entity;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
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
@ApiModel(value="Spu对象", description="")
public class Spu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "SPU名")
    @TableField(condition = SqlCondition.LIKE)
    private String name;

    @ApiModelProperty(value = "副标题")
    @TableField(condition = SqlCondition.LIKE)
    private String subtitle;

    @ApiModelProperty(value = "品牌ID")
    private Integer brandId;

    @ApiModelProperty(value = "一级分类")
    private Integer category1Id;

    @ApiModelProperty(value = "二级分类")
    private Integer category2Id;

    @ApiModelProperty(value = "三级分类")
    private Integer category3Id;

    @ApiModelProperty(value = "模板ID")
    private Integer templateId;

    @ApiModelProperty(value = "运费模板id")
    private Integer freightId;

    @ApiModelProperty(value = "图片")
    private String image;

    @ApiModelProperty(value = "图片列表")
    private String images;

    @ApiModelProperty(value = "售后服务")
    private String afterSales;

    @ApiModelProperty(value = "介绍")
    private String introduction;

    @ApiModelProperty(value = "规格列表")
    private String specItems;

    @ApiModelProperty(value = "参数列表")
    private String paraItems;

    @ApiModelProperty(value = "销量")
    private Integer saleQuantity;

    @ApiModelProperty(value = "评论数")
    private Integer commentQuantity;

    @ApiModelProperty(value = "是否上架")
    private String isMarketable;

    @ApiModelProperty(value = "是否启用规格")
    private String isEnableSpec;

    @ApiModelProperty(value = "是否删除")
    private String isDelete;

    @ApiModelProperty(value = "审核状态")
    private String status;


}
