package org.nodes.wms.dao.User;

import org.nodes.wms.dao.User.dto.output.UserLoginStatusResponse;
import org.nodes.wms.dao.User.entites.UserRegister;

public interface UserRegisterDao {
	/**
	 * 更新用户签到状态
	 */
	void saveUserRegister(UserRegister userRegister);

	/**
	 * 获取用户签到状态
	 */
	UserLoginStatusResponse getLoginStatusByUserId(Long userId);
}
