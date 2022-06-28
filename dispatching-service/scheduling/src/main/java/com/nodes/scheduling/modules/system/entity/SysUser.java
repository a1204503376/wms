package com.nodes.scheduling.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户表
 */
@Data
@TableName(value = "sys_user")
public class SysUser {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * 账号
     */
    @TableField(value = "account")
    private String account;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 用户密码
     */
    @TableField(value = "`password`")
    private String password;

    /**
     * 上一次登录时间
     */
    @TableField(value = "last_login_time")
    private Date lastLoginTime;

    /**
     * 账号是否可用。默认为1（可用）
     */
    @TableField(value = "enabled")
    private Boolean enabled;

    /**
     * 是否过期。默认为1（没有过期）
     */
    @TableField(value = "account_non_expired")
    private Boolean accountNonExpired;

    /**
     * 账号是否锁定。默认为1（没有锁定）
     */
    @TableField(value = "account_non_locked")
    private Boolean accountNonLocked;

    /**
     * 证书（密码）是否过期。默认为1（没有过期）
     */
    @TableField(value = "credentials_non_expired")
    private Boolean credentialsNonExpired;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField(value = "create_user")
    private Integer createUser;

    /**
     * 修改人
     */
    @TableField(value = "update_user")
    private Integer updateUser;

    public static final String COL_ID = "id";

    public static final String COL_ACCOUNT = "account";

    public static final String COL_USER_NAME = "user_name";

    public static final String COL_PASSWORD = "password";

    public static final String COL_LAST_LOGIN_TIME = "last_login_time";

    public static final String COL_ENABLED = "enabled";

    public static final String COL_ACCOUNT_NON_EXPIRED = "account_non_expired";

    public static final String COL_ACCOUNT_NON_LOCKED = "account_non_locked";

    public static final String COL_CREDENTIALS_NON_EXPIRED = "credentials_non_expired";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_USER = "create_user";

    public static final String COL_UPDATE_USER = "update_user";
}