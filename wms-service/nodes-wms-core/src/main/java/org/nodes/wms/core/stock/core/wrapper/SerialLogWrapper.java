package org.nodes.wms.core.stock.core.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.UserCache;
import org.nodes.core.base.entity.User;
import org.nodes.wms.core.stock.core.entity.Serial;
import org.nodes.wms.core.stock.core.entity.SerialLog;
import org.nodes.wms.core.stock.core.enums.SerialLogProcTypeEnum;
import org.nodes.wms.core.stock.core.vo.SerialLogVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;

/**
 * 序列号日志封装类
 *
 * @Author zx
 * @Date 2020/2/24
 **/
public class SerialLogWrapper extends BaseEntityWrapper<SerialLog, SerialLogVO> {

	public static SerialLogWrapper build() {
		return new SerialLogWrapper();
	}

	public SerialLog entity(Serial serial, SerialLogProcTypeEnum procType) {
		SerialLog serialLog = BeanUtil.copy(serial, SerialLog.class);
		if (ObjectUtil.isNotEmpty(serialLog)) {
			serialLog.setProType(procType.getIndex());
		}
		return serialLog;
	}

	@Override
	public SerialLogVO entityVO(SerialLog entity) {
		SerialLogVO serialLogVO = BeanUtil.copy(entity, SerialLogVO.class);
		if (ObjectUtil.isNotEmpty(serialLogVO)) {
			Warehouse warehouse = WarehouseCache.getById(entity.getWhId());
			if (ObjectUtil.isNotEmpty(warehouse)) {
				serialLogVO.setWhName(warehouse.getWhName());
			}
			serialLogVO.setProTypeName(DictCache.getValue("pro_type", entity.getProType()));
			User user = UserCache.getById(entity.getUpdateUser());
			if (Func.isNotEmpty(user)) {
				serialLogVO.setUpdateUserName(user.getName());

			}
		}
		return serialLogVO;
	}

}
