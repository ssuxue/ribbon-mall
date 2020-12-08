package com.chase.ribbon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chase.ribbon.entity.Brand;

import java.util.List;

/**
 * <p>
 * 品牌表 服务类
 * </p>
 *
 * @author chase
 * @since 2020-10-18
 */
public interface BrandService extends IService<Brand> {

    /**
     * 根据分类ID查询品牌
     * @param cid 分类ID
     * @return 品牌
     */
    List<Brand> getByCategoryId(Integer cid);
}
