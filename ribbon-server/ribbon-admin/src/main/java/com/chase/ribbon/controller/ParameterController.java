package com.chase.ribbon.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chase.ribbon.common.api.CommonResult;
import com.chase.ribbon.entity.Category;
import com.chase.ribbon.entity.Parameter;
import com.chase.ribbon.service.CategoryService;
import com.chase.ribbon.service.ParameterService;
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
@RequestMapping("/parameter")
@Api(tags = "商品参数管理")
public class ParameterController {

    @Autowired
    private ParameterService parameterService;
    @Autowired
    private CategoryService categoryService;

    @ApiOperation("通过条件实现参数分页查询")
    @PostMapping("/search/{page}/{size}")
    public CommonResult getPage(@RequestBody(required = false) Parameter parameter,
                                @PathVariable int current,
                                @PathVariable int size) {
        QueryWrapper<Parameter> queryWrapper = new QueryWrapper<>(parameter);
        Page<Parameter> page = new Page<>(current, size);
        IPage<Parameter> iPage = parameterService.page(page, queryWrapper);
        return CommonResult.success(iPage);
    }

    @ApiOperation("参数分页查询")
    @GetMapping("/search/{page}/{size}")
    public CommonResult getPage(@PathVariable int current, @PathVariable int size) {
        Page<Parameter> page = new Page<>(current, size);
        IPage<Parameter> iPage = parameterService.lambdaQuery().page(page);
        return CommonResult.success(iPage);
    }

    @ApiOperation("通过条件查询参数信息")
    @PostMapping("/search")
    public CommonResult getList(@RequestBody(required = false) Parameter parameter) {
        return CommonResult.success(parameterService.getOne(new QueryWrapper<>(parameter)));
    }

    @ApiOperation("通过ID删除参数")
    @DeleteMapping("/{id}")
    public CommonResult delete(@PathVariable Integer id) {
        boolean result = parameterService.removeById(id);
        if (result) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("删除失败");
    }

    @ApiOperation("通过ID修改参数信息")
    @PutMapping("/{id}")
    public CommonResult update(@RequestBody Parameter parameter, @PathVariable Integer id) {
        parameter.setId(id);

        boolean result = parameterService.updateById(parameter);
        if (result) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("修改失败");
    }

    @ApiOperation("添加参数信息")
    @PostMapping
    public CommonResult add(@RequestBody Parameter parameter) {
        boolean result = parameterService.save(parameter);
        if (result) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("添加失败");
    }

    @ApiOperation("通过ID查询参数")
    @GetMapping("/{id}")
    public CommonResult getById(@PathVariable Integer id) {
        return CommonResult.success(parameterService.getById(id));
    }

    @ApiOperation("查询所有参数")
    @GetMapping
    public CommonResult getAll(){
        return CommonResult.success(parameterService.list()) ;
    }

    @ApiOperation("通过分类ID查询参数")
    @GetMapping("/category/{cid}")
    public CommonResult getByCategoryId(@PathVariable("cid") Integer cid) {
        Category category = categoryService.getById(cid);
        Parameter parameter = parameterService.lambdaQuery()
                .eq(Parameter::getTemplateId, category.getTemplateId())
                .one();
        return CommonResult.success(parameter);
    }
}

