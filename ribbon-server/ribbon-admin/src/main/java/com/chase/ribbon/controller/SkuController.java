package com.chase.ribbon.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chase.ribbon.common.api.CommonResult;
import com.chase.ribbon.entity.Sku;
import com.chase.ribbon.service.SkuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author chase
 * @since 2020-10-18
 */
@RestController
@RequestMapping("/sku")
@Api(tags = "商品SKU管理")
public class SkuController {

    @Autowired
    private SkuService skuService;

    @ApiOperation("通过条件实现SKU分页查询")
    @PostMapping("/search/{page}/{size}")
    public CommonResult getPage(@RequestBody(required = false) Sku sku,
                                @PathVariable int current,
                                @PathVariable int size) {
        QueryWrapper<Sku> queryWrapper = new QueryWrapper<>(sku);
        Page<Sku> page = new Page<>(current, size);
        IPage<Sku> iPage = skuService.page(page, queryWrapper);
        return CommonResult.success(iPage);
    }

    @ApiOperation("SKU分页查询")
    @GetMapping("/search/{current}/{size}")
    public CommonResult getPage(@PathVariable int current, @PathVariable int size) {
        Page<Sku> page = new Page<>(current, size);
        IPage<Sku> iPage = skuService.lambdaQuery().page(page);
        return CommonResult.success(iPage);
    }

    @ApiOperation("通过条件查询SKU信息")
    @PostMapping("/search")
    public CommonResult getList(@RequestBody(required = false) Sku sku) {
        return CommonResult.success(skuService.getOne(new QueryWrapper<>(sku)));
    }

    @ApiOperation("通过ID删除SKU")
    @DeleteMapping("/{id}")
    public CommonResult delete(@PathVariable Long id) {
        boolean result = skuService.removeById(id);
        if (result) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("删除失败");
    }

    @ApiOperation("通过ID修改SKU信息")
    @PutMapping("/{id}")
    public CommonResult update(@RequestBody Sku sku, @PathVariable Long id) {
        sku.setId(id);

        boolean result = skuService.updateById(sku);
        if (result) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("修改失");
    }

    @ApiOperation("添加SKU信息")
    @PostMapping
    public CommonResult add(@RequestBody Sku sku) {
        boolean result = skuService.save(sku);
        if (result) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("添加失败");
    }

    @ApiOperation("通过ID查询SKU")
    @GetMapping("/{id}")
    public CommonResult getById(@PathVariable Long id) {
        return CommonResult.success(skuService.getById(id));
    }

    @ApiOperation("查询所有SKU")
    @GetMapping
    public CommonResult getAll(){
        return CommonResult.success(skuService.list()) ;
    }
}

