package org.nodes.wms.core.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.core.enums.StatusEnum;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.core.tool.utils.ValidationUtil;
import org.nodes.wms.biz.basics.dictionary.DictionaryBiz;
import org.nodes.wms.core.warehouse.cache.ZoneCache;
import org.nodes.wms.core.warehouse.dto.ZoneDTO;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.enums.LocTypeEnum;
import org.nodes.wms.core.warehouse.enums.ZoneTypeEnum;
import org.nodes.wms.core.warehouse.excel.ZoneExcel;
import org.nodes.wms.core.warehouse.mapper.ZoneMapper;
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.core.warehouse.vo.ZoneVO;
import org.nodes.wms.core.warehouse.wrapper.ZoneWrapper;
import org.nodes.wms.dao.application.dto.output.DictionaryResponse;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.constant.BladeConstant;
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
 * ???????????? ???????????????
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
			Zone zone = super.getById(id);
			String zoneCode = zone.getZoneCode();
			if (ZoneTypeEnum.VIRTUAL.getIndex().equals((zone.getZoneType()))
				&& StringUtil.contains(zoneCode, '-')
				&& ZoneTypeEnum.VIRTUAL.toString().equals(StringUtil.subAfter(zoneCode, "-", true))) {
				throw new ServiceException(String.format("?????????????????????[?????????%s, ?????????%s] ????????????????????????????????????", zoneCode, zone.getZoneName()));
			}
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
				if (Func.isEmpty(zone)) {
					throw new ServiceException("???????????????????????????????????????");
				} else {
					throw new ServiceException(String.format(
						"??????[??????:%s, ??????:%s] ????????????????????????????????????", zone.getZoneCode(), zone.getZoneName()));
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
			throw new ServiceException("??????????????????");
		}
		// ??????????????????????????????????????????
		if (Func.isEmpty(zoneDTO.getWhId())) {
			throw new ServiceException("??????????????????!");
		}
		ILocationService locationService = SpringUtil.getBean(ILocationService.class);
		List<Location> locationList = locationService.list(Condition.getQueryWrapper(new Location())
			.lambda()
			.eq(Location::getZoneId, zoneDTO.getZoneId())
		).stream().collect(Collectors.toList());
		if (locationList.size() > 0
			&& Func.isNotEmpty(zone) && !zoneDTO.getWhId().equals(zone.getWhId())) {
			throw new ServiceException(String.format("??????[%s] ????????????????????????????????????????????????! ", zone.getZoneName()));
		}
		boolean result = super.updateById(zoneDTO);
		Zone zone1 = BeanUtil.copy(zoneDTO, Zone.class);
		//ZoneCache.saveOrUpdate(zone1);
		return result;
	}

	public boolean save(ZoneDTO zoneDTO) {

		//???????????????????????????
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

		//??????????????????????????????
		if (Func.isNotEmpty(findObj)) {
			throw new ServiceException("??????????????????");
		}
		boolean result = super.save(zoneDTO);
		Zone zone = BeanUtil.copy(zoneDTO, Zone.class);
		//ZoneCache.saveOrUpdate(zone);
		return result;
	}

	/**
	 * ???????????????????????????
	 *
	 * @param zone ????????????
	 * @return ???????????????
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
		//????????????????????????
		List<ZoneVO> zoneList = ZoneWrapper.build().listVO(this.list(Condition.getQueryWrapper(params, Zone.class)));
		//??????????????????????????????????????????
		List<Long> whIdList = NodesUtil.toList(zoneList, Zone::getWhId);
		/*List<Warehouse> warehouseAll = WarehouseCache.list().stream().filter(warehouse -> {
			return whIdList.contains(warehouse.getWhId());
		}).collect(Collectors.toList());*/
		IWarehouseService warehouseService = SpringUtil.getBean(IWarehouseService.class);
		List<Warehouse> warehouseAll = warehouseService.listByIds(whIdList);

		List<ZoneExcel> zoneExportList = new ArrayList<>();
		for (Zone zone : zoneList) {
			// ?????????????????????????????????
			List<Warehouse> warehouseList = warehouseAll.stream().filter(warehouse -> {
				return warehouse.getWhId().equals(zone.getWhId());
			}).collect(Collectors.toList());

			ZoneExcel zoneExcel = BeanUtil.copy(zone, ZoneExcel.class);

			String zoneType = DictCache.getValue(DictCodeConstant.ZONE_TYPE, zone.getZoneType());
			if (Func.isNotEmpty(zoneType)) {
				zoneExcel.setZoneType(zoneType);
			}
			String status = zone.getStatus().equals(BladeConstant.DB_STATUS_NORMAL) ? StringPool.CHS_YES : StringPool.CHS_NO;
			zoneExcel.setStatus(status);
			//??????????????????
			if (Func.isNotEmpty(warehouseList)) {
				zoneExcel.setWhCode(warehouseList.get(0).getWhCode());
				zoneExcel.setWhName(warehouseList.get(0).getWhName());
			}
			zoneExportList.add(zoneExcel);
		}


		ExcelUtil.export(response, "??????", "???????????????", zoneExportList, ZoneExcel.class);
	}

	@Override
	public List<DataVerify> validExcel(List<ZoneExcel> dataList) {
		List<DataVerify> dataVerifyList = new ArrayList<>();

		Map<String, ZoneDTO> cache = new HashMap<>();
		int index = 1;
		for (ZoneExcel zoneExcel : dataList) {
			DataVerify dataVerify = new DataVerify();
			dataVerify.setIndex(index);
			// ?????????DTO
			ZoneDTO zoneDTO = ZoneWrapper.build().entityDTO(zoneExcel);
			if (Func.isNotEmpty(zoneExcel.getStatus())
				&& !zoneExcel.getStatus().equals(String.valueOf(StatusEnum.ON.getIndex()))
				&& !zoneExcel.getStatus().equals(String.valueOf(StatusEnum.OFF.getIndex()))) {
				throw new ServiceException("????????????????????????????????????1(??????)??????-1(??????)");
			}
			Integer status = Func.isEmpty(zoneExcel.getStatus()) ? null : Integer.parseInt(zoneExcel.getStatus());
			zoneDTO.setStatus(status);
//			ZoneTypeEnum[] zoneTypeEnums = ZoneTypeEnum.values();
//			List<String> typeIndexList = Arrays.stream(zoneTypeEnums).map(item -> item.getIndex().toString()).collect(Collectors.toList());
//			if (typeIndexList.contains(zoneExcel.getZoneType())) {
//				zoneDTO.setZoneType(Integer.parseInt(zoneExcel.getZoneType()));
//			} else {
//				throw new ServiceException("??????????????????????????????????????????");
//			}
			if (Func.isEmpty(zoneExcel.getZoneType())) {
				throw new ServiceException("?????????????????????????????????????????????");
			}
			DictionaryBiz dictionaryBiz = SpringUtil.getBean(DictionaryBiz.class);
			List<DictionaryResponse> zoneTypeList = dictionaryBiz.getDictionaryResponseList("zone_type");
			List<String> keyList = zoneTypeList.stream().map(item -> item.getDictKey().toString()).collect(Collectors.toList());
			if (keyList.contains(zoneExcel.getZoneType())) {
				zoneDTO.setZoneType(Integer.parseInt(zoneExcel.getZoneType()));
			} else {
				throw new ServiceException("??????????????????????????????????????????");
			}
			//??????????????????????????????
			//Warehouse warehouse = WarehouseCache.getByCode(zoneExcel.getWhCode());
			IWarehouseService warehouseService = SpringUtil.getBean(IWarehouseService.class);
			Warehouse warehouse = warehouseService.list(Condition.getQueryWrapper(new Warehouse())
				.lambda()
				.eq(Warehouse::getWhCode, zoneExcel.getWhCode())
			).stream().findFirst().orElse(null);
			if (Func.isEmpty(warehouse)) {
				throw new ServiceException("????????????[" + zoneExcel.getWhCode() + "]?????????");
			}
			zoneDTO.setWhId(warehouse.getWhId());
			// ??????????????????
			ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(zoneDTO);
			// ???????????????????????????
			//Zone findZone = ZoneCache.getByCode(zoneDTO.getZoneCode());
			Zone findZone = super.list(Condition.getQueryWrapper(new Zone())
				.lambda()
				.eq(Zone::getZoneCode, zoneDTO.getZoneCode())
			).stream().findFirst().orElse(null);

			if (Func.isNotEmpty(findZone)) {
				validResult.addError("zoneCode", "????????????[" + zoneDTO.getZoneCode() + "]?????????");
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
			// ???????????????redis?????????
			for (String code : cache.keySet()) {
				ZoneCache.put(code, cache.get(code));
			}
		}

		return dataVerifyList;
	}

	@Override
	public boolean importData(List<DataVerify> dataVerifyList) {
		if (Func.isEmpty(dataVerifyList)) {
			throw new ServiceException("???????????????????????????");
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
		//??????
		List<Zone> list = super.list();
		long count = list.stream()
			.filter(u -> {
				return u.getWhId().equals(zoneDTO.getWhId()) && u.getZoneCode().equals(zoneDTO.getZoneCode())
					&& !Func.equals(u.getZoneId(), zoneDTO.getZoneId());
			}).count();
		if (count > 0) {
			throw new ServiceException(String.format("????????????[%s] ????????????????????????! ", zoneDTO.getZoneCode()));
		}
	}
}
