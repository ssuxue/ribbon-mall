package com.chase.ribbon.config;

import com.chase.ribbon.component.EntryPointUnauthorizedHandler;
import com.chase.ribbon.component.JwtAuthenticationTokenFilter;
import com.chase.ribbon.component.MyAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @version 1.0
 * @Description SpringSecurity 配置类
 * @Author chase
 * @Date 2020/8/30 10:36
 */
@Configuration  // 声明为配置类
@EnableWebSecurity  // 启用 Spring Security web 安全的功能
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 注册 401 处理器
     */
    @Autowired
    private EntryPointUnauthorizedHandler unauthorizedHandler;

    /**
     * 注册 403 处理器
     */
    @Autowired
    private MyAccessDeniedHandler accessDeniedHandler;

    // 密码编码器
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 安全拦截机制
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/auth/admin").hasAuthority("admin")
                .antMatchers("/auth/user").hasAuthority("user")
                .antMatchers("/login", "/member/register").permitAll() // 对登录注册要允许匿名访问
                .anyRequest().permitAll()   // 允许所有请求通过
                .and()
                .exceptionHandling()    // 配置被拦截时的处理
                .authenticationEntryPoint(this.unauthorizedHandler)   // 添加 token 无效或者没有携带 token 时的处理
                .accessDeniedHandler(this.accessDeniedHandler)      // 添加无权限时的处理
                .and()
                .csrf() // 由于使用的是JWT，这里不需要csrf
                .disable()
                .sessionManagement()                        // 定制自己的 session 策略
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);// 调整为让 Spring Security 不创建和使用 session// 禁用 Spring Security 自带的跨域处理

        // 禁用缓存
        http.headers().cacheControl();
        // 添加JWT filter
        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }
}
