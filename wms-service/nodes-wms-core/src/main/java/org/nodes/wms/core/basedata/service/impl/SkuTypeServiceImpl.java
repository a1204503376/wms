package org.nodes.wms.core.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.constant.CommonConstant;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.basedata.service.ISkuService;
import org.springblade.core.log.exception.ServiceException;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.ValidationUtil;
import org.nodes.core.tool.validation.Excel;
import org.nodes.wms.core.basedata.cache.SkuTypeCache;
import org.nodes.wms.core.basedata.dto.SkuTypeDTO;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.sku.entities.SkuType;
import org.nodes.wms.core.basedata.excel.SkuTypeExcel;
import org.nodes.wms.core.basedata.mapper.SkuTypeMapper;
import org.nodes.wms.core.basedata.service.ISkuTypeService;
import org.nodes.wms.core.basedata.vo.SkuTypeVO;
import org.nodes.wms.core.basedata.wrapper.SkuTypeWrapper;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.node.ForestNodeMerger;
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
 * @author wanglei
 * @program 物品分类服务
 * @description 物品分类服务接口实现
 * @create 20191128
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SkuTypeServiceImpl<M extends SkuTypeMapper, T extends SkuType>
	extends BaseServiceImpl<SkuTypeMapper, SkuType>
	implements ISkuTypeService {

	@Override
	public List<SkuTypeVO> tree(SkuType skuType) {
		return ForestNodeMerger.merge(baseMapper.tree(skuType));
	}

	public List<SkuTypeVO> selectList(SkuType skuType) {
		return SkuTypeWrapper.build().listVO(super.list(this.getSelectQueryWrapper(skuType)));
	}
	private QueryWrapper<SkuType> getSelectQueryWrapper(SkuType skuType) {
		QueryWrapper<SkuType> queryWrapper = new QueryWrapper<>();
		if (Func.isNotEmpty(skuType.getTypeCode())) {
			queryWrapper.lambda().like(SkuType::getTypeCode, skuType.getTypeCode());
		}
		if (Func.isNotEmpty(skuType.getTypeName())) {
			queryWrapper.lambda().like(SkuType::getTypeName, skuType.getTypeName());
		}
		if (Func.isNotEmpty(skuType.getWoId())) {
			queryWrapper.lambda().eq(SkuType::getWoId, skuType.getWoId());
		}
		return queryWrapper;
	}
	private List<SkuTypeExcel> getSkuTypeExportDTOList(List<SkuTypeVO> skuTypeList) {
		// 用来导出的最终分类列表
		List<SkuTypeExcel> skuTypeExportList = new ArrayList<>();
		// 查询所有货主
		List<Long> woIdList = NodesUtil.toList(skuTypeList, SkuType::getWoId);
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		List<Owner> ownerAll = ownerService.list().stream().filter(u -> {
			return woIdList.contains(u.getWoId());
		}).collect(Collectors.toList());

		// 循环查询出来的分类列表
		for (SkuType skuType : skuTypeList) {
			// 查询当前分类的所有货主
			List<Owner> ownerList = ownerAll.stream().filter(owner ->
				owner.getWoId().equals(skuType.getWoId())).collect(Collectors.toList());

			for (int i = 0; i < ownerList.size(); i++) {
				SkuTypeExcel skuTypeExportDTO = SkuTypeWrapper.build().entityToExportDTO(skuType);
				//货主
				if (Func.isNotEmpty(ownerList)) {
					skuTypeExportDTO.setWoCode(ownerList.get(0).getOwnerCode());
					skuTypeExportDTO.setWoName(ownerList.get(0).getOwnerName());
				}
				//封装上级分类数据
				SkuType skuTypeper = new SkuType();
				skuTypeper.setSkuTypeId(skuType.getTypePreId());
				skuTypeper = this.getOne(Condition.getQueryWrapper(skuTypeper));
				if (Func.isNotEmpty(skuTypeper)) {
					skuTypeExportDTO.setTypePreName(skuTypeper.getTypeName());
				}
				//将仓库装入新list
				skuTypeExportList.add(skuTypeExportDTO);
			}
		}
		return skuTypeExportList;
	}
	@Override
	public void exportExcel(HashMap<String, Object> params, HttpServletResponse response) {
		List<SkuTypeVO> skuTypeList = SkuTypeWrapper.build().listVO(this.list(Condition.getQueryWrapper(params, SkuType.class)));
		if (Func.isNotEmpty(skuTypeList)) {
			List<SkuTypeExcel> SkuTypeExcels = getSkuTypeExportDTOList(skuTypeList);
			ExcelUtil.export(response, "物品分类", "物品分类表", SkuTypeExcels, SkuTypeExcel.class);
		}
	}
	public SkuType getByCode(String code) {
		SkuType skuType = new SkuType();
		skuType.setTypeCode(code);
		return list(Condition.getQueryWrapper(skuType)).stream().findFirst().orElse(null);
	}
	@Override
	public boolean updateById(SkuTypeDTO skuTypeVO) {
		if (Func.isEmpty(super.getById(skuTypeVO.getSkuTypeId())))
			throw new ServiceException("物品分类主键不存在:skuTypeId");

		SkuType skuType = BeanUtil.copy(skuTypeVO, SkuType.class);
		//查询分类名称+分类编码+货主是否重复 大于0
		QueryWrapper<SkuType> skuTypeQueryWrapper = new QueryWrapper<>();
		skuTypeQueryWrapper.eq("is_deleted", 0);
		skuTypeQueryWrapper.eq("type_code", skuType.getTypeCode());
		skuTypeQueryWrapper.eq("type_name", skuType.getTypeName());
		skuTypeQueryWrapper.eq("wo_id", skuType.getWoId());
		skuTypeQueryWrapper.ne("sku_type_id", skuType.getSkuTypeId());
		if (baseMapper.selectList(skuTypeQueryWrapper).size() > 0) {
			throw new RuntimeException("分类编码和货主不能同时重复");
		}
		//skuType.setTypePreId(skuTypeVO.gParentId());
		if (Func.isEmpty(skuType.getTypePreId())) {
			skuType.setTypePreId(CommonConstant.TOP_PARENT_ID);
		}
		if (skuType.getTypePreId().equals(skuType.getSkuTypeId())) {
			throw new ServiceException("上位分类 不能与 当前分类一致！");
		} else if (!skuType.getTypePreId().equals(CommonConstant.TOP_PARENT_ID)){
			SkuType parentSkuType = super.getById(skuType.getTypePreId());
			if (Func.isNotEmpty(parentSkuType)
				&& parentSkuType.getTypePath().indexOf(skuType.getSkuTypeId().toString()) > -1) {
				throw new ServiceException("上位分类 不能为 当前分类 的子级！");
			}
		}


		if (skuType.getTypePreId() == 0) {
			skuType.setTypePath("#" + Func.toStr(skuType.getSkuTypeId()));
			return (this.updateById(skuType));
		} else {
			SkuType skuTypeper = new SkuType();
			skuTypeper.setSkuTypeId(skuType.getTypePreId());
			skuTypeper = this.getOne(Condition.getQueryWrapper(skuTypeper));
			skuType.setTypePath(skuTypeper.getTypePath() + "#" + Func.toStr(skuType.getSkuTypeId()));
			return this.updateById(skuType);
		}
	}

	@Override
	public List<DataVerify> validExcel(List<SkuTypeExcel> dataList) {
		List<DataVerify> dataVerifyList = new ArrayList<>();
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		Map<String, SkuTypeDTO> cache = new HashMap<>();
		int index = 1;
		for (SkuTypeExcel skuTypeExcel : dataList) {
			DataVerify dataVerify = new DataVerify();
			dataVerify.setIndex(index);
			// 封装成DTO
			SkuTypeDTO skuTypeDTO = SkuTypeWrapper.build().skuTypeDTO(skuTypeExcel);
			// 校验数据
			if (ObjectUtil.isEmpty(skuTypeDTO)) {
				continue;
			}
			ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(skuTypeDTO, Excel.class);
			//查询货主是否存在
			if (Func.isEmpty(skuTypeDTO.getWoCode())){
				validResult.addError("woCode", "货主编码不能为空");
			} else {
				//Owner owner=OwnerCache.getByCode(skuTypeDTO.getWoCode());
				Owner owner = ownerService.list(Condition.getQueryWrapper(new Owner())
				.lambda()
				.eq(Owner::getOwnerCode,skuTypeDTO.getWoCode())
				).stream().findFirst().orElse(null);
				if (Func.isEmpty(owner)){
					validResult.addError("woCode", "货主编码[" + skuTypeDTO.getWoCode() + "]不存在");
				}
			}


			// 分类编码唯一性验证
			SkuType findObj = this.getByCode(skuTypeDTO.getTypeCode());
			if (ObjectUtil.isNotEmpty(findObj)) {
				validResult.addError(
					"typeCode",
					"分类编码" + skuTypeDTO.getTypeCode() + "已存在");
			}
			// 验证上位分类的货主是否与当前分类的货主一致
			if (Func.isNotEmpty(skuTypeDTO.getSkuTypeId())) {
				SkuType skuType = super.getById(skuTypeDTO.getTypePreId());
				if (Func.isEmpty(skuType)) {
					validResult.addError("typePreId", "上位分类不存在！");
				}
				if (!skuType.getWoId().equals(skuTypeDTO.getWoId())) {
					validResult.addError("typePreId", "上位分类指定货主与当前分类货主不一致！");
				}
			}
			if (validResult.hasErrors()) {
				dataVerify.setMessage(StringUtil.join(validResult.getAllErrors()));
			} else {
				cache.put(skuTypeDTO.getTypeCode()+skuTypeExcel.getWoCode(), skuTypeDTO);
				dataVerify.setCacheKey(skuTypeDTO.getTypeCode()+skuTypeExcel.getWoCode());
			}
			dataVerifyList.add(dataVerify);
			index++;
		}
		if (Func.isNotEmpty(cache)) {
			// 存储数据到redis缓存中
			for (String code : cache.keySet()) {
				SkuTypeCache.put(code, cache.get(code));
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
			SkuTypeDTO skuTypeDTO = SkuTypeCache.get(dataVerify.getCacheKey());
			if (ObjectUtil.isEmpty(skuTypeDTO)) {
				continue;
			}

			if (this.save(skuTypeDTO)) {
				SkuTypeCache.remove(dataVerify.getCacheKey());
				//super.saveOrUpdate(skuTypeDTO);
			}
		}
		return true;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		Integer cnt = super.count(
			Wrappers.<SkuType>query().lambda().in(SkuType::getTypePreId, idList));
		if (cnt > 0) {
			throw new ServiceException("请先删除子节点!");
		}
		ISkuService skuService = SpringUtil.getBean(ISkuService.class);
	/*	Long skuCount = skuService.list().stream().filter(sku->{
			return idList.contains(sku.getSkuTypeId());
		}).count();*/
		long skuCount = skuService.count(Condition.getQueryWrapper(new Sku())
		.lambda()
		.in(Sku::getSkuTypeId,idList));

		if (skuCount > 0) {
			throw new ServiceException("请先删除该分类下的物品信息！");
		}
		return super.removeByIds(idList);
	}
	@Override
	public boolean saveOrUpdate(SkuTypeDTO skuType) {
		return super.getIdVal(skuType) == null ? this.save(skuType) : this.updateById(skuType);
	}
	@Override
	public boolean updateById(SkuType entity) {
		if (Func.isEmpty(super.getById(entity.getSkuTypeId()))) {
			throw new ServiceException("物品分类主键不存在:skuTypeId");
		}
		//查询分类名称+分类编码+货主是否重复 大于0
		QueryWrapper<SkuType> skuTypeQueryWrapper = new QueryWrapper<>();
		skuTypeQueryWrapper.eq("type_code", entity.getTypeCode());
//		skuTypeQueryWrapper.eq("type_name", entity.getTypeName());
		skuTypeQueryWrapper.eq("wo_id", entity.getWoId());
		skuTypeQueryWrapper.ne("sku_type_id", entity.getSkuTypeId());
		if (super.count(skuTypeQueryWrapper) > 0) {
			throw new ServiceException("分类编码和货主不能同时重复");
		}
		if (Func.isEmpty(entity.getTypePreId())) {
			entity.setTypePreId(CommonConstant.TOP_PARENT_ID);
		}
		if (entity.getTypePreId().equals(entity.getSkuTypeId())) {
			throw new ServiceException("上位分类 不能与 当前分类一致！");
		} else if (!entity.getTypePreId().equals(CommonConstant.TOP_PARENT_ID)){
			SkuType parentSkuType = super.getById(entity.getTypePreId());
			if (Func.isNotEmpty(parentSkuType)
				&& parentSkuType.getTypePath().indexOf(entity.getSkuTypeId().toString()) > -1) {
				throw new ServiceException("上位分类 不能为 当前分类 的子级！");
			}
		}


		if (entity.getTypePreId() == 0) {
			entity.setTypePath("#" + Func.toStr(entity.getSkuTypeId()));
		} else {
			SkuType skuTypeper = new SkuType();
			skuTypeper.setSkuTypeId(entity.getTypePreId());
			skuTypeper = this.getOne(Condition.getQueryWrapper(skuTypeper));
			entity.setTypePath(skuTypeper.getTypePath() + "#" + Func.toStr(entity.getSkuTypeId()));
		}
		return super.updateById(entity);
	}

	@Override
	public boolean save(SkuTypeDTO entity) {
		if (Func.isNotEmpty(super.getById(entity.getSkuTypeId()))) {
			throw new ServiceException("物品分类主键已存在:skuTypeId");
		}
		//查询分类名称+分类编码+货主是否重复
		if (super.count(Condition.getQueryWrapper(new SkuType()).lambda()
//			.eq(SkuType::getTypeName, entity.getTypeName())
			.eq(SkuType::getTypeCode, entity.getTypeCode())
			.eq(SkuType::getWoId, entity.getWoId())) > 0) {
			throw new RuntimeException("名称和分类编码和货主不能同时重复");
		}
		SkuType one = super.getOne(Wrappers.lambdaQuery(SkuType.class)
			.eq(SkuType::getTypeCode, entity.getParentName())
			.eq(SkuType::getWoId,entity.getWoId()));
		if(Func.isNotEmpty(one)){
			entity.setTypePreId(one.getSkuTypeId());
		}
		super.save(entity);//获取ID 保存之后才有
		if (entity.getTypePreId() == (null)) {
			entity.setTypePath("#" + Func.toStr(entity.getSkuTypeId()));
		} else {
			SkuType skuTypeper = new SkuType();
			skuTypeper.setSkuTypeId(entity.getTypePreId());
			skuTypeper = this.getOne(Condition.getQueryWrapper(skuTypeper));
			if (Func.isNotEmpty(skuTypeper)) {
				entity.setTypePath(skuTypeper.getTypePath() + "#" + Func.toStr(entity.getSkuTypeId()));
			} else {
				entity.setTypePath("#" + Func.toStr(entity.getSkuTypeId()));
			}
		}
		return super.updateById(entity);
	}
}
