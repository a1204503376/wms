
package org.nodes.wms.core.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.ValidationUtil;
import org.nodes.wms.core.warehouse.cache.ZoneCache;
import org.nodes.wms.core.warehouse.dto.ZoneDTO;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.entity.Zone;
import org.nodes.wms.core.warehouse.enums.LocTypeEnum;
import org.nodes.wms.core.warehouse.excel.ZoneExcel;
import org.nodes.wms.core.warehouse.mapper.ZoneMapper;
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.core.warehouse.vo.ZoneVO;
import org.nodes.wms.core.warehouse.wrapper.ZoneWrapper;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 库区管理 服务实现类
 *
 * @author zhongls
 * @since 2019-12-06
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class ZoneServiceImpl<M extends ZoneMapper, T extends Zone>
	extends BaseServiceImpl<ZoneMapper, Zone>
	implements IZoneService {


	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {

		ILocationService locationService = SpringUtil.getBean(ILocationService.class);
		for (Serializable id : idList) {

			/*List<Location> locList = LocationCache.listByZoneId((Long) id)
				.stream().filter(u -> {
					return !LocTypeEnum.Virtual.getIndex().equals(u.getLocType());
				}).collect(Collectors.toList());*/

			List<Location> locList = locationService.list(Condition.getQueryWrapper(new Location())
				.lambda()
				.eq(Location::getZoneId, id)
				.ne(Location::getLocType, LocTypeEnum.Virtual.getIndex())
			).stream().collect(Collectors.toList());

			if (Func.isEmpty(locList)) {
				super.removeById(id);
			} else {
				Zone zone = super.getById(id);
				if (Func.isEmpty(zone)) {
					throw new ServiceException("该库区已被占用，无法删除！");
				} else {
					throw new ServiceException(String.format(
						"库区[编码:%s, 名称:%s] 已被库位占用，无法删除！", zone.getZoneCode(), zone.getZoneName()));
				}
			}
		}

		return true;
	}

	@Override
	public boolean saveOrUpdate(ZoneDTO zoneDTO) {
		verifyZone(zoneDTO);
		return super.saveOrUpdate(zoneDTO);
	}

	@Override
	public boolean updateById(ZoneDTO zoneDTO) {
		/*Zone zone = ZoneCache.list().stream().filter(u -> {
			return u.getZoneCode().equals(zoneDTO.getZoneCode())
				&& u.getWhId().equals(zoneDTO.getWhId())
				&& !u.getZoneId().equals(zoneDTO.getZoneId());
		}).findFirst().orElse(null);*/
		Zone zone = super.list(Condition.getQueryWrapper(new Zone())
			.lambda()
			.eq(Zone::getZoneCode, zoneDTO.getZoneCode())
			.eq(Zone::getWhId, zoneDTO.getWhId())
			.ne(Zone::getZoneId, zoneDTO.getZoneId())
		).stream().findFirst().orElse(null);

		if (Func.isNotEmpty(zone)) {
			throw new ServiceException("库区编码重复");
		}
		// 当库区下已有库位，不允许修改
		if (Func.isEmpty(zoneDTO.getWhId())) {
			throw new ServiceException("库房不能为空!");
		}
		ILocationService locationService = SpringUtil.getBean(ILocationService.class);
		List<Location> locationList = locationService.list(Condition.getQueryWrapper(new Location())
			.lambda()
			.eq(Location::getZoneId, zoneDTO.getZoneId())
		).stream().collect(Collectors.toList());
		if (locationList.size() > 0
			&& Func.isNotEmpty(zone) && !zoneDTO.getWhId().equals(zone.getWhId())) {
			throw new ServiceException(String.format("库位[%s] 下已存在库位，不允许修改所属库房! ", zone.getZoneName()));
		}
		boolean result = super.updateById(zoneDTO);
		Zone zone1 = BeanUtil.copy(zoneDTO, Zone.class);
		//ZoneCache.saveOrUpdate(zone1);
		return result;
	}

	public boolean save(ZoneDTO zoneDTO) {

		//如果主键存在则更新
		if (Func.isNotEmpty(zoneDTO.getZoneId())) {
			super.updateById(zoneDTO);
			return true;
		}
		/*Zone findObj = ZoneCache.list().stream().filter(u -> {
			return u.getWhId().equals(zoneDTO.getWhId()) && u.getZoneCode().equals(zoneDTO.getZoneCode());
		}).findFirst().orElse(null);*/

		Zone findObj = super.list(Condition.getQueryWrapper(new Zone())
			.lambda()
			.eq(Zone::getWhId, zoneDTO.getWhId())
			.eq(Zone::getZoneCode, zoneDTO.getZoneCode())
		).stream().findFirst().orElse(null);

		//查询库区编码是否重复
		if (Func.isNotEmpty(findObj)) {
			throw new ServiceException("库区编码重复");
		}
		boolean result = super.save(zoneDTO);
		Zone zone = BeanUtil.copy(zoneDTO, Zone.class);
		//ZoneCache.saveOrUpdate(zone);
		return result;
	}

	/**
	 * 获取模糊查询构造器
	 *
	 * @param zone 查询条件
	 * @return 查询构造器
	 */
	private QueryWrapper<Zone> getSelectQueryWrapper(Zone zone) {
		QueryWrapper<Zone> queryWrapper = new QueryWrapper<>();
		if (Func.isNotEmpty(zone.getZoneCode())) {
			queryWrapper.lambda().like(Zone::getZoneCode, zone.getZoneCode());
		}
		if (Func.isNotEmpty(zone.getZoneName())) {
			queryWrapper.lambda().like(Zone::getZoneName, zone.getZoneName());
		}
		if (Func.isNotEmpty(zone.getWhId())) {
			queryWrapper.lambda().eq(Zone::getWhId, zone.getWhId());
		}
		if (Func.isNotEmpty(zone.getZoneType())) {
			queryWrapper.lambda().eq(Zone::getZoneType, zone.getZoneType());
		}
		return queryWrapper;
	}

	@Override
	public List<ZoneVO> selectList(Zone zone) {
		return ZoneWrapper.build().listVO(super.list(this.getSelectQueryWrapper(zone)));
	}

	@Override
	public IPage<ZoneVO> selectPage(IPage<Zone> page, Zone zone) {
		IPage<Zone> pages = super.page(page, this.getSelectQueryWrapper(zone));
		return ZoneWrapper.build().pageVO(pages);
	}

	@Override
	public void exportExcel(HashMap<String, Object> params, HttpServletResponse response) {
		//查询计量单位信息
		List<ZoneVO> zoneList = ZoneWrapper.build().listVO(this.list(Condition.getQueryWrapper(params, Zone.class)));
		//查询所有与库区相关的库房信息
		List<Long> whIdList = NodesUtil.toList(zoneList, Zone::getWhId);
		/*List<Warehouse> warehouseAll = WarehouseCache.list().stream().filter(warehouse -> {
			return whIdList.contains(warehouse.getWhId());
		}).collect(Collectors.toList());*/
		IWarehouseService warehouseService = SpringUtil.getBean(IWarehouseService.class);
		List<Warehouse> warehouseAll = warehouseService.listByIds(whIdList);

		List<ZoneExcel> zoneExportList = new ArrayList<>();
		for (Zone zone : zoneList) {
			// 查询当前库区的所属库房
			List<Warehouse> warehouseList = warehouseAll.stream().filter(warehouse -> {
				return warehouse.getWhId().equals(zone.getWhId());
			}).collect(Collectors.toList());

			ZoneExcel zoneExcel = BeanUtil.copy(zone, ZoneExcel.class);

			String zoneType = DictCache.getValue(DictConstant.ZONE_TYPE, zone.getZoneType());
			if (Func.isNotEmpty(zoneType)) {
				zoneExcel.setZoneType(zoneType);
			}
			//封装所属库房
			if (Func.isNotEmpty(warehouseList)) {
				zoneExcel.setWhCode(warehouseList.get(0).getWhCode());
				zoneExcel.setWhName(warehouseList.get(0).getWhName());
			}
			zoneExportList.add(zoneExcel);
		}


		ExcelUtil.export(response, "库区", "库区数据表", zoneExportList, ZoneExcel.class);
	}

	@Override
	public List<DataVerify> validExcel(List<ZoneExcel> dataList) {
		List<DataVerify> dataVerifyList = new ArrayList<>();

		Map<String, ZoneDTO> cache = new HashMap<>();
		int index = 1;
		for (ZoneExcel zoneExcel : dataList) {
			DataVerify dataVerify = new DataVerify();
			dataVerify.setIndex(index);
			// 封装成DTO
			ZoneDTO zoneDTO = ZoneWrapper.build().entityDTO(zoneExcel);
			//查询所属库房是否存在
			//Warehouse warehouse = WarehouseCache.getByCode(zoneExcel.getWhCode());
			IWarehouseService warehouseService = SpringUtil.getBean(IWarehouseService.class);
			Warehouse warehouse = warehouseService.list(Condition.getQueryWrapper(new Warehouse())
				.lambda()
				.eq(Warehouse::getWhCode, zoneExcel.getWhCode())
			).stream().findFirst().orElse(null);
			if (Func.isEmpty(warehouse)) {
				throw new ServiceException("库房编码[" + zoneExcel.getWhCode() + "]不存在");
			}
			zoneDTO.setWhId(warehouse.getWhId());
			// 开始效验数据
			ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(zoneDTO);
			// 库区编码唯一性验证
			//Zone findZone = ZoneCache.getByCode(zoneDTO.getZoneCode());
			Zone findZone = super.list(Condition.getQueryWrapper(new Zone())
				.lambda()
				.eq(Zone::getZoneCode, zoneDTO.getZoneCode())
			).stream().findFirst().orElse(null);

			if (Func.isNotEmpty(findZone)) {
				validResult.addError("zoneCode", "库区编码[" + zoneDTO.getZoneCode() + "]已存在");
			}
			if (validResult.hasErrors()) {
				dataVerify.setMessage(StringUtil.join(validResult.getAllErrors()));
			} else {
				cache.put(zoneDTO.getZoneCode(), zoneDTO);
				dataVerify.setCacheKey(zoneDTO.getZoneCode());
			}
			dataVerifyList.add(dataVerify);
			index++;
		}
		if (Func.isNotEmpty(cache)) {
			// 存储数据到redis缓存中
			for (String code : cache.keySet()) {
				ZoneCache.put(code, cache.get(code));
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
			ZoneDTO zoneDTO = ZoneCache.get(dataVerify.getCacheKey());
			if (ObjectUtil.isEmpty(zoneDTO)) {
				continue;
			}
			if (this.saveOrUpdate(zoneDTO)) {
				ZoneCache.remove(zoneDTO.getZoneCode());
				//ZoneCache.saveOrUpdate(zoneDTO);
			}
		}
		return true;
	}

	public void verifyZone(ZoneDTO zoneDTO) {
		//验证
		List<Zone> list = super.list();
		long count = list.stream()
			.filter(u -> {
				return u.getWhId().equals(zoneDTO.getWhId()) && u.getZoneCode().equals(zoneDTO.getZoneCode())
					&& !Func.equals(u.getZoneId(), zoneDTO.getZoneId());
			}).count();
		if (count > 0) {
			throw new ServiceException(String.format("库区编码[%s] 在当前库房已存在! ", zoneDTO.getZoneCode()));
		}
	}
}
