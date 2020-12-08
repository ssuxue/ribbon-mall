package com.chase.ribbon.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chase.ribbon.common.api.CommonResult;
import com.chase.ribbon.entity.Category;
import com.chase.ribbon.entity.Specification;
import com.chase.ribbon.service.CategoryService;
import com.chase.ribbon.service.SpecificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chase
 * @since 2020-10-18
 */
@RestController
@RequestMapping("/specification")
@Api(tags = "商品规格管理")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;
    @Autowired
    private CategoryService categoryService;

    @ApiOperation("通过条件实现规格分页查询")
    @PostMapping("/search/{current}/{size}")
    public CommonResult getPage(@RequestBody(required = false) Specification specification,
                                @PathVariable int current,
                                @PathVariable int size) {
        QueryWrapper<Specification> queryWrapper = new QueryWrapper<>(specification);
        Page<Specification> page = new Page<>(current, size);
        IPage<Specification> iPage = specificationService.page(page, queryWrapper);
        return CommonResult.success(iPage);
    }

    @ApiOperation("规格分页查询")
    @GetMapping("/search/{current}/{size}")
    public CommonResult getPage(@PathVariable int current, @PathVariable int size) {
        Page<Specification> page = new Page<>(current, size);
        IPage<Specification> iPage = specificationService.lambdaQuery().page(page);
        return CommonResult.success(iPage);
    }

    @ApiOperation("通过条件查询规格信息")
    @PostMapping("/search")
    public CommonResult getList(@RequestBody(required = false) Specification spec) {
        return CommonResult.success(specificationService.getOne(new QueryWrapper<>(spec)));
    }

    @ApiOperation("通过ID删除规格")
    @DeleteMapping("/{id}")
    public CommonResult delete(@PathVariable Integer id) {
        boolean result = specificationService.removeById(id);
        if (result) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("删除失败");
    }

    @ApiOperation("通过ID修改规格信息")
    @PutMapping("/{id}")
    public CommonResult update(@RequestBody Specification spec, @PathVariable Integer id) {
        spec.setId(id);
        boolean result = specificationService.updateById(spec);
        if (result) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("修改失败");
    }

    @ApiOperation("添加规格信息")
    @PostMapping
    public CommonResult add(@RequestBody Specification spec) {
        boolean result = specificationService.save(spec);
        if (result) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("添加失败");
    }

    @ApiOperation("通过ID查询参数")
    @GetMapping("/{id}")
    public CommonResult getById(@PathVariable Integer id) {
        return CommonResult.success(specificationService.getById(id));
    }

    @ApiOperation("查询所有参数")
    @GetMapping
    public CommonResult getAll(){
        return CommonResult.success(specificationService.list()) ;
    }

    @ApiOperation("通过分类ID查询参数")
    @GetMapping("/category/{cid}")
    public CommonResult getByCategoryId(@PathVariable("cid") Integer cid) {
        Category category = categoryService.getById(cid);

        Specification specification = specificationService.lambdaQuery()
                .eq(Specification::getTemplateId, category.getTemplateId())
                .one();
        return CommonResult.success(specification);
    }
}

