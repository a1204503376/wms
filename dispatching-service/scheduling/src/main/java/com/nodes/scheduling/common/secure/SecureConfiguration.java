package com.nodes.scheduling.common.secure;

import com.nodes.scheduling.common.configure.proprety.SecurityProperty;
import com.nodes.scheduling.common.secure.domain.SecureUserDetailsServiceImpl;
import com.nodes.scheduling.common.secure.process.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.annotation.Resource;

/**
 * Security 配置
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(SecurityProperty.class)
public class SecureConfiguration extends WebSecurityConfigurerAdapter {

    @Resource
    private SecurityProperty securityProperty;
    @Resource
    private SecureAuthenticationEntryPoint secureAuthenticationEntryPoint;
    @Resource
    private SecureAuthenticationSuccessHandler secureAuthenticationSuccessHandler;
    @Resource
    private SecureAuthenticationFailureHandler secureAuthenticationFailureHandler;
    @Resource
    private SecureLogoutSuccessHandler secureLogoutSuccessHandler;
    @Resource
    private SecureSessionExpiredHandler secureSessionExpiredHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //配置认证方式
        auth.userDetailsService(userDetailsService());
    }

    /**
     * 配置 Security 控制逻辑
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                // 异常处理
                .and().exceptionHandling()
                .authenticationEntryPoint(secureAuthenticationEntryPoint)
                // 登录
                .and().formLogin()
                // 允许所有用户登录
                .permitAll()
                .successHandler(secureAuthenticationSuccessHandler)
                .failureHandler(secureAuthenticationFailureHandler)
                // 登出
                .and().logout()
                // 允许所有用户登出
                .permitAll()
                .logoutSuccessHandler(secureLogoutSuccessHandler)
                .deleteCookies("JSESSIONID")
                .and().sessionManagement()
                // 限制同一账号只能一个用户使用
                .maximumSessions(1)
                .expiredSessionStrategy(secureSessionExpiredHandler)
        ;

        // 取消跨站请求伪造防护
        http.csrf().disable();

        // 防止iframe 造成跨域
        http.headers().frameOptions().disable();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new SecureUserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
