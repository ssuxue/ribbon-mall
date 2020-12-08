package com.chase.ribbon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chase.ribbon.entity.Brand;
import com.chase.ribbon.mapper.BrandMapper;
import com.chase.ribbon.service.BrandService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 品牌表 服务实现类
 * </p>
 *
 * @author chase
 * @since 2020-10-18
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

    @Override
    public List<Brand> getByCategoryId(Integer cid) {
        return this.baseMapper.getByCategoryId(cid);
    }
}
