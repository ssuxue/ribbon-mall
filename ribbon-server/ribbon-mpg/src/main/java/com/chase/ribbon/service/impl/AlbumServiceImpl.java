package com.chase.ribbon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chase.ribbon.entity.Album;
import com.chase.ribbon.mapper.AlbumMapper;
import com.chase.ribbon.service.AlbumService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chase
 * @since 2020-10-18
 */
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album> implements AlbumService {

}
