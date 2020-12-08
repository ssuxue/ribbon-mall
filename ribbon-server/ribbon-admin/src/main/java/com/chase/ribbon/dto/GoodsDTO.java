package com.chase.ribbon.dto;

import com.chase.ribbon.entity.Sku;
import com.chase.ribbon.entity.Spu;
import lombok.Data;

import java.util.List;

/**
 * @version 1.0
 * @Description 商品信息封装 SKU＋SPU
 * @Author chase
 * @Date 2020/9/2 13:11
 */
@Data
public class GoodsDTO {

    private Spu spu;
    private List<Sku> skuList;
    public void setSpu(Spu spu) {
        this.spu = spu;
    }
    public void setSkuList(List<Sku> skuList) {
        this.skuList = skuList;
    }
    public Spu getSpu() {
        return spu;
    }
    public List<Sku> getSkuList() {
        return skuList;
    }
}
