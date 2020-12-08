package com.chase.ribbon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chase.ribbon.common.api.CommonResult;
import com.chase.ribbon.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chase
 * @since 2020-10-18
 */
public interface UserService extends IService<User> {

    /**
     * 登录功能
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    String login(String username, String password);

    /**
     * 生成验证码
     * @param telephone 手机号
     * @return 验证码
     */
    CommonResult generateAuthCode(String telephone);

    /**
     * 判断验证码和手机号码是否匹配
     * @param telephone 手机号
     * @param authCode 验证码
     * @return 验证结果
     */
    CommonResult verifyAuthCode(String telephone, String authCode);
}
