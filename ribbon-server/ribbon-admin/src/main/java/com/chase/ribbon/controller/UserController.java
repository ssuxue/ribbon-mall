package com.chase.ribbon.controller;


import com.chase.ribbon.common.api.CommonResult;
import com.chase.ribbon.common.util.JwtTokenUtils;
import com.chase.ribbon.dto.RequestLoginUser;
import com.chase.ribbon.entity.User;
import com.chase.ribbon.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chase
 * @since 2020-10-18
 */
@RestController
@RequestMapping("/user")
@Api(tags = "会员验证授权管理")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @ApiOperation("测试管理员权限")
    @GetMapping("/auth/admin")
    public String adminAuth() {
        return "管理员权限";
    }

    @ApiOperation("测试普通会员权限")
    @GetMapping("/auth/user")
    public String userAuth() {
        return "普通权限";
    }

    @ApiOperation("获取会员信息")
    @GetMapping("/geInfo/{username}")
    public User getInfo(@PathVariable("username") @ApiParam("用户名") String username) {
        return userService.lambdaQuery().eq(User::getUsername, username).one();
    }

    @ApiOperation("通过token获取会员信息")
    @GetMapping("/getInfoByToken")
    @ResponseBody
    public CommonResult<User> getInfoByToken(@RequestParam @ApiParam("JWT令牌") String token) {
        String username = jwtTokenUtils.getUserNameFromToken(token);
        User user = userService.lambdaQuery().eq(User::getUsername, username).one();
        return CommonResult.success(user);
    }

    @ApiOperation("登录认证")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public CommonResult login(@RequestBody RequestLoginUser requestLoginUser, BindingResult bindingResult) {

        // 检查有没有输入用户名密码和格式对不对
        if (bindingResult.hasErrors()) {
            return CommonResult.failed("缺少参数或者参数格式不对");
        }

        String token = userService.login(requestLoginUser.getUsername(), requestLoginUser.getPassword());
        if (token == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation("获取验证码")
    @PostMapping("/getAuthCode")
    @ResponseBody
    public CommonResult getAuthCode(@RequestParam @ApiParam("电话号码") String telephone) {
        return userService.generateAuthCode(telephone);
    }

    @ApiOperation("判断验证码是否正确")
    @PostMapping("/verifyAuthCode")
    @ResponseBody
    public CommonResult updatePassword(@RequestParam @ApiParam("电话号码") String telephone,
                                       @RequestParam @ApiParam("验证码") String authCode) {
        return userService.verifyAuthCode(telephone,authCode);
    }
}

