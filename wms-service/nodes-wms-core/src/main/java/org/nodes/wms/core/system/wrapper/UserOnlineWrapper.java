
package org.nodes.wms.core.system.wrapper;

import org.nodes.wms.core.system.entity.UserOnline;
import org.nodes.wms.core.system.vo.UserOnlineVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 在线用户包装类,返回视图层所需的字段
 *
 * @author pengwei
 * @since 2020-04-01
 */
public class UserOnlineWrapper extends BaseEntityWrapper<UserOnline, UserOnlineVO> {

	public static UserOnlineWrapper build() {
		return new UserOnlineWrapper();
	}

	@Override
	public UserOnlineVO entityVO(UserOnline userOnline) {
		UserOnlineVO userOnlineVO = BeanUtil.copy(userOnline, UserOnlineVO.class);

		return userOnlineVO;
	}

}
