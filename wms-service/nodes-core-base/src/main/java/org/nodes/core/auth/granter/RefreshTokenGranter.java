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
package org.nodes.core.auth.granter;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.nodes.core.auth.provider.ITokenGranter;
import org.nodes.core.auth.provider.TokenParameter;
import org.nodes.core.base.entity.Tenant;
import org.nodes.core.base.entity.UserInfo;
import org.nodes.core.base.service.ITenantService;
import org.nodes.core.base.service.IUserService;
import org.nodes.core.utils.TokenUtil;
import org.springblade.core.launch.constant.TokenConstant;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Component;

/**
 * RefreshTokenGranter
 *
 * @author Chill
 */
@Component
@AllArgsConstructor
public class RefreshTokenGranter implements ITokenGranter {

	public static final String GRANT_TYPE = "refresh_token";

	private final IUserService userService;
	private final ITenantService tenantService;

	@Override
	public UserInfo grant(TokenParameter tokenParameter) {
		String tenantId = tokenParameter.getArgs().getStr("tenantId");
		String grantType = tokenParameter.getArgs().getStr("grantType");
		String refreshToken = tokenParameter.getArgs().getStr("refreshToken");
		UserInfo userInfo = null;
		if (Func.isNoneBlank(grantType, refreshToken) && grantType.equals(TokenConstant.REFRESH_TOKEN)) {
			Claims claims = AuthUtil.parseJWT(refreshToken);
			if (claims != null) {
				String tokenType = Func.toStr(claims.get(TokenConstant.TOKEN_TYPE));
				if (tokenType.equals(TokenConstant.REFRESH_TOKEN)) {
					// 获取租户信息
					Tenant tenant = tenantService.getByTenantId(tenantId);
					if (TokenUtil.judgeTenant(tenant)) {
						throw new ServiceException(TokenUtil.USER_HAS_NO_TENANT_PERMISSION);
					}
					// 获取用户信息
					userInfo = userService.userInfo(Func.toLong(claims.get(TokenConstant.USER_ID)));
				}
			}
		}
		return userInfo;
	}
}
