package com.nodes.scheduling.common.configure.proprety;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 系统权限配置类
 */
@Data
@ConfigurationProperties(prefix = "nodes.security")
public class SecurityProperty {

    /**
     * 超级管理员不认证
     */
    private boolean superAuthOpen;

    /**
     * 不验证权限用户名
     */
    private String superAdmin;

    /**
     * 记住密码标识
     */
    private String rememberKey;

    /**
     * 开放接口列表
     */
    private String[] openApi;

    /**
     * 是否允许多账号在线
     */
    private Integer maximum = 1;

}
