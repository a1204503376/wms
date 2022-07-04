package com.nodes.project.system.user.domain;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 用户状态
 *
 * @author dml
 */
@TableName("sys_user_status")
public enum UserStatus {
    OK("0", "正常"), DISABLE("1", "停用"), DELETED("2", "删除");

    private final String code;
    private final String info;

    UserStatus(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
