package org.nodes.wms.core.warehouse.wrapper;


import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictConstant;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.dto.LocationDTO;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.excel.LocationExcel;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.core.warehouse.vo.LocationVO;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

public class LocationWrapper extends BaseEntityWrapper<Location, LocationVO> {

	public static LocationWrapper build() {
		return new LocationWrapper();
	}

	@Override
	public LocationVO entityVO(Location entity) {
		LocationVO locationVO = BeanUtil.copy(entity, LocationVO.class);
		if (Func.isNotEmpty(locationVO)) {
			// 库房
			Warehouse warehouse = WarehouseCache.getById(entity.getWhId());
			if (Func.isNotEmpty(warehouse)) {
				locationVO.setWhName(warehouse.getWhName());
			}
			// 库区
			IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
			Zone zone = zoneService.getById(entity.getZoneId());
			if (Func.isNotEmpty(zone)) {
				locationVO.setZoneName(zone.getZoneName());
			}
			locationVO.setLocTypeDesc(DictCache.getValue(DictConstant.LOC_TYPE, locationVO.getLocType()));
			locationVO.setLocCategoryDesc(DictCache.getValue(DictConstant.LOC_CATEGORY, locationVO.getLocCategory()));
			locationVO.setLocHandlingDesc(DictCache.getValue(DictConstant.LOC_HANDLING, locationVO.getLocHandling()));
			//使用状态
			locationVO.setLocFlagDesc(DictCache.getValue(DictConstant.LOC_FLAG, locationVO.getLocFlag()));
			locationVO.setAbcDesc(DictCache.getValue(DictConstant.LOC_ABC, locationVO.getAbc()));
		}
		return locationVO;
	}

	public LocationDTO entityDTO(LocationExcel locationExcel) {
		LocationDTO locationDTO = BeanUtil.copy(locationExcel, LocationDTO.class);
		return locationDTO;
	}
}
