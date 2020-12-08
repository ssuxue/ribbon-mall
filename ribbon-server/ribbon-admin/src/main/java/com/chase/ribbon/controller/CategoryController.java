package com.chase.ribbon.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chase.ribbon.common.api.CommonResult;
import com.chase.ribbon.entity.Category;
import com.chase.ribbon.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 商品类目 前端控制器
 * </p>
 *
 * @author chase
 * @since 2020-10-18
 */
@RestController
@RequestMapping("/category")
@Api(tags = "商品分类管理")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("根据条件实现分页查询")
    @PostMapping("/search/{pageNum}/{pageSize}")
    public CommonResult getListByPageAndCriteria(@RequestBody(required = false) Category category,
                                                 @PathVariable("pageNum") int pageNum,
                                                 @PathVariable("pageSize") int pageSize) {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>(category);
        Page<Category> page = new Page<>(pageNum, pageSize);
        IPage<Category> iPage = categoryService.page(page, queryWrapper);
        return CommonResult.success(iPage);
    }

    @ApiOperation("分页查询分类")
    @GetMapping("/search/{pageNum}/{pageSize}")
    public CommonResult getListByPage(@PathVariable("pageNum") int pageNum,
                                      @PathVariable("pageSize") int pageSize) {
        Page<Category> page = new Page<>(pageNum, pageSize);
        IPage<Category> iPage = categoryService.page(page);
        return CommonResult.success(iPage);
    }

    @ApiOperation("通过条件查询")
    @PostMapping("/search")
    public CommonResult getListByCriteria(@RequestBody(required = false) Category category) {
        return CommonResult.success(categoryService.getOne(new QueryWrapper<>(category)));
    }

    @ApiOperation("修改分类信息")
    @PutMapping("/update/{id}")
    public CommonResult updateCategory(@PathVariable("id") Integer id, @RequestBody Category category) {
        category.setId(id);

        boolean update = categoryService.updateById(category);
        if (update) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("修改失败");
    }

    @ApiOperation("删除分类信息")
    @DeleteMapping("/delete/{id}")
    public CommonResult deleteCategory(@PathVariable("id") Integer id) {
        boolean result = categoryService.removeById(id);
        if (result) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("删除失败");
    }

    @ApiOperation("新增分类信息")
    @PostMapping("/add")
    public CommonResult addCategory(@RequestBody Category category) {
        boolean result = categoryService.save(category);
        if (result) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("添加失败");
    }

    @ApiOperation("通过ID查询分类")
    @GetMapping("/getOne/{id}")
    public CommonResult getCategoryById(@PathVariable("id") Integer id) {
        return CommonResult.success(categoryService.getById(id));
    }

    @ApiOperation("获取所有分类")
    @GetMapping("/getAll")
    public CommonResult getALlCategorys() {
        return CommonResult.success(categoryService.list());
    }

    @ApiOperation("根据分类父ID查询子分类")
    @GetMapping("/list/{pid}")
    public CommonResult getByParentId(@PathVariable("pid") Integer pid) {
        Category category = categoryService.lambdaQuery().eq(Category::getParentId, pid).one();
        return CommonResult.success(category);
    }
}

