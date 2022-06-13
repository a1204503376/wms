package org.nodes.wms.biz.basics.warehouse.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.core.enums.StatusEnum;
import org.nodes.wms.biz.basics.lpntype.LpnTypeBiz;
import org.nodes.wms.biz.basics.warehouse.WarehouseBiz;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.dao.basics.lpntype.entities.LpnType;
import org.nodes.wms.dao.basics.warehouse.LocationDao;
import org.nodes.wms.dao.basics.warehouse.dto.input.LocationAddOrEditRequest;
import org.nodes.wms.dao.basics.warehouse.dto.input.LocationExcelRequest;
import org.nodes.wms.dao.basics.warehouse.dto.output.LocationEditResponse;
import org.nodes.wms.dao.basics.warehouse.entities.Location;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
			if (Func.isEmpty(data.getLocCode())) {
				throw new ServiceException("导入失败，库位编码不能为空");
			}
			if (Func.isEmpty(data.getWhCode())) {
				throw new ServiceException("导入失败，库房编码不能为空");
			}
			if (Func.isEmpty(data.getZoneCode())) {
				throw new ServiceException("导入失败，库区编码不能为空");
			}
			if (Func.isEmpty(data.getStatus())) {
				throw new ServiceException("导入失败，启用状态不能为空");
			}
			// 判断库位编码是否已存在
			boolean isExist = locationDao.isExistLocationCode(data.getLocCode());
			if (isExist) {
				throw new ServiceException("导入失败，库位编码[" + data.getLocCode() + "]已存在");
			} else {
				location.setLocCode(data.getLocCode());
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
			// 根据容器类型编码查询容器类型信息
			if (Func.isNotEmpty(data.getLpnTypeCode())) {
				LpnType lpnType = lpnTypeBiz.findLpnTypeByCode(data.getLpnTypeCode());
				if (Func.isEmpty(lpnType)) {
					throw new ServiceException("导入失败，不存在容器类型编码：" + data.getLpnTypeCode());
				} else {
					location.setLpnTypeId(lpnType.getId());
				}
			}
			// 判断启用状态
			if (!data.getStatus().equals(StatusEnum.ON.getIndex())
				&& !data.getStatus().equals(StatusEnum.OFF.getIndex())) {
				throw new ServiceException("导入失败，启用状态只能为1(启用)或者-1(禁用)");
			} else {
				location.setStatus(data.getStatus());
			}

			location.setLocType(data.getLocType());
			location.setLocCategory(data.getLocCategory());
			location.setLocHandling(data.getLocHandling());
			location.setCheckDigit(data.getCheckDigit());
			location.setAbc(data.getAbc());
			location.setLocFlag(data.getLocFlag());
			location.setLocLength(data.getLocLength());
			location.setLocWide(data.getLocWide());
			location.setLocHigh(data.getLocHigh());
			location.setLocLevel(data.getLocLevel());
			location.setLocColumn(data.getLocColumn());
			location.setLocBank(data.getLocBank());
			location.setPutOrder(data.getPutOrder());
			location.setCapacity(data.getCapacity());
			location.setLoadWeight(data.getLoadWeight());
			location.setItemNum(data.getItemNum());
			location.setTrayNum(data.getTrayNum());
			location.setLocSkuMix(data.getLocSkuMix());
			location.setLocLotNoMix(data.getLocLotNoMix());

			locationList.add(location);
		}
		return locationList;
	}

	public Location createLocation(LocationAddOrEditRequest locationAddOrEditRequest) {
		Location location = new Location();
//		if (Func.isEmpty(locationAddRequest.getStatus())) {
//			throw new ServiceException("新增失败，启用状态不能为空");
//		}
		// 判断启用状态
//		if (!locationAddRequest.getStatus().equals(StatusEnum.ON.getIndex())
//			&& !locationAddRequest.getStatus().equals(StatusEnum.OFF.getIndex())) {
//			throw new ServiceException("新增失败，启用状态只能为1(启用)或者-1(禁用)");
//		} else {
//			location.setStatus(locationAddRequest.getStatus());
//		}
		Func.copy(locationAddOrEditRequest,location);
		// 根据id是否为空判断是(新增/编辑)操作
		if (Func.isNotEmpty(locationAddOrEditRequest.getLocId())){
			location.setLocId(locationAddOrEditRequest.getLocId());
		} else {
			// 判断库位编码是否已存在
			boolean isExist = locationDao.isExistLocationCode(locationAddOrEditRequest.getLocCode());
			if (isExist) {
				throw new ServiceException("编辑失败，库位编码[" + locationAddOrEditRequest.getLocCode() + "]已存在");
			} else {
				location.setLocCode(locationAddOrEditRequest.getLocCode());
			}
		}

//		location.setWhId(locationAddOrEditRequest.getWhId());
//		location.setZoneId(locationAddOrEditRequest.getZoneId());
//		location.setLocType(locationAddOrEditRequest.getLocType());
//		location.setLocCategory(locationAddOrEditRequest.getLocCategory());
//		location.setLocHandling(locationAddOrEditRequest.getLocHandling());
//		location.setCheckDigit(locationAddOrEditRequest.getCheckDigit());
//		location.setLogicAllocation(locationAddOrEditRequest.getLogicAllocation());
//		location.setAbc(locationAddOrEditRequest.getAbc());
//		location.setLocFlag(locationAddOrEditRequest.getLocFlag());
//		location.setLocLength(locationAddOrEditRequest.getLocLength());
//		location.setLocWide(locationAddOrEditRequest.getLocWide());
//		location.setLocHigh(locationAddOrEditRequest.getLocHigh());
//		location.setLocLevel(locationAddOrEditRequest.getLocLevel());
//		location.setLocColumn(locationAddOrEditRequest.getLocColumn());
//		location.setLocBank(locationAddOrEditRequest.getLocBank());
//		location.setPutOrder(locationAddOrEditRequest.getPutOrder());
//		location.setCapacity(locationAddOrEditRequest.getCapacity());
//		location.setLoadWeight(locationAddOrEditRequest.getLoadWeight());
//		location.setItemNum(locationAddOrEditRequest.getItemNum());
//		location.setTrayNum(locationAddOrEditRequest.getTrayNum());
//		location.setLocSkuMix(locationAddOrEditRequest.getLocSkuMix());
//		location.setLocLotNoMix(locationAddOrEditRequest.getLocLotNoMix());

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
		Func.copy(location,locationEditResponse);
		return locationEditResponse;
	}
}
