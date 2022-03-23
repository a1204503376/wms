create table wms3.blade_client
(
    id                      bigint(64)    not null comment '主键'
        primary key,
    client_id               varchar(48)   not null comment '客户端id',
    client_secret           varchar(256)  not null comment '客户端密钥',
    resource_ids            varchar(256)  null comment '资源集合',
    scope                   varchar(256)  not null comment '授权范围',
    authorized_grant_types  varchar(256)  not null comment '授权类型',
    web_server_redirect_uri varchar(256)  null comment '回调地址',
    authorities             varchar(256)  null comment '权限',
    access_token_validity   int           not null comment '令牌过期秒数',
    refresh_token_validity  int           not null comment '刷新令牌过期秒数',
    additional_information  varchar(4096) null comment '附加说明',
    autoapprove             varchar(256)  null comment '自动授权',
    create_user             bigint(64)    null comment '创建人',
    create_dept             bigint(64)    null comment '创建部门',
    create_time             datetime      null comment '创建时间',
    update_user             bigint(64)    null comment '修改人',
    update_time             datetime      null comment '修改时间',
    status                  int(2)        not null comment '状态',
    is_deleted              int(2)        not null comment '是否已删除'
)
    comment '客户端表' charset = utf8mb4;

INSERT INTO wms3.blade_client (id, client_id, client_secret, resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove, create_user, create_dept, create_time, update_user, update_time, status, is_deleted) VALUES (1123598811738675201, 'sword', 'sword_secret', null, 'all', 'refresh_token,password,authorization_code', 'http://localhost:8888', null, 3600, 600000, null, null, 1123598815738675201, 1123598813738675201, '2019-03-24 10:40:55', 1123598815738675201, '2019-03-24 10:40:59', 1, 0);
INSERT INTO wms3.blade_client (id, client_id, client_secret, resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove, create_user, create_dept, create_time, update_user, update_time, status, is_deleted) VALUES (1123598811738675202, 'saber', 'saber_secret', null, 'all', 'refresh_token,password,authorization_code', 'http://localhost:8080', null, 3600, 600000, null, null, 1123598815738675201, 1123598813738675201, '2019-03-24 10:42:29', 1123598815738675201, '2019-03-24 10:42:32', 1, 0);

create table wms3.blade_code
(
    id            bigint(64)       not null comment '主键'
        primary key,
    datasource_id bigint(64)       null comment '数据源主键',
    service_name  varchar(64)      null comment '服务名称',
    code_name     varchar(64)      null comment '模块名称',
    table_name    varchar(64)      null comment '表名',
    table_prefix  varchar(64)      null comment '表前缀',
    pk_name       varchar(32)      null comment '主键名',
    package_name  varchar(500)     null comment '后端包名',
    base_mode     int(2)           null comment '基础业务模式',
    wrap_mode     int(2)           null comment '包装器模式',
    api_path      varchar(2000)    null comment '后端路径',
    web_path      varchar(2000)    null comment '前端路径',
    is_deleted    int(2) default 0 null comment '是否已删除'
)
    comment '代码生成表' charset = utf8mb4;

create table wms3.blade_datasource
(
    id           bigint(64)   not null comment '主键'
        primary key,
    name         varchar(100) null comment '名称',
    driver_class varchar(100) null comment '驱动类',
    url          varchar(500) null comment '连接地址',
    username     varchar(50)  null comment '用户名',
    password     varchar(50)  null comment '密码',
    remark       varchar(255) null comment '备注',
    create_user  bigint(64)   null comment '创建人',
    create_dept  bigint(64)   null comment '创建部门',
    create_time  datetime     null comment '创建时间',
    update_user  bigint(64)   null comment '修改人',
    update_time  datetime     null comment '修改时间',
    status       int(2)       null comment '状态',
    is_deleted   int(2)       null comment '是否已删除'
)
    comment '数据源配置表' charset = utf8mb4;

INSERT INTO wms3.blade_datasource (id, name, driver_class, url, username, password, remark, create_user, create_dept, create_time, update_user, update_time, status, is_deleted) VALUES (1161483357481541634, 'mysql', 'com.mysql.cj.jdbc.Driver', 'jdbc:mysql://10.10.30.224:3306/wms3?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true&allowPublicKeyRetrieval=true', 'nodes', 'nodes', 'mysql', 1123598821738675201, 1123598813738675201, '2019-08-14 11:43:06', 1277871073639120898, '2021-01-13 10:12:19', 1, 0);
INSERT INTO wms3.blade_datasource (id, name, driver_class, url, username, password, remark, create_user, create_dept, create_time, update_user, update_time, status, is_deleted) VALUES (1161483504353484802, 'postgresql', 'org.postgresql.Driver', 'jdbc:postgresql://127.0.0.1:5432/bladex', 'postgres', '123456', 'postgresql', 1123598821738675201, 1123598813738675201, '2019-08-14 11:43:41', 1123598821738675201, '2019-12-03 15:17:34', 1, 1);
INSERT INTO wms3.blade_datasource (id, name, driver_class, url, username, password, remark, create_user, create_dept, create_time, update_user, update_time, status, is_deleted) VALUES (1161483594023510018, 'oracle', 'oracle.jdbc.OracleDriver', 'jdbc:oracle:thin:@127.0.0.1:49161:orcl', 'BLADEX', 'bladex', 'oracle', 1123598821738675201, 1123598813738675201, '2019-08-14 11:44:03', 1123598821738675201, '2019-12-03 15:17:34', 1, 1);

create table wms3.blade_dept
(
    id            bigint(64)                   not null comment '主键'
        primary key,
    tenant_id     varchar(12) default '000000' null comment '租户ID',
    parent_id     bigint(64)  default 0        null comment '父主键',
    dept_code     varchar(45)                  null comment '部门编码',
    ancestors     varchar(2000)                null comment '祖级列表',
    dept_category int(2)                       null comment '部门类型',
    dept_name     varchar(45)                  null comment '部门名',
    full_name     varchar(45)                  null comment '部门全称',
    sort          int                          null comment '排序',
    remark        varchar(255)                 null comment '备注',
    status        int(2)      default 0        null comment '是否启用',
    create_time   timestamp                    null comment '创建时间',
    create_user   bigint                       null comment '创建人',
    create_dept   bigint(64)                   null comment '机构ID',
    update_time   timestamp                    null comment '更新时间',
    update_user   bigint                       null comment '更新人',
    is_deleted    int(2)      default 0        null comment '删除标志 0：有效 1：删除'
)
    comment '机构表' charset = utf8mb4;

create index idx_blade_dept_dept_name
    on wms3.blade_dept (dept_name);

INSERT INTO wms3.blade_dept (id, tenant_id, parent_id, dept_code, ancestors, dept_category, dept_name, full_name, sort, remark, status, create_time, create_user, create_dept, update_time, update_user, is_deleted) VALUES (1377474852725809153, '000000', 0, 'NODES', '0', 1, '北京节点通', '北京节点通网络技术有限公司', 0, null, 1, '2021-04-01 12:16:12', 1123598821738675202, 1123598816738675101, '2021-04-01 12:16:12', 1123598821738675202, 0);

create table wms3.blade_dict
(
    id          bigint(64)                               not null comment '主键'
        primary key,
    parent_id   bigint(64) default 0                     null comment '父主键',
    code        varchar(30)                              null comment '字典码',
    dict_key    int(2)                                   null comment '字典值',
    dict_value  varchar(255)                             null comment '字典名称',
    sort        int                                      null comment '排序',
    remark      varchar(255)                             null comment '字典备注',
    is_sealed   int(2)     default 0                     null comment '是否已封存',
    status      int(2)                                   null comment '是否启用',
    create_time timestamp  default CURRENT_TIMESTAMP     not null on update CURRENT_TIMESTAMP comment '创建时间',
    create_user bigint                                   null comment '创建人',
    create_dept bigint(64)                               null comment '机构ID',
    update_time timestamp  default '0000-00-00 00:00:00' not null comment '更新时间',
    update_user bigint                                   null comment '更新人',
    is_deleted  int(2)     default 0                     null comment '删除标志 0：有效 1：删除',
    is_visible  int(2)     default 0                     null comment '是否显示该字典 默认：0：显示；1：隐藏',
    tenant_id   varchar(12)                              null comment '租户ID'
)
    comment '字典表' charset = utf8mb4;

create index idx_blade_dict_code_dict_key_owner
    on wms3.blade_dict (code, dict_key, tenant_id);

create table wms3.blade_log_api
(
    id           bigint(64)                             not null comment '编号'
        primary key,
    tenant_id    varchar(12)  default '000000'          null comment '租户ID',
    service_id   varchar(32)                            null comment '服务ID',
    server_host  varchar(255)                           null comment '服务器名',
    server_ip    varchar(255)                           null comment '服务器IP地址',
    env          varchar(255)                           null comment '服务器环境',
    type         char         default '1'               null comment '日志类型',
    title        varchar(255) default ''                null comment '日志标题',
    method       varchar(10)                            null comment '操作方式',
    request_uri  varchar(255)                           null comment '请求URI',
    user_agent   varchar(1000)                          null comment '用户代理',
    remote_ip    varchar(255)                           null comment '操作IP地址',
    method_class varchar(255)                           null comment '方法类',
    method_name  varchar(255)                           null comment '方法名',
    params       text                                   null comment '操作提交的数据',
    time         varchar(64)                            null comment '执行时间',
    create_by    varchar(64)                            null comment '创建者',
    create_time  datetime     default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '接口日志表' charset = utf8mb4;

create table wms3.blade_log_error
(
    id             bigint(64)                            not null comment '编号'
        primary key,
    tenant_id      varchar(12) default '000000'          null comment '租户ID',
    service_id     varchar(32)                           null comment '服务ID',
    server_host    varchar(255)                          null comment '服务器名',
    server_ip      varchar(255)                          null comment '服务器IP地址',
    env            varchar(255)                          null comment '系统环境',
    method         varchar(10)                           null comment '操作方式',
    request_uri    varchar(255)                          null comment '请求URI',
    user_agent     varchar(1000)                         null comment '用户代理',
    stack_trace    text                                  null comment '堆栈',
    exception_name varchar(255)                          null comment '异常名',
    message        text                                  null comment '异常信息',
    line_number    int                                   null comment '错误行数',
    remote_ip      varchar(255)                          null comment '操作IP地址',
    method_class   varchar(255)                          null comment '方法类',
    file_name      varchar(1000)                         null comment '文件名',
    method_name    varchar(255)                          null comment '方法名',
    params         text                                  null comment '操作提交的数据',
    create_by      varchar(64)                           null comment '创建者',
    create_time    datetime    default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '错误日志表' charset = utf8mb4;

create table wms3.blade_log_usual
(
    id           bigint(64)                            not null comment '编号'
        primary key,
    tenant_id    varchar(12) default '000000'          null comment '租户ID',
    service_id   varchar(32)                           null comment '服务ID',
    server_host  varchar(255)                          null comment '服务器名',
    server_ip    varchar(255)                          null comment '服务器IP地址',
    env          varchar(255)                          null comment '系统环境',
    log_level    varchar(10)                           null comment '日志级别',
    log_id       varchar(100)                          null comment '日志业务id',
    log_data     text                                  null comment '日志数据',
    method       varchar(10)                           null comment '操作方式',
    request_uri  varchar(255)                          null comment '请求URI',
    remote_ip    varchar(255)                          null comment '操作IP地址',
    method_class varchar(255)                          null comment '方法类',
    method_name  varchar(255)                          null comment '方法名',
    user_agent   varchar(1000)                         null comment '用户代理',
    params       text                                  null comment '操作提交的数据',
    create_by    varchar(64)                           null comment '创建者',
    create_time  datetime    default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '通用日志表' charset = utf8mb4;

create table wms3.blade_menu
(
    id          bigint(64)                           not null comment '主键'
        primary key,
    parent_id   bigint(64) default 0                 null comment '父级菜单',
    code        varchar(255)                         null comment '菜单编号',
    name        varchar(255)                         null comment '菜单名称',
    alias       varchar(255)                         null comment '菜单别名',
    path        varchar(500)                         null comment '请求地址',
    source      varchar(255)                         null comment '菜单资源',
    sort        int(2)                               null comment '排序',
    category    int(2)                               null comment '菜单类型',
    action      int(2)     default 0                 null comment '操作按钮类型',
    is_open     int(2)     default 1                 null comment '是否打开新页面',
    system_type int(2)     default 1                 not null comment '系统类型',
    remark      varchar(255)                         null comment '备注',
    status      int(2)                               null comment '是否启用',
    create_time timestamp  default CURRENT_TIMESTAMP not null comment '创建时间',
    create_user bigint                               null comment '创建人',
    create_dept bigint(64)                           null comment '机构ID',
    update_time timestamp  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    update_user bigint                               null comment '更新人',
    is_deleted  int(2)     default 0                 null comment '删除标志 0：有效 1：删除',
    is_visible  int(2)     default 0                 null comment '是否显示改菜单 默认：0：显示；1：隐藏'
)
    comment '菜单表' charset = utf8mb4;

create index idx_blade_menu_is_deleted_category_system_type
    on wms3.blade_menu (is_deleted, category, system_type);

create table wms3.blade_notice
(
    id           bigint(64)                   not null comment '主键'
        primary key,
    tenant_id    varchar(12) default '000000' null comment '租户ID',
    title        varchar(255)                 null comment '标题',
    category     int                          null comment '类型',
    release_time datetime                     null comment '发布时间',
    content      varchar(2000)                null comment '内容',
    create_user  bigint(64)                   null comment '创建人',
    create_dept  bigint(64)                   null comment '创建部门',
    create_time  datetime                     null comment '创建时间',
    update_user  bigint(64)                   null comment '修改人',
    update_time  datetime                     null comment '修改时间',
    status       int(2)                       null comment '状态',
    is_deleted   int(2)                       null comment '是否已删除'
)
    comment '通知公告表' charset = utf8mb4;

create table wms3.blade_param
(
    id          bigint(64)                            not null comment '主键'
        primary key,
    tenant_id   varchar(12) default '0'               null comment '租户',
    param_name  varchar(255)                          null comment '参数名',
    param_key   varchar(50)                           null comment '参数键',
    param_value varchar(2000)                         null comment '参数值',
    remark      varchar(255)                          null comment '备注',
    create_dept bigint(64)                            null comment '创建部门',
    is_visible  int(2)      default 0                 null comment '是否显示 默认：0：显示；1：隐藏',
    create_user bigint                                null comment '创建人',
    create_time timestamp   default CURRENT_TIMESTAMP null comment '创建时间',
    update_user bigint                                null comment '修改人',
    update_time timestamp   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
    status      int(2)                                null comment '状态',
    is_deleted  int(2)      default 0                 null comment '删除标识'
)
    comment '参数表' charset = utf8mb4;

create index idx_blade_param_param_key
    on wms3.blade_param (param_key);

create table wms3.blade_process_leave
(
    id                    bigint(64)   not null comment '编号'
        primary key,
    process_definition_id varchar(64)  null comment '流程定义主键',
    process_instance_id   varchar(64)  null comment '流程实例主键',
    start_time            datetime     null comment '开始时间',
    end_time              datetime     null comment '结束时间',
    reason                varchar(255) null comment '请假理由',
    task_user             varchar(255) null comment '第一级审批人',
    apply_time            datetime     null comment '申请时间',
    create_user           bigint(64)   null comment '创建人',
    create_dept           bigint(64)   null comment '创建部门',
    create_time           datetime     null comment '创建时间',
    update_user           bigint(64)   null comment '修改人',
    update_time           datetime     null comment '修改时间',
    status                int(2)       null comment '状态',
    is_deleted            int(2)       null comment '是否已删除'
)
    comment '流程请假业务表' charset = utf8mb4;

create table wms3.blade_report_file
(
    id          bigint(64)                not null comment '主键'
        primary key,
    name        varchar(100) charset utf8 not null comment '文件名',
    content     mediumblob                null comment '文件内容',
    create_time datetime                  null comment '创建时间',
    update_time datetime                  null comment '更新时间',
    is_deleted  int(2) default 0          null comment '是否已删除'
)
    comment '报表文件表' charset = utf8mb4;

create table wms3.blade_role
(
    id          bigint(64)                   not null comment '主键'
        primary key,
    tenant_id   varchar(12) default '000000' null comment '租户ID',
    parent_id   bigint(64)  default 0        null comment '父主键',
    role_name   varchar(255)                 null comment '角色名',
    sort        int                          null comment '排序',
    role_alias  varchar(255)                 null comment '角色别名',
    status      int(2)      default 0        null comment '是否启用',
    create_time timestamp                    null comment '创建时间',
    create_user bigint                       null comment '创建人',
    create_dept bigint(64)                   null comment '机构ID',
    update_time timestamp                    null comment '更新时间',
    update_user bigint                       null comment '更新人',
    is_deleted  int(2)      default 0        null comment '删除标志 0：有效 1：删除'
)
    comment '角色表' charset = utf8mb4;

create table wms3.blade_role_menu
(
    id          bigint(64)                              not null comment '主键'
        primary key,
    menu_id     bigint(64)                              null comment '菜单id',
    role_id     bigint(64)                              null comment '角色id',
    status      int(2)                                  null comment '是否启用',
    create_time timestamp default CURRENT_TIMESTAMP     not null on update CURRENT_TIMESTAMP comment '创建时间',
    create_user bigint                                  null comment '创建人',
    create_dept bigint(64)                              null comment '机构ID',
    update_time timestamp default '0000-00-00 00:00:00' not null comment '更新时间',
    update_user bigint                                  null comment '更新人',
    is_deleted  int(2)    default 0                     null comment '删除标志 0：有效 1：删除',
    constraint uk_blade_role_menu_menu_id_role_id
        unique (menu_id, role_id)
)
    comment '角色菜单关联表' charset = utf8mb4;

create table wms3.blade_role_scope
(
    id             bigint(64)                         not null comment '主键'
        primary key,
    scope_category int(2)                             null comment '权限类型(1:数据权限、2:接口权限)',
    scope_id       bigint(64)                         null comment '权限id',
    role_id        bigint(64)                         null comment '角色id',
    create_user    bigint                             null comment '创建人',
    create_dept    bigint(64)                         null comment '机构ID',
    create_time    datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_user    bigint                             null comment '更新人',
    update_time    datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    status         int(2)                             null comment '用户状态',
    is_deleted     int(2)                             null comment '删除标志'
)
    comment '角色数据权限关联表' charset = utf8mb4;

create table wms3.blade_scope_api
(
    id            bigint(64)   not null comment '主键'
        primary key,
    menu_id       bigint(64)   null comment '菜单主键',
    resource_code varchar(255) null comment '资源编号',
    scope_name    varchar(255) null comment '接口权限名',
    scope_path    varchar(255) null comment '接口权限地址',
    scope_type    int(2)       null comment '接口权限类型',
    remark        varchar(255) null comment '接口权限备注',
    create_user   bigint(64)   null comment '创建人',
    create_dept   bigint(64)   null comment '创建部门',
    create_time   datetime     null comment '创建时间',
    update_user   bigint(64)   null comment '修改人',
    update_time   datetime     null comment '修改时间',
    status        int(2)       null comment '状态',
    is_deleted    int(2)       null comment '是否已删除'
)
    comment '接口权限表' charset = utf8mb4;

create table wms3.blade_scope_data
(
    id            bigint(64)    not null comment '主键'
        primary key,
    menu_id       bigint(64)    null comment '菜单主键',
    resource_code varchar(255)  null comment '资源编号',
    scope_name    varchar(255)  null comment '数据权限名称',
    scope_field   varchar(255)  null comment '数据权限字段',
    scope_class   varchar(500)  null comment '数据权限类名',
    scope_column  varchar(255)  null comment '数据权限字段',
    scope_type    int(2)        null comment '数据权限类型',
    scope_value   varchar(2000) null comment '数据权限值域',
    remark        varchar(255)  null comment '数据权限备注',
    create_user   bigint(64)    null comment '创建人',
    create_dept   bigint(64)    null comment '创建部门',
    create_time   datetime      null comment '创建时间',
    update_user   bigint(64)    null comment '修改人',
    update_time   datetime      null comment '修改时间',
    status        int(2)        null comment '状态',
    is_deleted    int(2)        null comment '是否已删除'
)
    comment '数据权限表' charset = utf8mb4;

create table wms3.blade_tenant
(
    id             bigint(64)                   not null comment '主键'
        primary key,
    tenant_id      varchar(12) default '000000' null comment '租户ID',
    tenant_name    varchar(50)                  not null comment '租户名称',
    domain         varchar(255)                 null comment '域名地址',
    background_url varchar(1000)                null comment '系统背景',
    linkman        varchar(20)                  null comment '联系人',
    contact_number varchar(20)                  null comment '联系电话',
    address        varchar(255)                 null comment '联系地址',
    account_number int         default -1       null comment '账号额度',
    expire_time    datetime                     null comment '过期时间',
    datasource_id  bigint(64)                   null comment '数据源ID',
    license_key    varchar(1000)                null comment '授权码',
    wh_max         int         default 1        null comment '库房上限',
    create_user    bigint(64)                   null comment '创建人',
    create_dept    bigint(64)                   null comment '创建部门',
    create_time    datetime                     null comment '创建时间',
    update_user    bigint(64)                   null comment '修改人',
    update_time    datetime                     null comment '修改时间',
    status         int(2)                       null comment '状态',
    is_deleted     int(2)      default 0        null comment '是否已删除'
)
    comment '租户表' charset = utf8mb4;

create table wms3.blade_top_menu
(
    id          bigint(64)   not null comment '主键'
        primary key,
    tenant_id   varchar(12)  null comment '租户id',
    code        varchar(255) null comment '顶部菜单编号',
    name        varchar(255) null comment '顶部菜单名',
    source      varchar(255) null comment '顶部菜单资源',
    sort        int(2)       null comment '顶部菜单排序',
    create_user bigint(64)   null comment '创建人',
    create_dept bigint(64)   null comment '创建部门',
    create_time datetime     null comment '创建时间',
    update_user bigint(64)   null comment '修改人',
    update_time datetime     null comment '修改时间',
    status      int(2)       null comment '状态',
    is_deleted  int(2)       null comment '是否已删除'
)
    comment '顶部菜单表' charset = utf8mb4;

create table wms3.blade_top_menu_setting
(
    id          bigint(64) not null comment '主键'
        primary key,
    top_menu_id bigint(64) null comment '顶部菜单主键',
    menu_id     bigint(64) null comment '菜单主键'
)
    comment '顶部菜单配置表' charset = utf8mb4;

create table wms3.blade_user
(
    id          bigint(64)                   not null comment '主键'
        primary key,
    tenant_id   varchar(12) default '000000' null comment '租户ID',
    account     varchar(45)                  null comment '账号',
    password    varchar(45)                  null comment '密码',
    name        varchar(20)                  null comment '昵称',
    real_name   varchar(10)                  null comment '真名',
    avatar      varchar(500)                 null comment '头像',
    email       varchar(45)                  null comment '邮箱',
    phone       varchar(45)                  null comment '手机',
    birthday    datetime                     null comment '生日',
    sex         smallint                     null comment '性别',
    role_id     varchar(1000)                null comment '角色id',
    dept_id     varchar(1000)                null comment '部门id',
    create_by   bigint                       null comment '创建人',
    data_type   varchar(1)                   null comment '创建部门',
    create_time timestamp                    null comment '创建时间',
    update_by   bigint                       null comment '修改人',
    update_time timestamp                    null comment '修改时间',
    is_active   int(2)                       null comment '状态',
    del_flag    int(2)      default 0        null comment '是否已删除',
    action_id   varchar(10)                  null comment '接口ID',
    create_user bigint                       null comment '创建人',
    create_dept bigint(64)                   null comment '机构ID',
    update_user bigint                       null comment '更新人',
    is_deleted  int(2)                       null comment '删除标志',
    status      int(2)                       null comment '是否启用',
    constraint uk_blade_user_account
        unique (account, tenant_id)
)
    comment '用户表' charset = utf8mb4;

create table wms3.blade_user_app
(
    id       bigint(64)           not null comment '主键'
        primary key,
    user_id  bigint(64) default 0 null comment '用户ID',
    user_ext varchar(255)         null comment '用户拓展信息'
)
    comment '用户平台拓展表' charset = utf8mb4;

create table wms3.blade_user_dept
(
    id      bigint(64)           not null comment '主键'
        primary key,
    user_id bigint(64) default 0 null comment '用户ID',
    dept_id bigint(64) default 0 null comment '部门ID'
)
    comment '用户部门表' charset = utf8mb4;

create table wms3.blade_user_oauth
(
    id        bigint(64)    not null comment '主键'
        primary key,
    tenant_id varchar(12)   null comment '租户ID',
    uuid      varchar(64)   null comment '第三方系统用户ID',
    user_id   bigint(64)    null comment '用户ID',
    username  varchar(32)   null comment '账号',
    nickname  varchar(64)   null comment '用户名',
    avatar    varchar(1000) null comment '头像',
    blog      varchar(50)   null comment '应用主页',
    company   varchar(255)  null comment '公司名',
    location  varchar(255)  null comment '地址',
    email     varchar(255)  null comment '邮件',
    remark    varchar(255)  null comment '备注',
    gender    varchar(16)   null comment '性别',
    source    varchar(16)   null comment '来源'
)
    comment '用户第三方认证表' charset = utf8mb4;

create table wms3.blade_user_other
(
    id       bigint(64)           not null comment '主键'
        primary key,
    user_id  bigint(64) default 0 null comment '用户ID',
    user_ext varchar(255)         null comment '用户拓展信息'
)
    comment '用户平台拓展表' charset = utf8mb4;

create table wms3.blade_user_web
(
    id       bigint(64)           not null comment '主键'
        primary key,
    user_id  bigint(64) default 0 null comment '用户ID',
    user_ext varchar(255)         null comment '用户拓展信息'
)
    comment '用户平台拓展表' charset = utf8mb4;
-- 盘点记录表新增实际库存量字段【关联Tapd任务ID:1000338】
alter table wms_count_record add column stock_qty decimal(20) null comment '实际库存量' after stock_id;
-- 库存明细表新增库房ID字段
alter table wms_stock_detail add column wh_id bigint(20) null comment '库房ID' after stock_id;
