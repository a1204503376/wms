package org.nodes.wms.core.warehouse.wrapper;

import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.entity.User;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.UserCache;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.base.service.IUserService;
import org.nodes.core.constant.DictConstant;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.dto.ZoneDTO;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.entity.Zone;
import org.nodes.wms.core.warehouse.excel.ZoneExcel;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.core.warehouse.vo.ZoneVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringPool;

/**
 * 库区管理 封装类
 *
 * @author zhongls
 * @since 2019-12-06
 */
public class ZoneWrapper extends BaseEntityWrapper<Zone, ZoneVO> {
	public static ZoneWrapper build() {
		return new ZoneWrapper();
	}

	@Override
	public ZoneVO entityVO(Zone zone) {
		ZoneVO zoneVO = BeanUtil.copy(zone, ZoneVO.class);
		if (Func.isNotEmpty(zoneVO)) {
			User createUser = UserCache.getById(zoneVO.getCreateUser());
			if (Func.isNotEmpty(createUser)) {
				zoneVO.setCreateByName(createUser.getName());
			}
			User updateUser = UserCache.getById(zoneVO.getUpdateUser());
			if (Func.isNotEmpty(updateUser)) {
				zoneVO.setUpdateByName(updateUser.getName());
			}
			Warehouse warehouse = WarehouseCache.getById(zone.getWhId());
			if (Func.isNotEmpty(warehouse)) {
				zoneVO.setWhName(warehouse.getWhName());
			}
			zoneVO.setZoneTypeDesc(DictCache.getValue(DictConstant.ZONE_TYPE,zone.getZoneType()));
		}
		return zoneVO;
	}

	public ZoneDTO entityDTO(ZoneExcel zoneExcel) {
		ZoneDTO zoneDTO = BeanUtil.copy(zoneExcel, ZoneDTO.class);
		return zoneDTO;
	}

}
