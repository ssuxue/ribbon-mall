package com.chase.ribbon.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chase.ribbon.common.api.CommonResult;
import com.chase.ribbon.entity.Album;
import com.chase.ribbon.service.AlbumService;
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
@RequestMapping("/album")
@Api(tags = "商品相册管理", value = "AlbumController")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @ApiOperation("通过条件实现相册分页查询")
    @PostMapping("/search/{current}/{size}")
    public CommonResult getPage(@RequestBody(required = false) Album album,
                                @PathVariable int current,
                                @PathVariable int size) {
        // 我可真是个小机灵鬼
        QueryWrapper<Album> queryWrapper = new QueryWrapper<>(album);
        Page<Album> page = new Page<>(current, size);
        IPage<Album> iPage = albumService.page(page, queryWrapper);
        return CommonResult.success(iPage);
    }

    @ApiOperation("相册分页查询")
    @GetMapping("/search/{current}/{size}")
    public CommonResult getPage(@PathVariable int current, @PathVariable int size) {
        Page<Album> page = new Page<>(current, size);
        IPage<Album> iPage = albumService.lambdaQuery().page(page);
        return CommonResult.success(iPage);
    }

    @ApiOperation("通过条件查询相册信息")
    @PostMapping("/search")
    public CommonResult getList(@RequestBody(required = false) Album album) {
        return CommonResult.success(albumService.getOne(new QueryWrapper<>(album)));
    }

    @ApiOperation("通过ID删除相册")
    @DeleteMapping("/{id}")
    public CommonResult delete(@PathVariable Long id) {
        boolean delete = albumService.removeById(id);
        if (delete) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("删除失败");
    }

    @ApiOperation("通过ID修改相册信息")
    @PutMapping("/{id}")
    public CommonResult update(@RequestBody Album album, @PathVariable Long id) {
        album.setId(id);
        boolean update = albumService.updateById(album);
        if (update) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("修改失败");
    }

    @ApiOperation("添加相册信息")
    @PostMapping
    public CommonResult add(@RequestBody Album template) {
        boolean result = albumService.save(template);
        if (result) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("添加失败");
    }

    @ApiOperation("通过ID查询相册")
    @GetMapping("/{id}")
    public CommonResult getById(@PathVariable Long id) {
        return CommonResult.success(albumService.getById(id));
    }

    @ApiOperation("查询所有相册")
    @GetMapping
    public CommonResult getAll(){
        return CommonResult.success(albumService.list()) ;
    }
}

