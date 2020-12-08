package com.chase.ribbon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chase.ribbon.entity.Brand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 品牌表 Mapper 接口
 * </p>
 *
 * @author chase
 * @since 2020-10-18
 */
public interface BrandMapper extends BaseMapper<Brand> {

    /**
     * 根据分类ID查询查询品牌
     * @param id 分类ID
     * @return 品牌
     */
    @Select("SELECT tb.* FROM brand tb, category_brand tcb WHERE tb.id = tcb.brand_id AND tcb.category_id = #{cid}")
    List<Brand> getByCategoryId(@Param("cid") Integer id);
}
