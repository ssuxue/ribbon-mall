package com.chase.ribbon.entity;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author chase
 * @since 2020-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Sku对象", description="商品表")
public class Sku implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id")
    private Long id;

    @ApiModelProperty(value = "SKU名称")
    @TableField(condition = SqlCondition.LIKE)
    private String name;

    @ApiModelProperty(value = "价格（分）")
    private Integer price;

    @ApiModelProperty(value = "库存数量")
    private Integer inventory;

    @ApiModelProperty(value = "库存预警数量")
    private Integer alertNum;

    @ApiModelProperty(value = "商品图片")
    private String image;

    @ApiModelProperty(value = "商品图片列表")
    private String images;

    @ApiModelProperty(value = "重量（克）")
    private Integer weight;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "SPUID")
    private Long spuId;

    @ApiModelProperty(value = "类目ID")
    private Integer categoryId;

    @ApiModelProperty(value = "类目名称")
    private String categoryName;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "规格")
    private String specification;

    @ApiModelProperty(value = "销量")
    private Integer saleQuantity;

    @ApiModelProperty(value = "评论数")
    private Integer commentQuantity;

    @ApiModelProperty(value = "商品状态 1-正常，2-下架，3-删除")
    private String status;


}
