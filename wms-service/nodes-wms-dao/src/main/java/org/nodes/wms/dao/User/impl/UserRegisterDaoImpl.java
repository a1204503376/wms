package org.nodes.wms.dao.User.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.User.UserRegisterDao;
import org.nodes.wms.dao.User.dto.output.UserLoginStatusResponse;
import org.nodes.wms.dao.User.entites.UserRegister;
import org.nodes.wms.dao.User.mapper.UserRegisterMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;


/**
 * 手持用户 DAO实现类
 */
@Repository
@RequiredArgsConstructor
public class UserRegisterDaoImpl extends BaseServiceImpl<UserRegisterMapper, UserRegister> implements UserRegisterDao {
	@Override
	public void saveUserRegister(UserRegister userRegister) {
		super.save(userRegister);
	}

	@Override
	public UserLoginStatusResponse getLoginStatusByUserId(Long userId) {
		return super.baseMapper.selectLoginStatusByUserId(userId);
	}
}
