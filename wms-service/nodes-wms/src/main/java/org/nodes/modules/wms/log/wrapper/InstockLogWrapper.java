package org.nodes.modules.wms.log.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.UserCache;
import org.nodes.core.base.entity.User;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.modules.wms.log.vo.InstockLogVO;
import org.nodes.wms.dao.putway.entities.InstockLog;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;


/**
 * 上架记录包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2020-04-27
 */
public class InstockLogWrapper extends BaseEntityWrapper<InstockLog, InstockLogVO> {

	public static InstockLogWrapper build() {
		return new InstockLogWrapper();
	}

	@Override
	public InstockLogVO entityVO(InstockLog instockLog) {
		InstockLogVO instockLogVO = BeanUtil.copy(instockLog, InstockLogVO.class);
		//库房名称
		Warehouse warehouse = WarehouseCache.getById(instockLog.getWhId());
		if (Func.isNotEmpty(warehouse) && Func.isNotEmpty(warehouse.getWhName())) {
			instockLogVO.setWhName(warehouse.getWhName());
		}
		//上架计算代码名称
		instockLogVO.setInstockFunctionName(
			DictCache.getValue(DictConstant.INSTOCK_FUNCTION, instockLog.getInstockFunction()));
		//是否成功名称
		if (Func.isNotEmpty(instockLog.getIsSuccess())) {
			if (instockLog.getIsSuccess() == 1) {
				instockLogVO.setIsSuccessName(StringPool.CHS_YES);
			} else {
				instockLogVO.setIsSuccessName(StringPool.CHS_NO);
			}
		}
		User user = UserCache.getById(instockLog.getCreateUser());
		if (Func.isNotEmpty(user)) {
			instockLogVO.setCreateUserCode(user.getAccount());
			instockLogVO.setCreateUserName(user.getName());
		}
		return instockLogVO;
	}

}
