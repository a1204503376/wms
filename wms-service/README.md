## Nodes-Tool是什么
* Nodes-Tool 是一个基于 Spring Boot 2 & Spring Cloud Greenwich & Mybatis 等核心技术，用于快速构建WMS的基础框架
* 已稳定生产近一年，经历了从Camden->Greenwich的技术架构，也经历了从FatJar->Docker->K8S+Jenkins的部署架构
* 采用前后端分离的模式，前端开发基于Vue、ElementUI
* 后端采用SpringCloud系列，对其基础组件做了高度的封装，单独出一个后端核心框架：Nodes-Tool
* Nodes-Tool已推送至Maven私有库，直接引入减少工程的模块与依赖，可更注重于业务开发
* 集成Sentinel从流量控制、熔断降级、系统负载保护等多个维度保护服务的稳定性
* 注册中心、配置中心选型Nacos，为工程瘦身的同时加强各模块之间的联动
* 使用Traefik进行反向代理，监听后台变化自动化应用新的配置文件
* 集成Oauth2协议，完美支持了多终端的接入与认证授权
* 集成工作流Flowable，复杂流程需求不再难办
* 创建多租户模式，发布简单，数据隔离轻松
* 项目分包明确，规范微服务的开发模式

## 商业授权
* 您一旦开始复制、下载、安装或者使用本产品，即被视为完全理解并接受本协议的各项条款
* 更多详情请看：[Nodes-Tool商业授权许可协议]

## 官网
* 官网地址：[http://www.nodes.com.cn](http://www.nodes.com.cn)

## 在线演示
* WMS演示地址：[http://114.215.69.116:8090/](http://114.215.69.116:8090/)

## 工程结构
``` 
Nodes-Tool
├── nodes-core-base     核心基础功能
├── nodes-core-tool     核心工具类
├── nodes-wms-core      WMS 核心功能
├── nodes-wms-report    报表、查询统计等
```

# 界面
## 监控界面一览
<table>
    <tr>
        <td><img src="https://gitee.com/smallc/SpringBlade/raw/master/pic/springblade-k8s1.png"/></td>
        <td><img src="https://gitee.com/smallc/SpringBlade/raw/master/pic/springblade-k8s2.png"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/smallc/SpringBlade/raw/master/pic/springblade-grafana.png"/></td>
        <td><img src="https://gitee.com/smallc/SpringBlade/raw/master/pic/springblade-harbor.png"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/smallc/SpringBlade/raw/master/pic/springblade-traefik.png"/></td>
        <td><img src="https://gitee.com/smallc/SpringBlade/raw/master/pic/springblade-traefik-health.png"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/smallc/SpringBlade/raw/master/pic/springblade-nacos.png"/></td>
        <td><img src="https://gitee.com/smallc/SpringBlade/raw/master/pic/springblade-sentinel.png"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/smallc/SpringBlade/raw/master/pic/springblade-admin1.png"/></td>
        <td><img src="https://gitee.com/smallc/SpringBlade/raw/master/pic/springblade-admin2.png"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/smallc/SpringBlade/raw/master/pic/springblade-swagger1.png"/></td>
        <td><img src="https://gitee.com/smallc/SpringBlade/raw/master/pic/springblade-swagger2.png"/></td>
    </tr>
</table>