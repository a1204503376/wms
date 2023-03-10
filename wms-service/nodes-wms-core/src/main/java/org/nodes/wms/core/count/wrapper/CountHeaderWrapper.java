package org.nodes.wms.core.count.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.UserCache;
import org.nodes.core.base.entity.User;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.wms.core.count.entity.CountHeader;
import org.nodes.wms.core.count.vo.CountHeaderVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;

public class CountHeaderWrapper extends BaseEntityWrapper<CountHeader, CountHeaderVO> {
	public static CountHeaderWrapper build() {
		return new CountHeaderWrapper();
	}

	@Override
	public CountHeaderVO entityVO(CountHeader entity) {
		CountHeaderVO countHeaderVO = BeanUtil.copy(entity, CountHeaderVO.class);
		if (ObjectUtil.isNotEmpty(countHeaderVO)) {
			Warehouse warehouse = WarehouseCache.getById(countHeaderVO.getWhId());
			if (Func.isNotEmpty(warehouse)) {
				countHeaderVO.setWhName(warehouse.getWhName());
			}
			countHeaderVO.setCountBillStateName(DictCache.getValue(
				"stockcount_state",
				countHeaderVO.getCountBillState())
			);
				countHeaderVO.setCountTagName(DictCache.getValue(
					"stockcount_type",
					countHeaderVO.getCountTag())
				);
			if (Func.isNotEmpty(entity.getCreator()) && !"系统".equals(entity.getCreator()) && !"管理员".equals(entity.getCreator())) {
				User user = UserCache.getById(Long.valueOf(entity.getCreator()));
				if (Func.isNotEmpty(user)) {
					countHeaderVO.setCreator(user.getName());
				}
			} else if (Func.isNotEmpty(entity.getCreateUser())) {
				User user = UserCache.getById(Long.valueOf(entity.getCreateUser()));
				if (Func.isNotEmpty(user)) {
					countHeaderVO.setCreator(user.getName());
				}
			}
			countHeaderVO.setSyncStateDesc(DictCache.getValue(DictCodeConstant.SYNC_STATE, countHeaderVO.getSyncState()));
		}
		return countHeaderVO;
	}
}
