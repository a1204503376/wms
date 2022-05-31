package org.nodes.wms.pdaBiz.user.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.pdaBiz.user.UserBiz;
import org.nodes.wms.pdaDao.User.UserRegisterDao;
import org.nodes.wms.pdaDao.User.dto.input.EditUserLoginStatusRequest;
import org.nodes.wms.pdaDao.User.dto.output.UserLoginStatusResponse;
import org.nodes.wms.pdaDao.User.entites.UserRegister;
import org.nodes.wms.pdaDao.User.enums.PlatformEnum;
import org.nodes.wms.pdaDao.User.enums.UserTypeEnum;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.StringPool;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 手持用户接口实现类
 */
@Service
@RequiredArgsConstructor
public class UserBizImpl implements UserBiz {
	private final UserRegisterDao userRegisterDao;
	@Override
	public UserLoginStatusResponse EditUserLoginStatus(EditUserLoginStatusRequest editUserLoginStatusRequest) {
		UserRegister userRegister = new UserRegister();
		userRegister.setUserId(AuthUtil.getUserId());
		userRegister.setUserCode(StringPool.ONE);
		userRegister.setUserName(AuthUtil.getNickName());
		userRegister.setUserType(UserTypeEnum.CUSTOM.getIndex());
		userRegister.setOnlineTerminal(PlatformEnum.PDA.getName());
		userRegister.setToken(editUserLoginStatusRequest.getToken());
		userRegister.setLastLoginTime(LocalDateTime.now());
		if(editUserLoginStatusRequest.getLoginStatus()==1){
			userRegister.setLoginStatus(0);
		} else{
			userRegister.setLoginStatus(1);
		}
		userRegisterDao.saveUserRegister(userRegister);
		UserLoginStatusResponse userLoginStatusResponse = new UserLoginStatusResponse();
		userLoginStatusResponse.setLoginStatus(userRegister.getLoginStatus());
		userLoginStatusResponse.setLastLoginTime(userRegister.getLastLoginTime());
        return userLoginStatusResponse;
	}

	@Override
	public UserLoginStatusResponse getLoginStatus(BladeUser user) {
		return  userRegisterDao.getLoginStatusByUserId(user.getUserId());
	}
}
