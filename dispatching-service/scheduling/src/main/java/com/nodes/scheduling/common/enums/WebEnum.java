package com.nodes.scheduling.common.enums;

/**
 * web端返回状态码
 */
public enum WebEnum {

    //----------------------自定义-----------------------------

    /**
     * 操作成功
     * 0
     */
    SUCCESS(0, "操作成功"),
    /**
     * 未知错误
     * 系统繁忙，此时请开发者稍候再试
     * -1
     */
    ERROR(-1, "未知错误"),
    /**
     * 路径不存在
     * 404
     */
    PATH_DOES_NOT_EXIST(404, "路径不存在"),

    /**
     * 用户不存在
     * 30001
     */
    USER_NOT_EXIST(30001, "账号不存在"),
    /**
     * 密码错误
     * 30002
     */
    PASSWORD_ERROR(30002, "密码错误"),
    /**
     * 账号密码错误
     * 30003
     */
    ACCOUNT_OR_PASSWORD_FAILURE(30003, "账号或密码错误,请重新登录"),
    /**
     * 验证码错误
     * 30004
     */
    VERIFICATION_CODE_ERROR(30004, "验证码错误"),
    /**
     * 请重新登录
     * 30005
     */
    RELOGIN(30005, "请重新登录"),
    /**
     * 密码不一致
     * 30006
     */
    INCONSISTENCY_OF_CIPHERS(30006, "密码不一致"),
    /**
     * 重置密码成功
     * 30007
     */
    RESET_THE_PASSWORD_SUCCESS(30007, "重置密码成功"),

    /**
     * 没有权限
     * 30008
     */
    NO_PERMISSION(30008, "没有权限"),
    /**
     * 用户已冻结
     * 30009
     */
    THE_USER_HAS_BEEN_FROZEN(30009, "用户已冻结"),
    /**
     * 修改成功
     * 30010
     */
    AMEND_THE_SUCCESS(30010, "修改成功"),
    /**
     * 发送验证码成功
     * 30011
     */
    SEND_VERIFICATION_CODE_SUCCESS(30011, "发送验证码成功"),
    /**
     * 发送验证码失败
     * 30012
     */
    FAILURE_OF_SENDING_AUTHENTICATION_CODE(30012, "发送验证码失败"),
    /**
     * 请先登录
     * 30013
     */
    PLEASE_LOG_IN_FIRST(30013, "请先登录"),
    /**
     * 该账号已注册，无法重复注册!
     * 30014
     */
    THE_ACCOUNT_HAS_BEEN_REGISTERED(30014, "该账号已注册,无法重复注册!"),
    /**
     * 导入失败
     * 30015
     */
    IMPORT_FAILURE(30015, "导入失败"),
    /**
     * 添加失败
     * 30016
     */
    ADD_FAILURE(30016, "添加失败"),
    /**
     * 查找失败
     * 30017
     */
    LOOKUP_FAILURE(30017, "查找失败"),
    /**
     * 修改失败
     * 30018
     */
    FAILURE_TO_MODIFY(30018, "修改失败"),
    /**
     * 删除失败
     * 30019
     */
    DELETE_FAILURE(30019, "删除失败"),
    /**
     * 删除失败
     * 30019
     */
    DUPLICATE_SUBMISSION_NOT_ALLOWED(30020, "不允许重复提交，请稍后再试"),


    REPEAT_LOGIN(30021, "您已在别处登录，请您修改密码或重新登录"),

    NOT_CURRENT_SYSTEM(30022, "当前系统没有开启注册功能！"),

    //----------------------微信公众号返回码-----------------------------

    /**
     * 获取 access_token 时 AppSecret 错误，或者 access_token 无效。
     * 请开发者认真比对 AppSecret 的正确性，或查看是否正在为恰当的公众号调用接口.
     * 40001
     */
    TOKEN_INVALID(40001, "AppSecret错误，或者 access_token无效"),
    /**
     * 不合法的凭证类型
     * 40002
     */
    UNIAWFUL_VOUCHER_TYPE(40002, "不合法的凭证类型"),
    /**
     * 不合法的 OpenID ，请开发者确认 OpenID （该用户）是否已关注公众号，或是否是其他公众号的 OpenID
     * 40003
     */
    UNIAWFUL_OPENID(40003, "不合法的 OpenID"),
    /**
     * 不合法的媒体文件类型
     * 40004
     */
    UNIAWFUL_MEDIA_TYPES(40004, "不合法的媒体类型"),
    /**
     * 不合法的文件类型
     * 40005
     */
    UNIAWFUL_FILE_TYPE(40005, "不合法的文件类型"),
    /**
     * 不合法的文件大小
     * 40006
     */
    UNIAWFUL_FILE_SIZE(40006, "不合法的文件大小"),
    /**
     * 不合法的媒体文件 id
     * 40007
     */
    ILLEGAL_MEDIA_FILE_ID(40007, "不合法的媒体文件 id"),
    /**
     * 不合法的消息类型
     * 40008
     */
    UNIAWFUL_MESSAGE_TYPE(40008, "不合法的消息类型"),
    /**
     * 不合法的图片文件大小(大小不能超过5M)
     * 40009
     */
    UNIAWFUL_PICTURE_FILE_SIZE(40009, "不合法的图片文件大小"),
    /**
     * 不合法的语音文件大小(小于60秒)
     * 40010
     */
    UNIAWFUL_VOIC_FILE_SIZE(40010, "不合法的语音文件大小"),
    /**
     * 不合法的视频文件大小
     * 40011
     */
    UNIAWFUL_VIDEO_FILE_SIZE(40011, "不合法的视频文件大小"),
    /**
     * 不合法的缩略图文件大小
     * 40012
     */
    UNIAWFUL_THUMBNAIL_FILE_SIZE(40012, "不合法的缩略图文件大小"),
    /**
     * 不合法的 AppID ，请开发者检查 AppID 的正确性，避免异常字符，注意大小写
     * 40013
     */
    UNIAWFUL_APPID(40013, "不合法的 AppID"),
    /**
     * 不合法的 access_token ，请开发者认真比对 access_token 的有效性（如是否过期），或查看是否正在为恰当的公众号调用接口
     * 40014
     */
    UNIAWFUL_ACCESS_TOKEN(40014, "不合法的 access_token"),
    /**
     * 不合法的菜单类型
     * 40015
     */
    UNIAWFUL_MENU_TYPE(40015, "不合法的菜单类型"),
    /**
     * 不合法的按钮个数
     * 40016
     */
    THE_NUMBER_OF_UNLAWFUL_BUTTONS(40016, "不合法的按钮个数"),
    /**
     * 不合法的按钮个数
     * 40017
     */
    THE_NUMBER_OF_UNLAWFUL_BUTTONSS(40017, "不合法的按钮个数"),
    /**
     * 不合法的按钮名字长度
     * 40018
     */
    UNLAWFUL_BUTTON_NAME_LENGTH(40018, "不合法的按钮名字长度"),
    /**
     * 不合法的按钮 KEY 长度
     * 40019
     */
    UNLAWFUL_BUTTON_KEY_LENGTH(40019, "不合法的按钮 KEY长度"),
    /**
     * 不合法的按钮 URL 长度
     * 40020
     */
    UNLAWFUL_BUTTON_URL_LENGTH(40020, "不合法的按钮 URL长度"),
    /**
     * 不合法的菜单版本号
     * 40021
     */
    UNLAWFUL_MENU_VERSION_NUMBER(40021, "不合法的菜单版本号"),
    /**
     * 不合法的子菜单级数
     * 40022
     */
    UNLAWFUL_SUBMENU_SERIES(40022, "不合法的子菜单级数"),
    /**
     * 不合法的子菜单按钮个数
     * 40023
     */
    THE_NUMBER_OF_UNLAWFUL_SUBMENU_BUTTONS(40023, "不合法的子菜单按钮个数"),
    /**
     * 不合法的子菜单按钮类型
     * 40024
     */
    UNLAWFUL_SUBMENU_BUTTON_TYPE(40024, "不合法的子菜单按钮类型"),
    /**
     * 不合法的子菜单按钮名字长度
     * 40025
     */
    UNLAWFUL_SUBMENU_BUTTON_NAME_LENGTH(40025, "不合法的子菜单按钮名字长度"),
    /**
     * 不合法的子菜单按钮 KEY 长度
     * 40026
     */
    UNLAWFUL_SUBMENU_BUTTON_KEY_LENGTH(40026, "不合法的子菜单按钮 KEY长度"),
    /**
     * 不合法的子菜单按钮URL长度
     * 40027
     */
    UNLAWFUL_SUBMENU_BUTTON_URL_LENGTH(40027, "不合法的子菜单按钮URL长度"),
    /**
     * 不合法的自定义菜单使用用户
     * 40028
     */
    UNLAWFUL_CUSTOM_MENUS_USE_THE_USER(40028, "不合法的自定义菜单使用用户"),
    /**
     * 不合法的 oauth_code
     * 40029
     */
    UNLAWFUL_OAUTH_CODE(40029, "不合法的 oauth_code"),
    /**
     * 不合法的 refresh_token
     * 40030
     */
    UNLAWFUL_REFRESH_TOKEN(40030, "不合法的 refresh_token"),
    /**
     * 不合法的 openid 列表
     * 40031
     */
    UNLAWFUL_OPENID_LIST(40031, "不合法的 openid列表"),
    /**
     * 不合法的 openid列表长度
     * 40032
     */
    UNLAWFUL_OPENID_LIST_LENGTH(40032, "不合法的 openid列表长度"),
    /**
     * 不合法的请求字符，不能包含  "\"uxxxx 格式的字符
     * 40033
     */
    UNLAWFUL_REQUEST_CHARACTER(40033, "不合法的请求字符"),
    /**
     * 不合法的参数
     * 40035
     */
    UNLAWFUL_PARAMETERS(40035, "不合法的参数"),
    /**
     * 不合法的请求格式
     * 40038
     */
    UNLAWFUL_REQUEST_FORMAT(40038, "不合法的请求格式"),
    /**
     * 不合法的 URL长度
     * 40039
     */
    UNLAWFUL_URL_LENGTH(40039, "不合法的 URL长度"),
    /**
     * 不合法的分组 id
     * 40050
     */
    UNLAWFUL_GROUPING_ID(40050, "不合法的分组 id"),
    /**
     * 分组名字不合法
     * 40051
     */
    GROUP_NAME_ILLEGALITY(40051, "分组名字不合法"),
    /**
     * 删除单篇图文时，指定的 article_idx 不合法
     * 40060
     */
    ARTICLE_IDX_ILLEGALITY(40060, "article_idx 不合法"),
    /**
     * 分组名字不合法
     * 40117
     */
    GROUP_NAMES_ILLEGALITY(40117, "分组名字不合法"),
    /**
     * media_id 大小不合法
     * 40118
     */
    MEDIA_ID_SIZE_IS_UNLAWFUL(40118, "media_id大小不合法"),
    /**
     * button 类型错误
     * 40119
     */
    BUTTON_TYPE_ERROR(40119, "button类型错误"),
    /**
     * button 类型错误
     * 40120
     */
    BUTTONS_TYPE_ERROR(40120, "button类型错误"),
    /**
     * 不合法的 media_id 类型
     * 40121
     */
    UNLAWFUL_MEDIA_ID_TYPE(40121, "不合法的 media_id类型"),
    /**
     * 微信号不合法
     * 40132
     */
    MICRO_SIGNAL_ILLEGALITY(40132, "微信号不合法"),
    /**
     * 不支持的图片格式
     * 40137
     */
    UNSUPPORTED_PICTURE_FORMATS(40137, "不支持的图片格式"),
    /**
     * 请勿添加其他公众号的主页链接
     * 40155
     */
    NOT_ADD_LINKS_OTHER_MICRO_SIGNAL(40155, "请勿添加其他公众号的主页链接"),
    /**
     * 缺少access_token参数
     * 41001
     */
    LACK_ACCESS_TOKEN_OF_PARAMETERS(41001, "缺少access_token参数"),
    /**
     * 缺少 appid 参数
     * 41002
     */
    LACK_APPID_OF_PARAMETERS(41002, "缺少appid参数"),
    /**
     * 缺少refresh_token参数
     * 41003
     */
    LACK_REFRESH_TOKEN_OF_PARAMETERS(41003, "缺少refresh_token参数"),
    /**
     * 缺少 secret 参数
     * 41004
     */
    LACK_SECRET_OF_PARAMETERS(41004, "缺少secret参数"),
    /**
     * 缺少多媒体文件数据
     * 41005
     */
    LACK_OF_MULTIMEDIA_FILE_DATA(41005, "缺少多媒体文件数据"),
    /**
     * 缺少 media_id 参数
     * 41006
     */
    LACK_MEDIA_ID_OF_PARAMETERS(41006, "缺少media_id参数"),
    /**
     * 缺少子菜单数据
     * 41007
     */
    LACK_OF_SUBMENU_DATA(41007, "缺少子菜单数据"),
    /**
     * 缺少 oauth code
     * 41008
     */
    LACK_OAUTH_CODE(41008, "缺少oauth code"),
    /**
     * 缺少 openid
     * 41009
     */
    LACK_OPENID(41009, "缺少openid"),
    /**
     * access_token 超时，请检查 access_token 的有效期，
     * 请参考基础支持 - 获取 access_token 中，对 access_token 的详细机制说明
     * 42001
     */
    ACCESS_TOKEN_OVERTIME(42001, "access_token超时"),
    /**
     * refresh_token 超时
     * 42002
     */
    REFRESH_TOKEN_OVERTIME(42002, "refresh_token超时"),
    /**
     * oauth_code 超时
     * 42003
     */
    OAUTH_CODE_OVERTIME(42003, "oauth_code超时"),
    /**
     * 用户修改微信密码， accesstoken 和 refreshtoken 失效，需要重新授权
     * 42007
     */
    RE_AUTHORIZE(42007, "重新授权"),
    /**
     * 需要 GET 请求
     * 43001
     */
    NEED_GET_REQUESTS(43001, "需要 GET请求"),
    /**
     * 需要POST请求
     * 43002
     */
    NEED_POST_REQUESTS(43002, "需要POST请求"),
    /**
     * 需要HTTPS请求
     * 43003
     */
    NEED_HTTPS_REQUESTS(43003, "需要HTTPS请求"),
    /**
     * 需要接收者关注
     * 43004
     */
    NEED_THE_RECIPIENT_ATTENTION(43004, "需要接收者关注"),
    /**
     * 需要好友关系
     * 43005
     */
    NEED_FRIENDS(43005, "需要好友关系"),
    /**
     * 需要将接收者从黑名单中移除
     * 43019
     */
    REMOVE_THE_RECIPIENT_FROM_THE_BLACKLIST(43019, "需要将接收者从黑名单中移除"),
    /**
     * 多媒体文件为空
     * 44001
     */
    MULTIMEDIA_FILES_ARE_EMPTY(44001, "多媒体文件为空"),
    /**
     * POST 的数据包为空
     * 44002
     */
    THE_DATA_PACKET_OF_POST_IS_EMPTY(44002, "POST的数据包为空"),
    /**
     * 图文消息内容为空
     * 44003
     */
    THE_CONTENT_OF_THE_MESSAGE_EMPTY(44003, "图文消息内容为空"),
    /**
     * 文本消息内容为空
     * 44004
     */
    THE_MESSAGE_CONTENT_EMPTY(44004, "文本消息内容为空"),
    /**
     * 多媒体文件大小超过限制
     * 45001
     */
    MULTIMEDIA_FILE_SIZE_LIMIT(45001, "多媒体文件大小超过限制"),
    /**
     * 消息内容超过限制
     * 45002
     */
    MESSAGE_CONTENT_EXCEEDS_RESTRICTIONS(45002, "消息内容超过限制"),
    /**
     * 标题字段超过限制
     * 45003
     */
    TITLE_FIELDS_EXCEED_RESTRICTIONS(45003, "标题字段超过限制"),
    /**
     * 描述字段超过限制
     * 45004
     */
    DESCRIPTION_FIELD_EXCEEDS_RESTRICTIONS(45004, "描述字段超过限制"),
    /**
     * 链接字段超过限制
     * 45005
     */
    LINK_FIELD_EXCEEDS_RESTRICTIONS(45005, "链接字段超过限制"),
    /**
     * 图片链接字段超过限制
     * 45006
     */
    PHOTO_LINK_FIELD_EXCEEDS_RESTRICTIONS(45006, "图片链接字段超过限制"),
    /**
     * 语音播放时间超过限制
     * 45007
     */
    VOICE_PLAY_TIME_EXCEEDS_LIMIT(45007, "语音播放时间超过限制"),
    /**
     * 图文消息超过限制
     * 45008
     */
    GRAPH_AND_TEXT_MESSAGE_EXCEEDING_RESTRICTION(45008, "图文消息超过限制"),
    /**
     * 接口调用超过限制
     * 45009
     */
    INTERFACE_CALLS_EXCEED_RESTRICTIONS(45009, "接口调用超过限制"),
    /**
     * 建菜单个数超过限制
     * 45010
     */
    NUMBER_OF_MENUS_MORE_THAN_LIMIT(45010, "建菜单个数超过限制"),
    /**
     * API 调用太频繁，请稍候再试
     * 45011
     */
    LATER_TRY_AGAIN(45011, "请稍候再试"),
    /**
     * 回复时间超过限制
     * 45015
     */
    RETURN_TIME_EXCEEDS_LIMIT(45015, "回复时间超过限制"),
    /**
     * 系统分组，不允许修改
     * 45016
     */
    NOT_ALLOWED_TO_MODIFY(45016, "系统分组，不允许修改"),
    /**
     * 分组名字过长
     * 45017
     */
    NAME_OF_GROUP_TOO_LONG(45017, "分组名字过长"),
    /**
     * 分组数量超过上限
     * 45018
     */
    NUMBER_OF_GROUP_EXCEEDS_UPPER_LIMIT(45018, "分组数量超过上限"),
    /**
     * 客服接口下行条数超过上限
     * 45047
     */
    NUMBER_OF_BARS_EXCEEDS_UPPER_LIMIT(45047, "客服接口下行条数超过上限"),
    /**
     * 不存在媒体数据
     * 46001
     */
    THERE_IS_NO_MEDIA_DATA(46001, "不存在媒体数据"),
    /**
     * 不存在的菜单版本
     * 46002
     */
    NONEXISTENT_MENU_VERSION(46002, "不存在的菜单版本"),
    /**
     * 不存在的菜单数据
     * 46003
     */
    NONEXISTENT_MENU_DATA(46003, "不存在的菜单数据"),
    /**
     * 不存在的用户
     * 46004
     */
    NONEXISTENT_USERS(46004, "不存在的用户"),
    /**
     * 解析 JSON/XML 内容错误
     * 47001
     */
    PARSING_CONTENT_ERRORS(47001, "解析JSON/XML内容错误"),
    /**
     * api 功能未授权，请确认公众号已获得该接口，可以在公众平台官网 - 开发者中心页中查看接口权限
     * 48001
     */
    API_UNAUTHORIZED_FUNCTION(48001, "api功能未授权"),
    /**
     * 粉丝拒收消息（粉丝在公众号选项中，关闭了 “ 接收消息 ” ）
     * 48002
     */
    VERMICELLI_REFUSED_TO_RECEIVE_MESSAGES(48002, "粉丝拒收消息"),
    /**
     * api 接口被封禁，请登录 mp.weixin.qq.com 查看详情
     * 48004
     */
    API_INTERFACE_IS_BANNED(48004, "api接口被封禁"),
    /**
     * api 禁止删除被自动回复和自定义菜单引用的素材
     * 48005
     */
    PROHIBITION_OF_REFERENCE_MATERIAL(48005, "api禁止删除被自动回复和自定义菜单引用的素材"),
    /**
     * api 禁止清零调用次数，因为清零次数达到上限
     * 48006
     */
    UPPER_LIMIT_OF_ZEROING(48006, "api禁止清零调用次数，因为清零次数达到上限"),
    /**
     * 没有该类型消息的发送权限
     * 48008
     */
    NO_TRANSMISSION_PERMISSIONS_FOR_TYPE_OF_MESSAGE(48008, "没有该类型消息的发送权限"),
    /**
     * 用户未授权该 api
     * 50001
     */
    THE_USER_NOT_AUTHORIZE_THE_API(50001, "用户未授权该api"),
    /**
     * 用户受限，可能是违规后接口被封禁
     * 50002
     */
    USER_LIMITED(50002, "用户受限"),
    /**
     * 参数错误
     * 61451
     */
    PARAMETER_ERROR(61451, "参数错误"),
    /**
     * 无效客服账号
     * 61452
     */
    INVALID_CUSTOMER_SERVICE_ACCOUNT_NUMBER(61452, "无效客服账号"),
    /**
     * 客服帐号已存在
     * 61453
     */
    CUSTOMER_SERVICE_ACCOUNTS_HAVE_ALREADY_EXISTED(61453, "客服帐号已存在"),
    /**
     * 客服帐号名长度超过限制( 仅允许 10 个英文字符，不包括 @ 及 @ 后的公众号的微信号 )(invalid kf_acount length)
     * 61454
     */
    ACCOUNT_LENGTH_EXCEEDS_LIMIT(61454, "客服帐号名长度超过限制"),
    /**
     * 客服帐号名包含非法字符 (仅允许英文 + 数字 )
     * 61455
     */
    ACCOUNT_NAME_CONTAINS_ILLEGAL_CHARACTERS(61455, "帐号名包含非法字符"),
    /**
     * 客服帐号个数超过限制 (10 个客服账号 )
     * 61456
     */
    ACCOUNT_NUMBER_MORE_THAN_LIMIT(61456, "客服帐号个数超过限制"),
    /**
     * 无效头像文件类型
     * 61457
     */
    INVALID_HEADER_FILE_TYPE(61457, "无效头像文件类型"),
    /**
     * 系统错误
     * 61450
     */
    SYSTEM_ERROR(61450, "系统错误"),
    /**
     * 日期格式错误
     * 61500
     */
    DATA_FORMAT_ERROR(61500, "日期格式错误"),
    /**
     * 不存在此 menuid 对应的个性化菜单
     * 65301
     */
    PERSONALIZED_MENU(65301, "不存在此 menuid 对应的个性化菜单"),
    /**
     * 没有相应的用户
     * 65302
     */
    NO_CORRESPONDING_USER(65302, "没有相应的用户"),
    /**
     * 没有默认菜单，不能创建个性化菜单
     * 65303
     */
    NO_DEFAULT_MENU(65303, "没有默认菜单"),
    /**
     * MatchRule 信息为空
     * 65304
     */
    MATCHRULE_INFORMATION_EMPTY(65304, "MatchRule信息为空"),
    /**
     * 个性化菜单数量受限
     * 65305
     */
    LIMITED_NUMBER_OF_PERSONALIZATION_MENUS(65305, "个性化菜单数量受限"),
    /**
     * 不支持个性化菜单的帐号
     * 65306
     */
    DO_NOT_SUPPORT_PERSONALIZED_MENU(65306, "不支持个性化菜单的帐号"),
    /**
     * 个性化菜单信息为空
     * 65307
     */
    PERSONALIZED_MENU_INFORMATION_EMPTY(65307, "个性化菜单信息为空"),
    /**
     * 包含没有响应类型的 button
     * 65308
     */
    CONTAINS_BUTTON_WITH_NO_RESPONSE_TYPE(65308, "包含没有响应类型的 button"),
    /**
     * 个性化菜单开关处于关闭状态
     * 65309
     */
    PERSONALIZED_MENU_SWITCH_CLOSED_STATE(65309, "个性化菜单开关处于关闭状态"),
    /**
     * 填写了省份或城市信息，国家信息不能为空
     * 65310
     */
    NATIONAL_INFORMATION_CAN_NOT_BE_EMPTY(65310, "国家信息不能为空"),
    /**
     * 填写了城市信息，省份信息不能为空
     * 65311
     */
    PROVINCIAL_INFORMATION_CAN_NOT_BE_EMPTY(65311, "省份信息不能为空"),
    /**
     * 不合法的国家信息
     * 65312
     */
    UNLAWFUL_STATE_INFORMATION(65312, "不合法的国家信息"),
    /**
     * 不合法的省份信息
     * 65313
     */
    UNLAWFUL_PROVINCIAL_INFORMATION(65313, "不合法的省份信息"),
    /**
     * 不合法的城市信息
     * 65314
     */
    UNLAWFUL_CITY_INFORMATION(65314, "不合法的城市信息"),
    /**
     * 该公众号的菜单设置了过多的域名外跳（最多跳转到 3 个域名的链接）
     * 65316
     */
    TOO_MANY_DNS_HAVE_BEEN_SET_UP(65316, "该公众号的菜单设置了过多的域名外跳"),
    /**
     * 不合法的 URL
     * 65317
     */
    UNLAWFUL_URL(65317, "不合法的 URL"),
    /**
     * POST 数据参数不合法
     * 9001001
     */
    POST_DATA_PARAMETERS_ARE_UNLAWFUL(9001001, "POST数据参数不合法"),
    /**
     * 远端服务不可用
     * 9001002
     */
    REMOTE_SERVICE_IS_UNAVAILABLE(9001002, "远端服务不可用"),
    /**
     * Ticket 不合法
     * 9001003
     */
    TICKET_ILLEGALITY(9001003, "Ticket不合法"),
    /**
     * 获取摇周边用户信息失败
     * 9001004
     */
    GETTING_FAILURE_WAGGING_INFORMATION(9001004, "获取摇周边用户信息失败"),
    /**
     * 获取商户信息失败
     * 9001005
     */
    ACCESS_BUSINESS_INFORMATION_FAILURE(9001005, "获取商户信息失败"),
    /**
     * 获取 OpenID 失败
     * 9001006
     */
    GETTING_OPENID_FAILURE(9001006, "获取OpenID失败"),
    /**
     * 上传文件缺失
     * 9001007
     */
    MISSING_FILE(9001007, "上传文件缺失"),
    /**
     * 上传素材的文件类型不合法
     * 9001008
     */
    UNLAWFUL_FILE_TYPE(9001008, "上传素材的文件类型不合法"),
    /**
     * 上传素材的文件尺寸不合法
     * 9001009
     */
    UNLAWFUL_FILE_SIZE(9001009, "上传素材的文件尺寸不合法"),
    /**
     * 上传失败
     * 9001010
     */
    UPLOAD_FAILURE(9001010, "上传失败"),
    /**
     * 帐号不合法
     * 9001020
     */
    ACCOUNT_UNLAWFUL(9001020, "帐号不合法"),
    /**
     * 已有设备激活率低于 50% ，不能新增设备
     * 9001021
     */
    NO_NEW_EQUIPMENT_CAN_ADDED(9001021, "已有设备激活率低于50%,不能新增设备"),
    /**
     * 设备申请数不合法，必须为大于 0 的数字
     * 9001022
     */
    MUST_NUMBER_GREATER_THAN_0(9001022, "设备申请数不合法,必须为大于0的数字"),
    /**
     * 已存在审核中的设备 ID 申请
     * 9001023
     */
    APPLICATION_IN_EXISTING_AUDITS(9001023, "已存在审核中的设备ID申请"),
    /**
     * 一次查询设备 ID 数量不能超过 50
     * 9001024
     */
    NUMBER_QUERY_DEVICES_NO_MORE_THAN_50(9001024, "一次查询设备ID数量不能超过50"),
    /**
     * 设备 ID 不合法
     * 9001025
     */
    DEVICE_ID_IS_UNLAWFUL(9001025, "设备ID不合法"),
    /**
     * 页面 ID 不合法
     * 9001026
     */
    PAGE_ID_IS_ILLEGAL(9001026, "页面ID不合法"),
    /**
     * 页面参数不合法
     * 9001027
     */
    UNLAWFUL_PAGE_PARAMETERS(9001027, "页面参数不合法"),
    /**
     * 一次删除页面 ID 数量不能超过 10
     * 9001028
     */
    NUMBER_DELETED_PAGES_ID_NOT_10(9001028, "一次删除页面ID数量不能超过 10"),
    /**
     * 页面已应用在设备中，请先解除应用关系再删除
     * 9001029
     */
    REMOVE_RELATIONSHIP_BEFORE_DELETING(9001029, "页面已应用在设备中,请先解除应用关系再删除"),
    /**
     * 一次查询页面 ID 数量不能超过 50
     * 9001030
     */
    NUMBER_QUERY_PAGES_ID_NO_MORE_THAN_50(9001030, "一次查询页面ID数量不能超过50"),
    /**
     * 时间区间不合法
     * 9001031
     */
    TIME_INTERVAL_ILLEGALITY(9001031, "时间区间不合法"),
    /**
     * 保存设备与页面的绑定关系参数错误
     * 9001032
     */
    DEVICE_AND_PAGE_PARAMETERS_ERROR(9001032, "保存设备与页面的绑定关系参数错误"),
    /**
     * 门店 ID 不合法
     * 9001033
     */
    THE_STORE_ID_UNLAWFUL(9001033, "门店ID不合法"),
    /**
     * 设备备注信息过长
     * 9001034
     */
    OVERLONG_INFORMATION_ON_EQUIPMENT_NOTES(9001034, "设备备注信息过长"),
    /**
     * 设备申请参数不合法
     * 9001035
     */
    APPLICATION_PARAMETERS_ARE_UNLAWFUL(9001035, "设备申请参数不合法"),
    /**
     * 查询起始值 begin 不合法
     * 9001036
     */
    QUERY_STARTING_VALUE_BEGIN_UNLAWFUL(9001036, "查询起始值begin不合法");


    /**
     * 状态码
     */
    private Integer code;
    /**
     * 描述
     */
    private String msg;

    private WebEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
