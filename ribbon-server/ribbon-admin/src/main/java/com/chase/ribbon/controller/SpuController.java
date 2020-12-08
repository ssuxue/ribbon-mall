package com.chase.ribbon.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chase.ribbon.common.api.CommonResult;
import com.chase.ribbon.common.api.SnowFlake;
import com.chase.ribbon.dto.GoodsDTO;
import com.chase.ribbon.entity.Brand;
import com.chase.ribbon.entity.Category;
import com.chase.ribbon.entity.Sku;
import com.chase.ribbon.entity.Spu;
import com.chase.ribbon.service.BrandService;
import com.chase.ribbon.service.CategoryService;
import com.chase.ribbon.service.SkuService;
import com.chase.ribbon.service.SpuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chase
 * @since 2020-10-18
 */
@RestController
@RequestMapping("/spu")
@Api(tags = "商品SPU管理")
public class SpuController {

    @Autowired
    private SpuService spuService;
    @Autowired
    private SkuService sKuService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;

    @ApiOperation("通过条件实现SPU分页查询")
    @PostMapping("/search/{current}/{size}")
    public CommonResult getPage(@RequestBody(required = false) Spu spu,
                                @PathVariable int current,
                                @PathVariable int size) {
        QueryWrapper<Spu> queryWrapper = new QueryWrapper<>(spu);
        Page<Spu> page = new Page<>(current, size);
        IPage<Spu> iPage = spuService.page(page, queryWrapper);
        return CommonResult.success(iPage);
    }

    @ApiOperation("SPU分页查询")
    @GetMapping("/search/{current}/{size}")
    public CommonResult getPage(@PathVariable int current, @PathVariable int size) {
        Page<Spu> page = new Page<>(current, size);
        IPage<Spu> iPage = spuService.lambdaQuery().page(page);
        return CommonResult.success(iPage);
    }

    @ApiOperation("通过条件查询SPU信息")
    @PostMapping("/search")
    public CommonResult getList(@RequestBody(required = false) Spu spu) {
        return CommonResult.success(spuService.getOne(new QueryWrapper<>(spu)));
    }

    @ApiOperation("通过ID删除SPU")
    @DeleteMapping("/{id}")
    public CommonResult delete(@PathVariable Long id) {
        boolean result = spuService.removeById(id);
        if (result) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("删除失败");
    }

    @ApiOperation("通过ID修改SPU信息")
    @PutMapping("/{id}")
    public CommonResult update(@RequestBody Spu spu, @PathVariable Long id) {
        spu.setId(id);

        boolean result = spuService.updateById(spu);
        if (result) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("修改失败");
    }

    @ApiOperation("添加SPU信息")
    @PostMapping
    public CommonResult add(@RequestBody Spu spu) {
        boolean result = spuService.save(spu);
        if (result) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("添加失败");
    }

    @ApiOperation("通过ID查询SPU")
    @GetMapping("/{id}")
    public CommonResult getById(@PathVariable Long id) {
        return CommonResult.success(spuService.getById(id));
    }

    @ApiOperation("查询所有SPU")
    @GetMapping
    public CommonResult getAll(){
        return CommonResult.success(spuService.list()) ;
    }

    @ApiOperation("添加商品")
    @PostMapping("/save")
    public CommonResult saveGoods(@RequestBody GoodsDTO goodsDTO) {
        Spu spu = goodsDTO.getSpu();
        // 添加或者修改
        if (spu.getId() == null) {
            spu.setId(SnowFlake.nextId());
            spuService.save(spu);
        } else {
            spuService.updateById(spu);
            Sku sku = new Sku();
            sku.setSpuId(spu.getId());
            sKuService.removeById(sku.getId());
        }

        LocalDateTime dateTime = LocalDateTime.now();
        Category category = categoryService.getById(spu.getCategory3Id());
        Brand brand = brandService.getById(spu.getBrandId());

        List<Sku> skuList = goodsDTO.getSkuList();
        boolean result = false;
        for (Sku sku : skuList) {
            String name = spu.getName();

            if (StringUtils.isEmpty(sku.getSpecification())) {
                sku.setSpecification("{}");
            }

            Map<String, String> specMap = JSON.parseObject(sku.getSpecification(), Map.class);
            for (Map.Entry<String, String> entry : specMap.entrySet()) {
                name += " " + entry.getValue();
            }

            sku.setId(SnowFlake.nextId());
            sku.setName(name);
            sku.setCreateTime(dateTime);
            sku.setUpdateTime(dateTime);
            sku.setSpuId(spu.getId());
            // 三级分类ID
            sku.setCategoryId(spu.getCategory3Id());
            // 三级分类名
            sku.setCategoryName(category.getName());
            sku.setBrandName(brand.getName());

            result = sKuService.save(sku);
        }
        return result ? CommonResult.success(null) : CommonResult.failed("商品新增失败");
    }

    @ApiOperation("获取商品")
    @GetMapping("/goods/{id}")
    public CommonResult<GoodsDTO> getGoodsById(@PathVariable("id") Long id) {
        Spu spu = spuService.getById(id);
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skuList = sKuService.list(new QueryWrapper<>(sku));

        GoodsDTO goodsDTO = new GoodsDTO();
        goodsDTO.setSpu(spu);
        goodsDTO.setSkuList(skuList);
        return CommonResult.success(goodsDTO);
    }

    @ApiOperation("审核商品")
    @PutMapping("/audit/{id}")
    public CommonResult audit(@PathVariable("id") Long id) {
        // TODO 待优化 将 IsXxx 字段用MP映射成 Xxx
        Spu spu = spuService.getById(id);
        // 判断商品是否符合审核条件
        if ("1".equalsIgnoreCase(spu.getIsDelete()
        )) {
            throw new RuntimeException("该商品已删除！");
        }

        // 审核通过
        spu.setStatus("1");
        // 上架
        spu.setIsMarketable("1");
        spuService.updateById(spu);
        return CommonResult.success(null);
    }

    @ApiOperation("商品上架")
    @PutMapping("/takeOffShelves/{id}")
    public CommonResult takeOffShelves(@PathVariable("id") Long id) {
        // TODO 待优化 将 IsXxx 字段用MP映射成 Xxx
        Spu spu = spuService.getById(id);
        if ("1".equals(spu.getIsDelete())) {
            throw new RuntimeException("不能对已被删除的商品进行下架！！");
        }

        // 下架
        spu.setIsMarketable("0");
        spuService.updateById(spu);
        return CommonResult.success(null);
    }

    @ApiOperation("商品上架")
    @PutMapping("/launch/{id}")
    public CommonResult launch(@PathVariable("id") Long id) {
        // TODO 待优化 将 IsXxx 字段用MP映射成 Xxx
        Spu spu = spuService.getById(id);
        if ("1".equals(spu.getIsDelete())) {
            throw new RuntimeException("不能对已被删除的商品进行下架！！");
        }
        if (!"1".equals(spu.getStatus())) {
            throw new RuntimeException("未通过审核的商品不能上架！");
        }

        // 下架
        spu.setIsMarketable("1");
        spuService.updateById(spu);
        return CommonResult.success(null);
    }

    @ApiOperation("批量上架商品")
    @PutMapping("/batchLaunch")
    public CommonResult batchLaunch(@RequestBody Long[] ids) {
        // TODO 待优化 将 IsXxx 字段用MP映射成 Xxx
        int count = 0;
        for (Long id : ids) {
            Spu spu = spuService.lambdaQuery()
                    .eq(Spu::getId, id)
                    .eq(Spu::getIsDelete, 0)
                    .eq(Spu::getStatus, 1)
                    .one();
            spu.setIsMarketable("1");
            boolean result = spuService.updateById(spu);
            count = result ? (count + 1) : count;
        }

        return (count > 0) ? CommonResult.success(null) : CommonResult.failed("上架失败");
    }
}

