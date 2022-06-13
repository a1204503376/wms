
package org.nodes.wms.core.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.PrintServiceUtil;
import org.nodes.core.tool.utils.ValidationUtil;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.dto.LocationDTO;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.excel.LocationExcel;
import org.nodes.wms.core.warehouse.mapper.LocationMapper;
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.core.warehouse.vo.LocationVO;
import org.nodes.wms.core.warehouse.wrapper.LocationWrapper;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.jackson.JsonUtil;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.nodes.wms.core.warehouse.cache.LocationCache.LOCATION_CACHE;

/**
 * 服务实现类
 *
 * @author NodeX
 * @since 2019-12-11
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class LocationServiceImpl<M extends LocationMapper, T extends Location>
	extends BaseServiceImpl<LocationMapper, Location>
	implements ILocationService {

	@Autowired
	IStockService stockService;

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {

		if (stockService.count(Condition.getQueryWrapper(new Stock()).lambda()
			.in(Stock::getLocId, idList)) > 0) {
			throw new ServiceException("该库位已被占用，无法删除！");
		}
		boolean result = super.removeByIds(idList);
		if (result) {
			//LocationCache.removeByIds(idList);
		}
		return result;
	}

	@Override
	public boolean updateById(Location location) {
		Location findObj = LocationCache.getByCode(location.getLocCode());

		if (Func.isNotEmpty(findObj) && !findObj.getLocId().equals(location.getLocId())) {
			throw new ServiceException("库位编码[" + location.getLocCode() + "]重复");
		}
		// 库房或者库位不一样的情况下，需要验证是否有库存
		if (Func.isEmpty(findObj) || !location.getWhId().equals(findObj.getWhId()) ||
			!location.getZoneId().equals(findObj.getZoneId())) {
			// 更新库位信息，验证库存
//			int stockCount = stockService.count(Condition.getQueryWrapper(new Stock()).lambda()
//				.eq(Stock::getLocId, location.getLocId()));
//			if (stockCount > 0) {
//				throw new ServiceException(String.format(
//					"库位[%s] 已存在库存信息，不允许修改所属库房或库区！", location.getLocCode()));
//			}
		}
		boolean result = super.updateById(location);
		if (result) {
			//LocationCache.saveOrUpdate(location);
		}
		return result;
	}

	@Override
	public boolean save(Location location) {
		//查询库位编码是否重复
		long cnt = super.count(Condition.getQueryWrapper(new Location())
			.lambda()
			.eq(Location::getLocCode, location.getLocCode())
		);
		if (cnt > 0L) {
			throw new ServiceException("库位编码重复");
		}
		boolean result = super.save(location);
		if (result) {
			//LocationCache.saveOrUpdate(location);
		}
		return result;
	}

	@Override
	public boolean print(List<Long> idList) {
		for (Long id : idList) {
			Location location = LocationCache.getById(id);
			if (Func.isEmpty(location)) {
				throw new ServiceException("库位不存在或已删除！");
			}

			PrintServiceUtil.print("库位标签", JsonUtil.toJson(LocationWrapper.build().entityVO(location)));
		}
		return true;
	}

	private QueryWrapper<Location> getSelectQueryWrapper(Location location) {
		QueryWrapper<Location> queryWrapper = new QueryWrapper<>();
		if (Func.isNotEmpty(location.getLocCode())) {
			queryWrapper.lambda().like(Location::getLocCode, location.getLocCode());
		}
		if (Func.isNotEmpty(location.getWhId())) {
			queryWrapper.lambda().eq(Location::getWhId, location.getWhId());
		}
		if (Func.isNotEmpty(location.getZoneId())) {
			queryWrapper.lambda().eq(Location::getZoneId, location.getZoneId());
		}
		if (Func.isNotEmpty(location.getLocType())) {
			queryWrapper.lambda().eq(Location::getLocType, location.getLocType());
		}
		if (Func.isNotEmpty(location.getLocCategory())) {
			queryWrapper.lambda().eq(Location::getLocCategory, location.getLocCategory());
		}
		return queryWrapper;
	}

	@Override
	public List<LocationVO> selectList(Location location) {
		return LocationWrapper.build().listVO(super.list(this.getSelectQueryWrapper(location)));
	}

	@Override
	public boolean updateCountBillNo(Long locId, String countBillNo) {
		return updateCountBillNo(new ArrayList() {{
			add(locId);
		}}, countBillNo);
	}

	@Override
	public synchronized boolean updateCountBillNo(Collection<Long> locIdList, String countBillNo) {
		UpdateWrapper<Location> updateWrapper = new UpdateWrapper<>();
		updateWrapper.lambda()
			.set(Location::getCountBillNo, countBillNo)
			.func(sql -> {
				// 修改盘点发布时间
				if (Func.isNotEmpty(countBillNo)) {
					sql.set(Location::getLastCycleDate, LocalDateTime.now());
				}
			})
			.in(Location::getLocId, locIdList);
		CacheUtil.clear(LOCATION_CACHE);
		return super.update(updateWrapper);
	}

	/**
	 * 锁定库位
	 *
	 * @param locIds
	 * @param lockFlag 锁定状态
	 * @return
	 */
	@Override
	public boolean lockById(String locIds, String lockFlag) {
		if (Func.isEmpty(locIds)) {
			throw new ServiceException("库位id不能为空");
		}
		List<Long> idList = Func.toLongList(locIds);
		UpdateWrapper<Location> updateWrapper = new UpdateWrapper<>();
		updateWrapper.lambda()
			.set(Location::getLockFlag, lockFlag)
			.in(Location::getLocId, idList);
		CacheUtil.clear(LOCATION_CACHE);
		boolean result = super.update(updateWrapper);
		if (result) {
			for (Long id : idList) {
				Location location = super.getById(id);
				location.setLockFlag(lockFlag);
				//LocationCache.saveOrUpdate(location);
			}
		}
		return result;
	}

	@Override
	public IPage<LocationVO> selectPage(IPage<Location> page, Location location) {
		IPage<Location> pages = super.page(page, this.getSelectQueryWrapper(location));
		return LocationWrapper.build().pageVO(pages);
	}

	@Override
	public void exportExcel(HashMap<String, Object> params, HttpServletResponse response) {
		//查询库位信息
		List<LocationVO> locationList = LocationWrapper.build().listVO(this.list(Condition.getQueryWrapper(params, Location.class)));
		//查询所有与库位相关的库房信息
		List<Long> whIdList = NodesUtil.toList(locationList, Location::getWhId);
		/*List<Warehouse> warehouseAll = WarehouseCache.list().stream().filter(warehouse -> {
			return whIdList.contains(warehouse.getWhId());
		}).collect(Collectors.toList());*/
		IWarehouseService warehouseService = SpringUtil.getBean(IWarehouseService.class);
		List<Warehouse> warehouseAll = warehouseService.listByIds(whIdList);

		//查询所有与库位相关的库区信息
		List<Long> zoneIdList = NodesUtil.toList(locationList, Location::getZoneId);
	/*	List<Zone> zoneAll = ZoneCache.list().stream().filter(zone -> {
			return zoneIdList.contains(zone.getZoneId());
		}).collect(Collectors.toList());*/
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		List<Zone> zoneAll = zoneService.listByIds(zoneIdList);

		List<LocationExcel> locationExportList = new ArrayList<>();
		for (Location location : locationList) {
			// 查询当前库位的所属库房
			List<Warehouse> warehouseList = warehouseAll.stream().filter(warehouse -> {
				return warehouse.getWhId().equals(location.getWhId());
			}).collect(Collectors.toList());
			// 查询当前库位的所属库区
			List<Zone> zoneList = zoneAll.stream().filter(zone -> {
				return zone.getZoneId().equals(location.getZoneId());
			}).collect(Collectors.toList());

			LocationExcel locationExcel = BeanUtil.copy(location, LocationExcel.class);

			//封装所属库房
			if (Func.isNotEmpty(warehouseList)) {
				locationExcel.setWhCode(warehouseList.get(0).getWhCode());
				locationExcel.setWhName(warehouseList.get(0).getWhName());
			}
			//封装所属库区
			if (Func.isNotEmpty(zoneList)) {
				locationExcel.setZoneCode(zoneList.get(0).getZoneCode());
				locationExcel.setZoneName(zoneList.get(0).getZoneName());
			}
			//应用类型
			locationExcel.setLocType(Func.toInt(DictCache.getValue(DictConstant.LOC_TYPE, location.getLocType())));
			//库位种类
			locationExcel.setLocCategory(Func.toInt(DictCache.getValue(DictConstant.LOC_CATEGORY, location.getLocCategory())));
			locationExportList.add(locationExcel);

		}
		ExcelUtil.export(response, "库位", "库位数据表", locationExportList, LocationExcel.class);
	}

	@Override
	public List<DataVerify> validExcel(List<LocationExcel> dataList) {
		List<DataVerify> dataVerifyList = new ArrayList<>();

		Map<String, LocationDTO> cache = new HashMap<>();
		int index = 1;
		for (LocationExcel locationExcel : dataList) {
			DataVerify dataVerify = new DataVerify();
			dataVerify.setIndex(index);
			// 封装成DTO
			LocationDTO locationDTO = LocationWrapper.build().entityDTO(locationExcel);
			//查询库房是否存在
			//Warehouse warehouse = WarehouseCache.getByCode(locationExcel.getWhCode());
			IWarehouseService warehouseService = SpringUtil.getBean(IWarehouseService.class);
			Warehouse warehouse = warehouseService.list(Condition.getQueryWrapper(new Warehouse())
				.lambda()
				.eq(Warehouse::getWhCode, locationExcel.getWhCode())
			).stream().findFirst().orElse(null);
			if (Func.isEmpty(warehouse)) {
				throw new ServiceException("库房编码[" + locationExcel.getWhCode() + "]不存在");
			}
			locationDTO.setWhId(warehouse.getWhId());
			//查询库区是否存在
			//Zone zone = ZoneCache.getByCode(locationExcel.getZoneCode());
			IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
			Zone zone = zoneService.list(Condition.getQueryWrapper(new Zone())
				.lambda()
				.eq(Zone::getZoneCode, locationExcel.getZoneCode())
			).stream().findFirst().orElse(null);
			if (Func.isEmpty(zone)) {
				throw new ServiceException("库位编码[" + locationExcel.getZoneCode() + "]不存在");
			}
			locationDTO.setZoneId(zone.getZoneId());
			// 开始效验数据
			ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(locationDTO);
			long cnt = DictCache.list(DictConstant.LOC_TYPE).stream().filter(u -> {
				return Func.equals(u.getDictKey(), Func.toInt(locationExcel.getLocType()));
			}).count();
			if (cnt == 0) {
				validResult.addError("locType", "库位类型[" + locationExcel.getLocType() + "]不存在");
			}
			locationDTO.setLocType(locationExcel.getLocType());
			cnt = DictCache.list(DictConstant.LOC_CATEGORY).stream().filter(u -> {
				return Func.equals(u.getDictKey(), locationExcel.getLocCategory());
			}).count();
			if (cnt == 0) {
				validResult.addError("locCategory", "库位种类[" + locationExcel.getLocCategory() + "]不存在");
			}
			locationDTO.setLocType(locationExcel.getLocCategory());

			// 库位编码唯一性验证
			//Location findLocation = LocationCache.getByCode(locationDTO.getLocCode());
			Location findLocation = super.list(Condition.getQueryWrapper(new Location())
				.lambda()
				.eq(Location::getLocCode, locationDTO.getLocCode())
			).stream().findFirst().orElse(null);
			if (Func.isNotEmpty(findLocation)) {
				validResult.addError("locCode", "库位编码[" + locationDTO.getLocCode() + "]已存在");
			}
			dataVerifyList.add(dataVerify);
			index++;
		}
		if (Func.isNotEmpty(cache)) {
			// 存储数据到redis缓存中
			for (String code : cache.keySet()) {
				LocationCache.put(code, cache.get(code));
			}
		}

		return dataVerifyList;
	}

	@Override
	public boolean importData(List<DataVerify> dataVerifyList) {

		if (Func.isEmpty(dataVerifyList)) {
			throw new ServiceException("没有数据可以保存！");
		}
		for (DataVerify dataVerify : dataVerifyList) {
			if (ObjectUtil.isEmpty(dataVerify)) {
				continue;
			}
			LocationDTO locationDTO = LocationCache.get(dataVerify.getCacheKey());
			if (ObjectUtil.isEmpty(locationDTO)) {
				continue;
			}
			if (this.saveOrUpdate(locationDTO)) {
				LocationCache.remove(locationDTO.getLocCode());
				//LocationCache.saveOrUpdate(locationDTO);
			}
		}
		return true;
	}
}
