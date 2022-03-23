package org.nodes.wms.core.strategy.wrapper;

import org.nodes.core.base.entity.User;
import org.nodes.core.base.cache.UserCache;
import org.nodes.core.base.service.IUserService;
import org.nodes.wms.core.strategy.entity.Outstock;
import org.nodes.wms.core.strategy.vo.OutstockVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.Objects;

public class OutstockWrapper extends BaseEntityWrapper<Outstock, OutstockVO> {

	public static OutstockWrapper build() {
		return new OutstockWrapper();
	}

	@Override
	public OutstockVO entityVO(Outstock outstock) {
		OutstockVO outstockVO = Objects.requireNonNull(BeanUtil.copy(outstock, OutstockVO.class));
		if (Func.isNotEmpty(outstockVO)) {
			User createUser = UserCache.getById(outstockVO.getCreateUser());
			if(Func.isNotEmpty(createUser)) {
				outstockVO.setCreateByName(createUser.getName());
			}
			User updateUser = UserCache.getById(outstockVO.getUpdateUser());
			if (Func.isNotEmpty(updateUser)) {
				outstockVO.setUpdateByName(updateUser.getName());
			}
			Warehouse warehouse = WarehouseCache.getById(outstock.getWhId());
			if (Func.isNotEmpty(warehouse)) {
				outstockVO.setWhName(warehouse.getWhName());
			}
		}
		return outstockVO;
	}
}
