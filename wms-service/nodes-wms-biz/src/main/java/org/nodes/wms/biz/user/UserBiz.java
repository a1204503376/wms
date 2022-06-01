package org.nodes.wms.biz.user;

import org.nodes.wms.dao.User.dto.input.EditUserLoginStatusRequest;
import org.nodes.wms.dao.User.dto.output.UserLoginStatusResponse;
import org.springblade.core.secure.BladeUser;

/**
 * 手持用户模块业务层接口
 */
public interface UserBiz {
	/**
	 * 手持端修改用户签到状态
	 */
    UserLoginStatusResponse EditUserLoginStatus(EditUserLoginStatusRequest editUserLoginStatusRequest);

	/**
	 * 手持获取用户签到状态
	 * @param user 用户信息
	 */
	UserLoginStatusResponse getLoginStatus(BladeUser user);
}
