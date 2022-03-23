
package org.nodes.wms.core.system.wrapper;

import org.nodes.wms.core.system.entity.UserRegister;
import org.nodes.wms.core.system.vo.UserRegisterVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 在线用户登录日志
 *
 * @author pengwei
 * @since 2020-04-01
 */
public class UserRegisterWrapper extends BaseEntityWrapper<UserRegister, UserRegisterVO> {

	public static UserRegisterWrapper build() {
		return new UserRegisterWrapper();
	}

	@Override
	public UserRegisterVO entityVO(UserRegister userRegister) {
		UserRegisterVO userRegisterVO = BeanUtil.copy(userRegister, UserRegisterVO.class);
		return userRegisterVO;
	}
	public List<UserRegisterVO> entityVODay(List<UserRegister> userRegister) {
		List<UserRegisterVO> result = new ArrayList<>();
		for (UserRegister userRegis : userRegister) {
			UserRegisterVO user = BeanUtil.copy(userRegis, UserRegisterVO.class);
			user.setLastLoginDay(userRegis.getLastLoginTime().getDayOfMonth());
			result.add(user);
		}
		return result;
	}
}
