package com.chase.ribbon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chase.ribbon.entity.Category;
import com.chase.ribbon.mapper.CategoryMapper;
import com.chase.ribbon.service.CategoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品类目 服务实现类
 * </p>
 *
 * @author chase
 * @since 2020-10-18
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

}
