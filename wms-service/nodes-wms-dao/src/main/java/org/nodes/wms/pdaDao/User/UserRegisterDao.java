package org.nodes.wms.pdaDao.User;

import org.nodes.wms.pdaDao.User.dto.output.UserLoginStatusResponse;
import org.nodes.wms.pdaDao.User.entites.UserRegister;

public interface UserRegisterDao {
	void saveUserRegister(UserRegister userRegister);

	UserLoginStatusResponse getLoginStatusByUserId(Long userId);
}
