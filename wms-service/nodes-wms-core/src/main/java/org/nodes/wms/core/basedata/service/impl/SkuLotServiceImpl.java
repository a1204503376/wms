package org.nodes.wms.core.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.ValidationUtil;
import org.nodes.core.tool.validation.Excel;
import org.nodes.wms.core.basedata.cache.OwnerCache;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuLotCache;
import org.nodes.wms.core.basedata.cache.SkuLotValCache;
import org.nodes.wms.core.basedata.dto.SkuLotDTO;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.entity.SkuLot;
import org.nodes.wms.core.basedata.entity.SkuLotVal;
import org.nodes.wms.core.basedata.excel.SkuLotExcel;
import org.nodes.wms.core.basedata.mapper.SkuLotMapper;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.basedata.service.ISkuLotService;
import org.nodes.wms.core.basedata.service.ISkuLotValService;
import org.nodes.wms.core.basedata.service.ISkuService;
import org.nodes.wms.core.basedata.vo.SkuLotConfigVO;
import org.nodes.wms.core.basedata.vo.SkuLotVO;
import org.nodes.wms.core.basedata.wrapper.SkuLotWrapper;
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
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 物品批属性 服务实现类
 *
 * @author NodeX
 * @since 2019-12-10
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SkuLotServiceImpl<M extends SkuLotMapper, T extends SkuLot>
	extends BaseServiceImpl<SkuLotMapper, SkuLot>
	implements ISkuLotService {

	/**
	 * 获取模糊查询构造器
	 *
	 * @param skuLot 查询条件
	 * @return 查询构造器
	 */
	private QueryWrapper<SkuLot> getSelectQueryWrapper(SkuLot skuLot) {
		QueryWrapper<SkuLot> queryWrapper = new QueryWrapper<>();
		if (Func.isNotEmpty(skuLot.getSkuLotCode())) {
			queryWrapper.lambda().like(SkuLot::getSkuLotCode, skuLot.getSkuLotCode());
		}
		if (Func.isNotEmpty(skuLot.getSkuLotName())) {
			queryWrapper.lambda().like(SkuLot::getSkuLotName, skuLot.getSkuLotName());
		}
		if (Func.isNotEmpty(skuLot.getWoId())) {
			queryWrapper.lambda().eq(SkuLot::getWoId, skuLot.getWoId());
		}
		return queryWrapper;
	}

	@Override
	public IPage<SkuLotVO> selectPage(IPage<SkuLot> page, SkuLot skuLot) {
		IPage<SkuLot> pages = super.page(page, this.getSelectQueryWrapper(skuLot));
		return SkuLotWrapper.build().pageVO(pages);
	}

	@Override
	public void exportExcel(HashMap<String, Object> params, HttpServletResponse response) {
		//查询批属性信息
		List<SkuLotVO> skuLotList = SkuLotWrapper.build().listVO(this.list(Condition.getQueryWrapper(params, SkuLot.class)));
		//查询所有与批属性相关的货主信息
		List<Long> woIdList = NodesUtil.toList(skuLotList, SkuLot::getWoId);
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		List<Owner> ownerAll = ownerService.listByIds(woIdList);
		List<SkuLotExcel> skuLotExportList = new ArrayList<>();

		for (SkuLot skuLot : skuLotList) {
			// 查询当前批属性的货主
			List<Owner> ownerList = ownerAll.stream().filter(owner -> {
				return owner.getWoId().equals(skuLot.getWoId());
			}).collect(Collectors.toList());
			// 计算最大循环次数，最少循环一次
			Integer maxLength = 1;
			for (int i = 0; i < maxLength; i++) {
				SkuLotExcel skuLotExport = BeanUtil.copy(skuLot, SkuLotExcel.class);
				//封装货主
				if (Func.isNotEmpty(ownerList)) {
					skuLotExport.setOwnerCode(ownerList.get(0).getOwnerCode());
					skuLotExport.setOwnerName(ownerList.get(0).getOwnerName());
				}
				skuLotExportList.add(skuLotExport);
			}
		}

		ExcelUtil.export(response, "批属性", "批属性数据表", skuLotExportList, SkuLotExcel.class);
	}

	@Override
	public List<DataVerify> validExcel(List<SkuLotExcel> dataList) {
		List<DataVerify> dataVerifyList = new ArrayList<>();
		Map<String, SkuLotDTO> cache = new HashMap<>();
		int index = 1;
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		for (SkuLotExcel skuLotExcel : dataList) {
			DataVerify dataVerify = new DataVerify();
			dataVerify.setIndex(index);
			// 封装成DTO
			SkuLotDTO skuLotDTO = SkuLotWrapper.build().entityDTO(skuLotExcel);

			// 开始效验数据
			ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(skuLotDTO, Excel.class);
			// 批属性编码唯一性验证
			//SkuLot findSkuLot = SkuLotCache.getByCode(skuLotDTO.getSkuLotCode());
			SkuLot findSkuLot = super.list(Condition.getQueryWrapper(new SkuLot())
			.lambda()
			.eq(SkuLot::getSkuLotCode,skuLotDTO.getSkuLotCode())
			).stream().findFirst().orElse(null);
			if (Func.isNotEmpty(findSkuLot)) {
				validResult.addError("skuLotCode", "批属性编码[" + skuLotDTO.getSkuLotCode() + "]已存在");
			}
			//Owner owner = OwnerCache.getByCode(skuLotExcel.getOwnerCode());
			Owner owner = ownerService.list(Condition.getQueryWrapper(new Owner())
			.lambda()
			.eq(Owner::getOwnerCode,skuLotExcel.getOwnerCode())
			).stream().findFirst().orElse(null);
			if (Func.isEmpty(owner)) {
				validResult.addError("ownerCode", "货主编码[" + skuLotExcel.getOwnerCode() + "]不存在");
			}else{
				skuLotDTO.setWoId(owner.getWoId());
			}
			if (validResult.hasErrors()) {
				dataVerify.setMessage(StringUtil.join(validResult.getAllErrors()));
			} else {
				cache.put(skuLotDTO.getSkuLotCode(), skuLotDTO);
				dataVerify.setCacheKey(skuLotDTO.getSkuLotCode());
			}
			dataVerifyList.add(dataVerify);
			index++;
		}
		if (Func.isNotEmpty(cache)) {
			// 存储数据到redis缓存中
			for (String code : cache.keySet()) {
				SkuLotCache.put(code, cache.get(code));
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
			SkuLotDTO skuLotDTO = SkuLotCache.get(dataVerify.getCacheKey());
			if (ObjectUtil.isEmpty(skuLotDTO)) {
				continue;
			}
			if (this.saveOrUpdate(skuLotDTO)) {
				SkuLotCache.remove(skuLotDTO.getSkuLotCode());
				//super.saveOrUpdate(skuLotDTO);
			}
		}
		return true;
	}

	@Override
	public List<SkuLotConfigVO> getConfig(Long skuId) {
		List<SkuLotConfigVO> list = new ArrayList<>();
		Sku sku = SkuCache.getById(skuId);
		if (Func.isNotEmpty(sku)) {
			SkuLot skuLot = super.getById(sku.getWslId());
			if (Func.isNotEmpty(skuLot)) {
				ISkuLotValService skuLotValService = SpringUtil.getBean(ISkuLotValService.class);
				SkuLotVal skuLotVal = skuLotValService.getById(sku.getWslvId());
				if (Func.isNotEmpty(skuLotVal)) {
					for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
						SkuLotConfigVO skuLotConfig = new SkuLotConfigVO();
						skuLotConfig.setSkuLotIndex(i);
						// 批属性
						Object value = NodesUtil.getFieldValue(skuLot, "skuLot" + i);
						skuLotConfig.setSkuLot(Func.toStr(value));
						// 批属性标签
						value = NodesUtil.getFieldValue(skuLot, "skuLotLabel" + i);
						skuLotConfig.setSkuLotLabel(Func.toStr(value));
						// 必填项
						value = NodesUtil.getFieldValue(skuLotVal, "skuLotMust" + i);
						skuLotConfig.setSkuLotMust(Func.toInt(value));
						// 是否可见
						value = NodesUtil.getFieldValue(skuLotVal, "skuLotDisp" + i);
						skuLotConfig.setSkuLotDisp(Func.toInt(value));
						// 混合存放
						value = NodesUtil.getFieldValue(skuLotVal, "skuLotMix" + i);
						skuLotConfig.setSkuLotMix(Func.toInt(value));
						// 混合存放掩码
						value = NodesUtil.getFieldValue(skuLotVal, "skuLotMixMask" + i);
						skuLotConfig.setSkuLotMixMask(Func.toStr(value));
						// 批属性生成掩码
						value = NodesUtil.getFieldValue(skuLotVal, "skuLotMask" + i);
						skuLotConfig.setSkuLotMask(Func.toInt(value));
						// 批掩码生成规则
						value = NodesUtil.getFieldValue(skuLotVal, "skuLotEditType" + i);
						skuLotConfig.setSkuLotEditType(Func.toInt(value));
						// 长度上线
						value = NodesUtil.getFieldValue(skuLotVal, "skuLen" + i);
						skuLotConfig.setSkuLen(Func.toInt(value));
						//判断文本框是日期还是text
						try {
							SimpleDateFormat sdf = new SimpleDateFormat(skuLotConfig.getSkuLotMixMask());
							sdf.setLenient(false);
							String date = sdf.format(new Date());
							sdf.parse(date);
							if (skuLotConfig.getSkuLotMixMask().contains("yyyy")) {
								//日期框
								skuLotConfig.setSkuTextType(1);
							} else {
								//文本框
								skuLotConfig.setSkuTextType(0);
							}
						} catch (Exception e) {
							//文本框
							skuLotConfig.setSkuTextType(0);
						}
						list.add(skuLotConfig);
					}
				}
			}
		}

		return list;
	}

	@Override
	public List<SkuLotVO> selectList(SkuLot skuLot) {
		return SkuLotWrapper.build().listVO(super.list(this.getSelectQueryWrapper(skuLot)));
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		ISkuService skuService = SpringUtil.getBean(ISkuService.class);
		List<Sku> skuList = skuService.listByIds(idList);
		for (Sku sku: skuList) {
			if(Func.isNotEmpty(sku)){
				continue;
			}

			SkuLot skuLot = super.getById(sku.getSkuId());

			if (Func.isNotEmpty(skuList)) {
				throw new ServiceException(String.format(
					"批属性[%s] 被物品[名称: %s]占用，请先删除占用物品! ",
					Func.isEmpty(skuLot) ? "ID:" + sku.getSkuId() : "名称:" + skuLot.getSkuLotName(),
					NodesUtil.join(skuList, "skuName")
				));
			}
		}

		boolean result = super.removeByIds(idList);
		if (result) {
			//super.removeByIds(idList);
		}
		return result;
	}

	@Override
	public boolean save(SkuLot entity) {
		//查询单据编号是否编号
		if (super.count(Condition.getQueryWrapper(new SkuLot()).lambda()
			.eq(SkuLot::getSkuLotCode, entity.getSkuLotCode())) > 0) {
			throw new ServiceException("批属性编码重复");
		}
		boolean result = super.save(entity);
		return result;
	}

	@Override
	public boolean updateById(SkuLot entity) {
		QueryWrapper<SkuLot> skuLotQueryWrapper = new QueryWrapper<>();
		skuLotQueryWrapper.eq("wh_id", entity.getWhId());
		skuLotQueryWrapper.eq("wo_id", entity.getWoId());
		skuLotQueryWrapper.eq("sku_lot_code", entity.getSkuLotCode());
		skuLotQueryWrapper.ne("wsl_id", entity.getWslId());

		if (baseMapper.selectList(skuLotQueryWrapper).size() > 0) {
			throw new ServiceException("批属性编码重复");
		}
		boolean result = super.updateById(entity);
		if (result) {

		}
		return result;
	}
}
