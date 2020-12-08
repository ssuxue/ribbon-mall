package com.chase.ribbon.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chase.ribbon.common.api.CommonResult;
import com.chase.ribbon.entity.Category;
import com.chase.ribbon.entity.Template;
import com.chase.ribbon.service.CategoryService;
import com.chase.ribbon.service.TemplateService;
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
@RequestMapping("/template")
@Api(tags = "商品模板管理")
public class TemplateController {

    @Autowired
    private TemplateService templateService;
    @Autowired
    private CategoryService categoryService;

    @ApiOperation("通过条件实现模板分页查询")
    @PostMapping("/search/{current}/{size}")
    public CommonResult getPage(@RequestBody(required = false) Template template,
                                @PathVariable int current,
                                @PathVariable int size) {
        QueryWrapper<Template> queryWrapper = new QueryWrapper<>(template);
        Page<Template> page = new Page<>(current, size);
        IPage<Template> iPage = templateService.page(page, queryWrapper);
        return CommonResult.success(iPage);
    }

    @ApiOperation("模板分页查询")
    @GetMapping("/search/{current}/{size}")
    public CommonResult getPage(@PathVariable int current, @PathVariable int size) {
        Page<Template> page = new Page<>(current, size);
        IPage<Template> iPage = templateService.lambdaQuery().page(page);
        return CommonResult.success(iPage);
    }

    @ApiOperation("通过条件查询模板信息")
    @PostMapping("/search")
    public CommonResult getList(@RequestBody(required = false) Template template) {
        return CommonResult.success(templateService.getOne(new QueryWrapper<>(template)));
    }

    @ApiOperation("通过ID删除模板")
    @DeleteMapping("/{id}")
    public CommonResult delete(@PathVariable Integer id) {
        boolean delete = templateService.removeById(id);
        if (delete) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("删除失败");
    }

    @ApiOperation("通过ID修改模板信息")
    @PutMapping("/{id}")
    public CommonResult update(@RequestBody Template template, @PathVariable Integer id) {
        template.setId(id);
        boolean update = templateService.updateById(template);
        if (update) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("修改失败");
    }

    @ApiOperation("添加模板信息")
    @PostMapping
    public CommonResult add(@RequestBody Template template) {
        boolean result = templateService.save(template);
        if (result) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("添加失败");
    }

    @ApiOperation("通过ID查询模板")
    @GetMapping("/{id}")
    public CommonResult getById(@PathVariable Integer id) {
        return CommonResult.success(templateService.getById(id));
    }

    @ApiOperation("查询所有模板")
    @GetMapping
    public CommonResult getAll(){
        return CommonResult.success(templateService.list()) ;
    }

    @ApiOperation("通过分类ID查询模板")
    @GetMapping("/category/{cid}")
    public CommonResult getByCategoryId(@PathVariable("cid") Integer cid) {
        Category category = categoryService.getById(cid);

        Template template = templateService.lambdaQuery()
                .eq(Template::getId, category.getTemplateId())
                .one();
        return CommonResult.success(template);
    }
}

