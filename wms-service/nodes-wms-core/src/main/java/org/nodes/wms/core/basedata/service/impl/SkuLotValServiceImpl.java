
package org.nodes.wms.core.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.ValidationUtil;
import org.nodes.core.tool.validation.Excel;
import org.nodes.wms.core.basedata.cache.OwnerCache;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuLotValCache;
import org.nodes.wms.core.basedata.dto.SkuLotValDTO;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.entity.SkuLotVal;
import org.nodes.wms.core.basedata.excel.SkuLotValExcel;
import org.nodes.wms.core.basedata.mapper.SkuLotValMapper;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.basedata.service.ISkuLotValService;
import org.nodes.wms.core.basedata.service.ISkuService;
import org.nodes.wms.core.basedata.vo.SkuLotValVO;
import org.nodes.wms.core.basedata.wrapper.SkuLotValWrapper;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * 批属性验证 服务实现类
 *
 * @author chenhz
 * @since 2019-12-18
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SkuLotValServiceImpl<M extends SkuLotValMapper, T extends SkuLotVal>
	extends BaseServiceImpl<SkuLotValMapper, SkuLotVal>
	implements ISkuLotValService {

	/**
	 * 获取模糊查询构造器
	 *
	 * @param skuLotVal 查询条件
	 * @return
	 */
	private QueryWrapper<SkuLotVal> getSelectQueryWrapper(SkuLotVal skuLotVal) {
		QueryWrapper<SkuLotVal> queryWrapper = new QueryWrapper<>();
		if (Func.isNotEmpty(skuLotVal.getSkuLotValName())) {
			queryWrapper.lambda().like(SkuLotVal::getSkuLotValName, skuLotVal.getSkuLotValName());
		}
		return queryWrapper;
	}


	@Override
	public List<SkuLotValVO> selectList(SkuLotVal skuLotVal) {
		return SkuLotValWrapper.build().listVO(super.list(this.getSelectQueryWrapper(skuLotVal)));
	}

	@Override
	public IPage<SkuLotValVO> selectPage(IPage<SkuLotVal> page, SkuLotVal skuLotVal) {
		return SkuLotValWrapper.build().pageVO(super.page(page, this.getSelectQueryWrapper(skuLotVal)));
	}

	@Override
	public void exportExcel(HashMap<String, Object> params, HttpServletResponse response) {
		//查询批属性信息
		List<SkuLotValVO> skuLotValList = SkuLotValWrapper.build().listVO(this.list(Condition.getQueryWrapper(params, SkuLotVal.class)));
		//查询所有与批属性相关的货主信息
		List<Long> woIdList = NodesUtil.toList(skuLotValList, SkuLotVal::getWoId);
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		List<Owner> ownerAll = ownerService.listByIds(woIdList);
		/*List<Owner> ownerAll = ownerService.list(Condition.getQueryWrapper(new Owner())
		.lambda()
		.in(Owner::getWoId,woIdList));*/
		List<SkuLotValExcel> skuLotValExportList = new ArrayList<>();

		for (SkuLotVal skuLotVal : skuLotValList) {
			// 查询当前批属性的货主
			List<Owner> ownerList = ownerAll.stream().filter(owner -> {
				return owner.getWoId().equals(skuLotVal.getWoId());
			}).collect(Collectors.toList());
			// 计算最大循环次数，最少循环一次
			Integer maxLength = 1;
			for (int i = 0; i < maxLength; i++) {
				SkuLotValExcel skuLotValExport = BeanUtil.copy(skuLotVal, SkuLotValExcel.class);
				//封装货主
				if (Func.isNotEmpty(ownerList)) {
					skuLotValExport.setOwnerCode(ownerList.get(0).getOwnerCode());
					skuLotValExport.setOwnerName(ownerList.get(0).getOwnerName());
				}
				skuLotValExportList.add(skuLotValExport);
			}
		}

		ExcelUtil.export(response, "批属性验证", "批属性验证数据表", skuLotValExportList, SkuLotValExcel.class);
	}

	@Override
	public List<DataVerify> validExcel(List<SkuLotValExcel> dataList) {
		List<DataVerify> dataVerifyList = new ArrayList<>();
		Map<String, SkuLotValDTO> cache = new HashMap<>();
		int index = 1;
		for (SkuLotValExcel skuLotValExcel : dataList) {
			DataVerify dataVerify = new DataVerify();
			dataVerify.setIndex(index);
			// 封装成DTO
			SkuLotValDTO skuLotValDTO = SkuLotValWrapper.build().entityDTO(skuLotValExcel);
			// 开始效验数据
			ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(skuLotValDTO, Excel.class);
			// 批属性验证名称唯一性验证
			//SkuLotVal findSkuLotVal = SkuLotValCache.getByName(skuLotValDTO.getSkuLotValName());
			SkuLotVal findSkuLotVal = super.list(Condition.getQueryWrapper(new SkuLotVal())
				.lambda()
				.eq(SkuLotVal::getSkuLotValName, skuLotValDTO.getSkuLotValName())
			).stream().findFirst().orElse(null);
			if (Func.isNotEmpty(findSkuLotVal)) {
				validResult.addError("skuLotValName", "批属性编码[" + skuLotValDTO.getSkuLotValName() + "]已存在");
			}
			//查询货主是否存在
			//Owner owner = OwnerCache.getByCode(skuLotValExcel.getOwnerCode());
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			Owner owner = ownerService.list(Condition.getQueryWrapper(new Owner())
				.lambda()
				.eq(Owner::getOwnerCode, skuLotValExcel.getOwnerCode())
			).stream().findFirst().orElse(null);
			if (Func.isEmpty(owner)) {
				validResult.addError("ownerCode", "货主编码[" + skuLotValExcel.getOwnerCode() + "]不存在");
			} else {
				skuLotValDTO.setWoId(owner.getWoId());
			}
			if (validResult.hasErrors()) {
				dataVerify.setMessage(StringUtil.join(validResult.getAllErrors()));
			} else {
				cache.put(skuLotValDTO.getSkuLotValName(), skuLotValDTO);
				dataVerify.setCacheKey(skuLotValDTO.getSkuLotValName());
			}
			dataVerifyList.add(dataVerify);
			index++;
		}
		if (Func.isNotEmpty(cache)) {
			// 存储数据到redis缓存中
			for (String name : cache.keySet()) {
				SkuLotValCache.put(name, cache.get(name));
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
			SkuLotValDTO skuLotValDTO = SkuLotValCache.get(dataVerify.getCacheKey());
			if (ObjectUtil.isEmpty(skuLotValDTO)) {
				continue;
			}
			if (this.saveOrUpdate(skuLotValDTO)) {
				SkuLotValCache.remove(skuLotValDTO.getSkuLotValName());
				//super.saveOrUpdate(skuLotValDTO);
			}
		}
		return true;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		ISkuService skuService = SpringUtil.getBean(ISkuService.class);
		List<Sku> skuList = skuService.list(Condition.getQueryWrapper(new Sku())
			.lambda()
			.in(Sku::getWslvId, idList)
		);
		for (Serializable id : idList) {
			SkuLotVal skuLotVal = super.getById(id);

			if (Func.isNotEmpty(skuList)) {
				throw new ServiceException(String.format(
					"批属性验证[%s] 被物品[名称: %s]占用，请先删除占用信息! ",
					Func.isEmpty(skuLotVal) ? "ID:" + id : "名称:" + skuLotVal.getSkuLotValName(),
					NodesUtil.join(skuList, "skuName")
				));
			}
		}
		return super.removeByIds(idList);
	}


	public boolean saveOrUpdate(SkuLotVal skuLotVal) {
		long count = super.count(Condition.getQueryWrapper(new SkuLotVal())
			.lambda()
			.eq(SkuLotVal::getSkuLotValName, skuLotVal.getSkuLotValName())
			.ne(SkuLotVal::getWslvId, skuLotVal.getWslvId()));
		if (count > 0L) {
			throw new ServiceException("该名称已被其他批属性验证使用");
		}
		return super.saveOrUpdate(skuLotVal);
	}
}




