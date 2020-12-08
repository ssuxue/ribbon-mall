package com.chase.ribbon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chase.ribbon.entity.Role;
import com.chase.ribbon.mapper.RoleMapper;
import com.chase.ribbon.service.RoleService;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
