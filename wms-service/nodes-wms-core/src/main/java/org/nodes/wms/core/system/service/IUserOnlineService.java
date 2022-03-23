
package org.nodes.wms.core.system.service;

import org.nodes.wms.core.system.entity.UserOnline;
import org.nodes.wms.core.system.vo.UserRegisterVO;
import org.springblade.core.mp.base.BaseService;

/**
 * 在线用户 服务类
 *
 * @author pengwei
 * @since 2020-04-01
 */

public interface IUserOnlineService extends BaseService<UserOnline> {

	/**
	 * 签到/签退
	 * @param userOnline 用户在线状态信息
	 * @return UserRegisterVO
	 */
	UserRegisterVO sign(UserOnline userOnline);
}
