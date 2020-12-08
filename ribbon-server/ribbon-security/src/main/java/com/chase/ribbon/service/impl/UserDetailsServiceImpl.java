package com.chase.ribbon.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @Description SpringSecurity 用于将 数据库中的用户信息转换成 userDetail 对象的服务类的实现类
 * @Author chase
 * @Date 2020/8/30 11:03
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // @Autowired
    // private UserDao userDao;

    /**
     * 获取 userDetail
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // UserDTO user = this.userDao.getUserFromDatabase(username);
        //
        // if (user == null) {
        //     throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        // }
        // UserDetails userDetails = User.withUsername(user.getUsername()).password(user.getPassword()).authorities(user.getAuthorities()).build();
        // return userDetails;
        return null;
    }
}