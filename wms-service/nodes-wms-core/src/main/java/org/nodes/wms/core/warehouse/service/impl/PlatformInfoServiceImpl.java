
package org.nodes.wms.core.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.ValidationUtil;
import org.nodes.core.tool.validation.Group;
import org.nodes.wms.core.warehouse.cache.PlatformInfoCache;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.dto.PlatformInfoDTO;
import org.nodes.wms.core.warehouse.entity.PlatformInfo;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.excel.PlatformInfoExcel;
import org.nodes.wms.core.warehouse.mapper.PlatformInfoMapper;
import org.nodes.wms.core.warehouse.service.IPlatformInfoService;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.nodes.wms.core.warehouse.wrapper.PlatforminfoWrapper;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 月台服务实现类
 *
 * @author liangmei
 * @since 2019-12-06
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class PlatformInfoServiceImpl<M extends PlatformInfoMapper, T extends PlatformInfo>
	extends BaseServiceImpl<PlatformInfoMapper, PlatformInfo>
	implements IPlatformInfoService {


	@Override
	public boolean saveOrUpdate(PlatformInfo platformInfo) {
		QueryWrapper<PlatformInfo> platformInfoQueryWrapper = new QueryWrapper<>();
		platformInfoQueryWrapper.eq("platform_code", platformInfo.getPlatformCode());
		platformInfoQueryWrapper.eq("wh_id", platformInfo.getWhId());
		if (Func.isEmpty(platformInfo.getSpiId())) {
			if (baseMapper.selectList(platformInfoQueryWrapper).size() > 0) {
				throw new ServiceException("月台编码不可重复");
			}
			super.save(platformInfo);
		} else {
			platformInfoQueryWrapper.ne("spi_id", platformInfo.getSpiId());
			if (baseMapper.selectList(platformInfoQueryWrapper).size() > 0) {
				throw new ServiceException("月台编码不可重复");
			}
			super.updateById(platformInfo);
		}
		return true;
	}

	@Override
	public List<PlatformInfo> selectList(PlatformInfoDTO platformInfoDTO) {
		return super.list(this.getQueryWrapper(platformInfoDTO));
	}

	@Override
	public IPage<PlatformInfo> selectPage(IPage<PlatformInfo> page, PlatformInfoDTO platformInfoDTO) {
		return super.page(page, this.getQueryWrapper(platformInfoDTO));
	}

	@Override
	public void exportExcel(HashMap<String, Object> params, HttpServletResponse response) {
        // 查询出月台信息
		List<PlatformInfo> platformInfoList = this.list(Condition.getQueryWrapper(params, PlatformInfo.class));

		//查询所有与月台相关的库房信息
		List<Long> whIdList= NodesUtil.toList(platformInfoList,PlatformInfo::getWhId);
		/*List<Warehouse> warehouseAll = WarehouseCache.list().stream().filter(warehouse -> {
			return whIdList.contains(warehouse.getWhId());
		}).collect(Collectors.toList());*/
		IWarehouseService warehouseService = SpringUtil.getBean(IWarehouseService.class);
		List<Warehouse> warehouseAll = warehouseService.listByIds(whIdList);
		List<PlatformInfoExcel> platExportList = new ArrayList<>();
		// 循环查询出来的月台列表
		for (PlatformInfo platformInfo : platformInfoList) {
			// 查询当前月台的库房
			List<Warehouse> warehouseList = warehouseAll.stream().filter(warehouse -> {
				return warehouse.getWhId().equals(platformInfo.getWhId());
			}).collect(Collectors.toList());
			// 计算最大循环次数，最少循环一次
			Integer maxLength = 1;
			for (int i = 0; i < maxLength; i++) {
				PlatformInfoExcel platfromInfoExport = BeanUtil.copy(platformInfo, PlatformInfoExcel.class);
				Dict dict = DictCache.list(DictConstant.PLATFORM_TYPE).stream().filter(u->{
					return Func.equals(u.getDictKey(), platformInfo.getPlatformType());
				}).findFirst().orElse(null);
				platfromInfoExport.setPlatformTypeDesc(Func.isEmpty(dict)? StringPool.EMPTY : dict.getDictValue());
				//封装库房
				if (Func.isNotEmpty(warehouseList)){
					platfromInfoExport.setWhCode(warehouseList.get(0).getWhCode());
					platfromInfoExport.setWhName(warehouseList.get(0).getWhName());
				}

				//将月台装入新list
				platExportList.add(platfromInfoExport);
			}
		}
		ExcelUtil.export(response, "月台", "月台数据表", platExportList, PlatformInfoExcel.class);
	}

	@Override
	public List<DataVerify> validExcel(List<PlatformInfoExcel> dataList) {
		List<DataVerify> dataVerifyList = new ArrayList<>();

		Map<String, PlatformInfoDTO> cache = new HashMap<>();
		int index = 1;
		for (PlatformInfoExcel platformInfoExcel: dataList) {
			DataVerify dataVerify = new DataVerify();
			dataVerify.setIndex(index);
			// 封装成DTO
			PlatformInfoDTO platformInfoDTO = PlatforminfoWrapper.build().entityDTO(platformInfoExcel);

			// 开始效验数据
			ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(platformInfoDTO, Group.class);

			// 月台编码唯一性验证
			//PlatformInfo findPlatform = PlatformInfoCache.getByCode(platformInfoDTO.getPlatformCode());
			PlatformInfo findPlatform = super.list(Condition.getQueryWrapper(new PlatformInfo())
			.lambda()
			.eq(PlatformInfo::getPlatformCode,platformInfoDTO.getPlatformCode())
			).stream().findFirst().orElse(null);
			if (Func.isNotEmpty(findPlatform)) {
				validResult.addError("platformCode", "月台编码[" + platformInfoDTO.getPlatformCode() + "]已存在");
			}

			//查询库房是否存在
			//Warehouse warehouse=WarehouseCache.getByCode(platformInfoExcel.getWhCode());
			IWarehouseService warehouseService = SpringUtil.getBean(IWarehouseService.class);
			Warehouse warehouse = warehouseService.list(Condition.getQueryWrapper(new Warehouse())
			.lambda()
			.eq(Warehouse::getWhCode,platformInfoExcel.getWhCode())
			).stream().findFirst().orElse(null);
			if (Func.isEmpty(warehouse)){
				validResult.addError("platformCode", "库房编码不存在");
			}else{
				platformInfoDTO.setWhId(warehouse.getWhId());
			}
			Dict dict = DictCache.list(DictConstant.PLATFORM_TYPE).stream().filter(u->{
				return Func.equals(u.getDictValue(), platformInfoDTO.getPlatformTypeDesc());
			}).findFirst().orElse(null);
			platformInfoDTO.setPlatformType(dict.getDictKey());
			if (validResult.hasErrors()) {
				dataVerify.setMessage(StringUtil.join(validResult.getAllErrors()));
			} else {
				cache.put(platformInfoDTO.getPlatformCode(), platformInfoDTO);
				dataVerify.setCacheKey(platformInfoDTO.getPlatformCode());
			}
			dataVerifyList.add(dataVerify);
			index++;
		}
		if (Func.isNotEmpty(cache)) {
			// 存储数据到redis缓存中
			for (String code : cache.keySet()) {
				PlatformInfoCache.put(code, cache.get(code));
			}
		}
		return dataVerifyList;
	}

	@Override
	public boolean importData(List<DataVerify> dataVerifyList) {
		if(Func.isEmpty(dataVerifyList)) {
			throw new ServiceException("没有数据可以保存！");
		}
		for (DataVerify dataVerify : dataVerifyList) {
			if (ObjectUtil.isEmpty(dataVerify)) {
				continue;
			}
			PlatformInfoDTO platformInfoDTO = PlatformInfoCache.get(dataVerify.getCacheKey());
			if (ObjectUtil.isEmpty(platformInfoDTO)) {
				continue;
			}
			if (this.saveOrUpdate(platformInfoDTO)) {
				PlatformInfoCache.remove(platformInfoDTO.getPlatformCode());
				//PlatformInfoCache.saveOrUpdate(platformInfoDTO);
			}
		}
		return true;
	}
	private QueryWrapper<PlatformInfo> getQueryWrapper(PlatformInfo platformInfo) {
		QueryWrapper<PlatformInfo> queryWrapper = new QueryWrapper<>();

		if (Func.isNotEmpty(platformInfo.getPlatformCode())) {
			queryWrapper.lambda().like(PlatformInfo::getPlatformCode, platformInfo.getPlatformCode());
		}
		if (Func.isNotEmpty(platformInfo.getPlatformName())) {
			queryWrapper.lambda().like(PlatformInfo::getPlatformName, platformInfo.getPlatformName());
		}
		if (Func.isNotEmpty(platformInfo.getPlatformType())) {
			queryWrapper.lambda().like(PlatformInfo::getPlatformType, platformInfo.getPlatformType());
		}
		return queryWrapper;
	}
}
