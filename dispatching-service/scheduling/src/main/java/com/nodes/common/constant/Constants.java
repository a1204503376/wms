package com.nodes.common.constant;

/**
 * 通用常量信息
 *
 * @author dml
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 系统用户授权缓存
     */
    public static final String SYS_AUTH_CACHE = "sys-authCache";

    /**
     * 参数管理 cache name
     */
    public static final String SYS_CONFIG_CACHE = "sys-config";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache name
     */
    public static final String SYS_DICT_CACHE = "sys-dict";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * 调度JOB类型
     */
    public static final String SCHEDULING_JOB_TYPE = "scheduling_job_type";

    /**
     * 调度JOB类型对应的优先级
     */
    public static final String SCHEDULING_JOB_PRIORITY = "scheduling_job_priority";

    /**
     * 调度JOB默认优先级
     */
    public static final int DEFAULT_PRIORITY = 100;

    /**
     * 调度JOB最高优先级
     */
    public static final int HIGHEST_PRIORITY = 0;

    /**
     * 系统通用API路径前缀
     */
    public static final String API_PATH_PREFIX = "/api/";

    /**
     * AGV API
     */
    public static final String API_AGV = API_PATH_PREFIX + "agv";

    /**
     * WMS API
     */
    public static final String API_WMS = API_PATH_PREFIX + "wms";

    /**
     * WMS API名称
     */
    public static final String API_WMS_NAME = "wms_api";

    /**
     * AGV API名称
     */
    public static final String API_AGV_NAME = "agv_api";
}
