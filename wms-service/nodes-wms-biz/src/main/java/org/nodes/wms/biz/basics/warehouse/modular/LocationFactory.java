package org.nodes.wms.biz.basics.warehouse.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.lpntype.LpnTypeBiz;
import org.nodes.wms.biz.basics.warehouse.WarehouseBiz;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.dao.basics.location.LocationDao;
import org.nodes.wms.dao.basics.location.dto.input.LocationAddOrEditRequest;
import org.nodes.wms.dao.basics.location.dto.input.LocationExcelRequest;
import org.nodes.wms.dao.basics.location.dto.output.LocationEditResponse;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.lpntype.entities.LpnType;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationFactory {

	private final WarehouseBiz warehouseBiz;

	private final ZoneBiz zoneBiz;

	private final LpnTypeBiz lpnTypeBiz;

	private final LocationDao locationDao;

	public List<Location> createLocationListForImport(List<LocationExcelRequest> importDataList) {
		List<Location> locationList = new ArrayList<>();
		for (LocationExcelRequest data : importDataList) {
			Location location = new Location();
			Func.copy(data, location);
			if (Func.isEmpty(data.getLocCode())) {
				throw new ServiceException("导入失败，库位编码不能为空");
			}
			if (Func.isEmpty(data.getWhCode())) {
				throw new ServiceException("导入失败，库房编码不能为空");
			}
			if (Func.isEmpty(data.getZoneCode())) {
				throw new ServiceException("导入失败，库区编码不能为空");
			}

			// 根据库房编码查询库房信息
			if (Func.isNotEmpty(data.getWhCode())) {
				Warehouse warehouse = warehouseBiz.findByCode(data.getWhCode());
				if (Func.isEmpty(warehouse)) {
					throw new ServiceException("导入失败，不存在库房编码：" + data.getWhCode());
				} else {
					location.setWhId(warehouse.getWhId());
				}
			}
			// 根据库区编码查询库区信息
			if (Func.isNotEmpty(data.getZoneCode())) {
				Zone zone = zoneBiz.findByCode(data.getZoneCode());
				if (Func.isEmpty(zone)) {
					throw new ServiceException("导入失败，不存在库区编码：" + data.getZoneCode());
				} else {
					location.setZoneId(zone.getZoneId());
				}
			}

			// 判断库位编码是否已存在，存在则更新库位信息
			List<Location> locations = locationDao.getLocationByWhId(location.getWhId());
			List<String> locCodeList = locations.stream()
				.map(Location::getLocCode)
				.collect(Collectors.toList());
			if (Func.isNotEmpty(locCodeList) && locCodeList.contains(location.getLocCode())) {
				Location locationByLocCode = locationDao.getLocationByLocCode(location.getWhId(), location.getLocCode());
				location.setLocId(locationByLocCode.getLocId());
			}

			//判断容器类别
			if (Func.isNotEmpty(data.getLpnSortCode())) {
				LpnType lpnType = lpnTypeBiz.findLpnTypeByBoxCode(data.getLpnSortCode());
				if (Func.isNotEmpty(lpnType)) {
					location.setLpnTypeId(lpnType.getId());
				}
			}

			locationList.add(location);
		}
		return locationList;
	}

	public Location createLocation(LocationAddOrEditRequest locationAddOrEditRequest) {
		Location location = new Location();
		Func.copy(locationAddOrEditRequest, location);
		return location;
	}

	/**
	 * 生成编辑返回dto
	 *
	 * @param location: 库位实体
	 * @return null
	 */
	public LocationEditResponse createLocationEditResponse(Location location) {
		LocationEditResponse locationEditResponse = new LocationEditResponse();
		// 库位实体复制给库位dto
		Func.copy(location, locationEditResponse);
		return locationEditResponse;
	}
}
