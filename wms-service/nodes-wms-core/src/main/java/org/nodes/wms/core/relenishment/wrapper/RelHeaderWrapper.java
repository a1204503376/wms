
package org.nodes.wms.core.relenishment.wrapper;

import org.nodes.core.base.cache.UserCache;
import org.nodes.core.base.entity.User;
import org.nodes.core.base.service.IUserService;
import org.nodes.wms.core.relenishment.entity.RelHeader;
import org.nodes.wms.core.relenishment.enums.RelStateEnum;
import org.nodes.wms.core.relenishment.vo.RelHeaderVo;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;


/**
 * 库存包装类,返回视图层所需的字段
 *
 * @author pengwei
 * @since 2019-12-24
 */
public class RelHeaderWrapper extends BaseEntityWrapper<RelHeader, RelHeaderVo> {

	public static RelHeaderWrapper build() {
		return new RelHeaderWrapper();
	}

	@Override
	public RelHeaderVo entityVO(RelHeader relHeader) {
		RelHeaderVo relHeaderVo = BeanUtil.copy(relHeader, RelHeaderVo.class);
		if (Func.isNotEmpty(relHeaderVo)) {
			switch (relHeader.getRelBillState()){
				case 10:
					relHeaderVo.setRelBillStateDesc(RelStateEnum.UNEXECUTED.getName());
					break;
				case 20:
					relHeaderVo.setRelBillStateDesc(RelStateEnum.EXECUTING.getName());
					break;
				case 30:
					relHeaderVo.setRelBillStateDesc(RelStateEnum.EXECUTED.getName());
					break;
			}
			User user = UserCache.getById(relHeader.getCreateUser());
			if (Func.isNotEmpty(user)){
				relHeaderVo.setCreateUserName(user.getName());
			}
		}
		return relHeaderVo;
	}
}
