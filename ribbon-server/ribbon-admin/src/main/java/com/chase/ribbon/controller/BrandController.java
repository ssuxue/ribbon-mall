package com.chase.ribbon.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chase.ribbon.common.api.CommonResult;
import com.chase.ribbon.entity.Brand;
import com.chase.ribbon.service.BrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 品牌表 前端控制器
 * </p>
 *
 * @author chase
 * @since 2020-10-18
 */
@RestController
@RequestMapping("/brand")
@Api(tags = "品牌管理")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @ApiOperation("获取所有品牌")
    @GetMapping("/getAll")
    public CommonResult getAll() {
        return CommonResult.success(brandService.list());
    }

    @ApiOperation("通过ID获取品牌")
    @GetMapping("/getById/{id}")
    public CommonResult getById(@PathVariable("id") Integer id) {
        return CommonResult.success(brandService.getById(id));
    }

    @ApiOperation("添加品牌")
    @PostMapping("/add")
    public CommonResult add(@RequestBody Brand brand) {
        boolean result = brandService.save(brand);
        if (result) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("添加失败");
    }

    @ApiOperation("修改品牌")
    @PutMapping("/update/{id}")
    public CommonResult update(@PathVariable("id")Integer id, @RequestBody Brand brand) {
        brand.setId(id);
        boolean update = brandService.updateById(brand);
        if (update) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("修改失败");
    }

    @ApiOperation("删除品牌")
    @DeleteMapping("/delete/{id}")
    public CommonResult delete(@PathVariable("id") Integer id) {
        boolean delete = brandService.removeById(id);
        if (delete) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("删除失败");
    }

    @ApiOperation("根据条件查询品牌")
    @PostMapping("/search")
    public CommonResult getListByCriteria(@RequestBody Brand brand) {
        return CommonResult.success(brandService.getOne(new QueryWrapper<>(brand)));
    }

    @ApiOperation("分页查询品牌")
    @GetMapping("/search/{pageNum}/{pageSize}")
    public CommonResult getListByPage(@PathVariable("pageNum") int pageNum,
                                      @PathVariable("pageSize") int pageSize) {
        Page<Brand> page = new Page<>(pageNum, pageSize);
        IPage<Brand> iPage = brandService.lambdaQuery().page(page);
        return CommonResult.success(iPage);
    }

    @ApiOperation("根据条件实现分页查询品牌")
    @PostMapping("/search/{pageNum}/{pageSize}")
    public CommonResult getListByPageAndCriteria(@RequestBody Brand brand,
                                                 @PathVariable("pageNum") int pageNum,
                                                 @PathVariable("pageSize") int pageSize) {
        QueryWrapper<Brand> queryWrapper = new QueryWrapper<>(brand);
        Page<Brand> page = new Page<>(pageNum, pageSize);
        IPage<Brand> iPage = brandService.page(page, queryWrapper);
        return CommonResult.success(iPage);
    }

    @ApiOperation("根据分类查询品牌")
    @GetMapping("/category/{cid}")
    public CommonResult getByCategoryId(@PathVariable("cid") Integer cid) {
        return CommonResult.success(brandService.getByCategoryId(cid));
    }
}

