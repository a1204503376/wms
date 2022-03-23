/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.nodes.common.config;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.nodes.core.tool.constant.AppConstant;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.swagger.EnableSwagger;
import org.springblade.core.swagger.SwaggerProperties;
import org.springblade.core.swagger.SwaggerUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Swagger配置类
 *
 * @author Chill
 */
@Configuration
@EnableSwagger
@AllArgsConstructor
public class SwaggerConfiguration {

	/**
	 * 引入swagger配置类
	 */
	private final SwaggerProperties swaggerProperties;

	/**
	 * 引入Knife4j扩展类
	 */
	private final OpenApiExtensionResolver openApiExtensionResolver;

	@Bean
	public Docket authDocket() {
		return docket("授权模块", Arrays.asList("org.nodes.modules.auth"));
	}

	@Bean
	public Docket sysDocket() {
		return docket("系统模块", Arrays.asList("org.nodes.modules.core"));
	}

	@Bean
	public Docket wmsBasedataDocket(){
		return docket("WMS-基础数据", Arrays.asList("org.nodes.modules.wms.basedata"));
	}

	@Bean
	public Docket wmsWarehouseDocket(){
		return docket("WMS-库房数据", Arrays.asList("org.nodes.modules.wms.warehouse"));
	}

	@Bean
	public Docket wmsAllotDocket(){
		return docket("调拨单", Arrays.asList("org.nodes.modules.wms.allot"));
	}

	@Bean
	public Docket wmsApiDocket(){
		return docket("手持接口", Arrays.asList("org.nodes.modules.wms.api"));
	}

	@Bean
	public Docket wmsCoreDocket(){
		return docket("WMS核心", Arrays.asList("org.nodes.modules.wms.core"));
	}

	@Bean
	public Docket wmsBillingDocket(){
		return docket("计费管理", Arrays.asList("org.nodes.modules.wms.billing"));
	}

	@Bean
	public Docket wmsCountDocket(){
		return docket("盘点单管理", Arrays.asList("org.nodes.modules.wms.count"));
	}

	@Bean
	public Docket wmsCrontabDocket(){
		return docket("定时任务", Arrays.asList("org.nodes.modules.wms.crontab"));
	}

	@Bean
	public Docket wmsInstockDocket(){
		return docket("入库单管理", Arrays.asList("org.nodes.modules.wms.instock"));
	}

	@Bean
	public Docket wmsLogDocket(){
		return docket("日志模块", Arrays.asList("org.nodes.modules.wms.log"));
	}

	@Bean
	public Docket wmsOutStockDocket(){
		return docket("出库单管理", Arrays.asList("org.nodes.modules.wms.outstock"));
	}

	@Bean
	public Docket wmsStatisticsDocket(){
		return docket("统计模块", Arrays.asList("org.nodes.modules.wms.statistics"));
	}

	@Bean
	public Docket wmsStockDocket(){
		return docket("库存管理", Arrays.asList("org.nodes.modules.wms.stock"));
	}

	@Bean
	public Docket wmsStrategyDocket(){
		return docket("策略管理", Arrays.asList("org.nodes.modules.wms.strategy"));
	}


	private Docket docket(String groupName, List<String> basePackages) {
		return new Docket(DocumentationType.SWAGGER_2)
			.groupName(groupName)
			.apiInfo(apiInfo())
			.ignoredParameterTypes(BladeUser.class)
			.select()
			.apis(SwaggerUtil.basePackages(basePackages))
			.paths(PathSelectors.any())
			.build().securityContexts(securityContexts()).securitySchemes(securitySchemas())
			.extensions(openApiExtensionResolver.buildExtensions(groupName));
	}

	private List<SecurityContext> securityContexts() {
		return Collections.singletonList(SecurityContext.builder()
			.securityReferences(defaultAuth())
			.forPaths(PathSelectors.regex("^.*$"))
			.build());
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverywhere");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Lists.newArrayList(new SecurityReference(SwaggerUtil.clientInfo().getName(), authorizationScopes),
			new SecurityReference(SwaggerUtil.bladeAuth().getName(), authorizationScopes),
			new SecurityReference(SwaggerUtil.bladeTenant().getName(), authorizationScopes));
	}

	private List<SecurityScheme> securitySchemas() {
		return Lists.newArrayList(SwaggerUtil.clientInfo(), SwaggerUtil.bladeAuth(), SwaggerUtil.bladeTenant());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title(swaggerProperties.getTitle())
			.description(swaggerProperties.getDescription())
			.license(swaggerProperties.getLicense())
			.licenseUrl(swaggerProperties.getLicenseUrl())
			.termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
			.contact(new Contact(swaggerProperties.getContact().getName(), swaggerProperties.getContact().getUrl(), swaggerProperties.getContact().getEmail()))
			.version(swaggerProperties.getVersion())
			.build();
	}

}
