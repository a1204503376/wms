package org.nodes.wms.core.instock.asn.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.enums.ZoneVirtualTypeEnum;
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseEntity;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbsBaseAsnHeaderService<M extends BaseMapper<T>, T extends BaseEntity> extends BaseServiceImpl<M,T>{
	/**
	 * 收货校验库位有效性
	 * @param locCode
	 * @return
	 */
	public Location checkLoc(Long whId, String locCode){
		Warehouse warehouse = WarehouseCache.getById(whId);
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("库房不存在或者已删除");
		}
		if (ZoneVirtualTypeEnum.isVirtual(locCode)) {
			locCode = Func.firstCharToUpper(locCode.toLowerCase()) + warehouse.getWhCode();
		}
		ILocationService locationService = SpringUtil.getBean(ILocationService.class);
		//List<Location> locations = LocationCache.listByWhId(whId);
		List<Location> locations = locationService.list(Condition.getQueryWrapper(new Location())
		.lambda()
		.in(Location::getWhId,whId)
		).stream().collect(Collectors.toList());
		if(Func.isEmpty(locations)){
			throw new ServiceException("该库房还没有库位！");
		}
		String finalLocCode = locCode;
		Location location1 = locations.stream()
			.filter(location -> location.getLocCode().equals(finalLocCode))
			.findFirst().orElse(null);
		if(Func.isEmpty(location1)){
			throw new ServiceException(String.format("库位[%s]在库房[%s]中不存在！",locCode,warehouse.getWhName()));
		}
		Location location = LocationCache.getValid(locCode);
//		if (Func.isNotEmpty(location.getCountBillNo())) {
//			throw new ServiceException(
//				String.format("库位[%s]已被盘点单[%s]占用", location.getLocCode(), location.getCountBillNo()));
//		}
		return location;
	}


}
