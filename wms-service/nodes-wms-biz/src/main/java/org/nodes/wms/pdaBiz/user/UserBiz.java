package org.nodes.wms.pdaBiz.user;

import org.nodes.wms.pdaDao.User.dto.input.EditUserLoginStatusRequest;
import org.nodes.wms.pdaDao.User.dto.output.UserLoginStatusResponse;
import org.nodes.wms.pdaDao.User.entites.UserRegister;
import org.springblade.core.secure.BladeUser;

/**
 * 手持用户模块业务层接口
 */
public interface UserBiz {
	/**
	 * 手持端修改用户签到状态
	 */
    UserLoginStatusResponse EditUserLoginStatus(EditUserLoginStatusRequest editUserLoginStatusRequest);

	UserLoginStatusResponse getLoginStatus(BladeUser user);
}
