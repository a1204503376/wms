package org.nodes.wms.core.basedata.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.nodes.wms.core.basedata.cache.OwnerCache;
import org.nodes.wms.core.basedata.cache.SkuTypeCache;
import org.nodes.wms.core.basedata.dto.SkuTypeDTO;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.core.basedata.entity.SkuType;
import org.nodes.wms.core.basedata.excel.SkuTypeExcel;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.basedata.service.ISkuTypeService;
import org.nodes.wms.core.basedata.vo.SkuTypeVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author wanglei
 * @program 物品分类封装类
 * @description 将物品分类实体封装为Vo
 * @create 20191128
 */
public class SkuTypeWrapper extends BaseEntityWrapper<SkuType, SkuTypeVO> {

	/**
	 * 物品分类封装
	 */
	public static SkuTypeWrapper build() {
		return new SkuTypeWrapper();
	}

	/**
	 * @program 物品分类封装方法
	 * @description 将物品分类实体封装为Vo
	 * @author wanglei
	 * @create 20191128
	 */
	@Override
	public SkuTypeVO entityVO(SkuType skuType) {
		SkuTypeVO skuTypeVo = Objects.requireNonNull(BeanUtil.copy(skuType, SkuTypeVO.class));
		if (Func.equals(skuTypeVo.getTypePreId(), BladeConstant.TOP_PARENT_ID)) {
			skuTypeVo.setParentName(BladeConstant.TOP_PARENT_NAME);
		} else {
			skuTypeVo.setParentId(skuType.getTypePreId());
			ISkuTypeService skuTypeService = SpringUtil.getBean(ISkuTypeService.class);
			SkuType skuTypePre = skuTypeService.getById(skuType.getTypePreId());
			if (Func.isNotEmpty(skuTypePre))
				skuTypeVo.setParentName(skuTypePre.getTypeName());
		}
		if (Func.isNotEmpty(skuType.getWoId())) {
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			Owner owner = ownerService.getById(skuType.getWoId());
			if (Func.isNotEmpty(owner))
				skuTypeVo.setOwnerName(owner.getOwnerName());
		}
		return skuTypeVo;
	}
	/**
	 * 实体转导出DTO
	 *
	 * @param skuType
	 * @return
	 */
	public SkuTypeExcel entityToExportDTO(SkuType skuType) {
		return BeanUtil.copy(skuType, SkuTypeExcel.class);
	}
	public SkuTypeDTO skuTypeDTO(SkuTypeExcel skuTypeExcel) {
		SkuTypeDTO skuTypeDTO = null;
		String val_code = skuTypeExcel.getTypeCode();
		String val_name = skuTypeExcel.getTypeName();
		String val_woCode = skuTypeExcel.getWoCode();
		String val_typePreName = skuTypeExcel.getTypePreName();
		if (Func.isEmpty(skuTypeExcel.getGradeNum())){
			skuTypeExcel.setGradeNum(new BigDecimal(0));
		}
		String val_gradeNum = skuTypeExcel.getGradeNum().stripTrailingZeros().toPlainString();
		String val_typeRemark = skuTypeExcel.getTypeRemark();


		if (ObjectUtil.isNotEmpty(val_code) || ObjectUtil.isNotEmpty(val_name) || ObjectUtil.isNotEmpty(val_woCode) ||
			ObjectUtil.isNotEmpty(val_typePreName) || ObjectUtil.isNotEmpty(val_gradeNum) ||
			ObjectUtil.isNotEmpty(val_typeRemark)) {

			skuTypeDTO = new SkuTypeDTO();
			skuTypeDTO.setTypeCode(val_code);
			skuTypeDTO.setTypeName(val_name);
			skuTypeDTO.setParentName(val_typePreName);
			//Owner owner = OwnerCache.getByCode(val_woCode);
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			Owner owner = ownerService.list(Condition.getQueryWrapper(new Owner())
			.lambda()
			.eq(Owner::getOwnerCode,val_woCode)
			).stream().findFirst().orElse(null);
			if (ObjectUtil.isNotEmpty(owner)) {
				skuTypeDTO.setWoId(owner.getWoId());
				skuTypeDTO.setOwnerName(owner.getOwnerName());
			}
			if (Func.isNotEmpty(val_typePreName)) {
				ISkuTypeService skuTypeService = SpringUtil.getBean(ISkuTypeService.class);
				QueryWrapper<SkuType> oqw = new QueryWrapper<>();
				oqw.lambda().eq(SkuType::getTypeName, val_typePreName).last("limit 1");
				SkuType skuType = skuTypeService.getOne(oqw);
				if (Func.isNotEmpty(skuType)) {
					if (!skuType.getWoId().equals(owner.getWoId())) {

					}
					skuTypeDTO.setTypePreId(skuType.getSkuTypeId());
				}
			} else {
				skuTypeDTO.setTypePreId(null);
			}
			if (Func.isNotEmpty(val_gradeNum)) {
				skuTypeDTO.setGradeNum(new BigDecimal(val_gradeNum));
			} else {
				skuTypeDTO.setGradeNum(null);
			}
			skuTypeDTO.setTypeRemark(val_typeRemark);
		}
		if (Func.isNotEmpty(skuTypeExcel.getWoCode())){
			skuTypeDTO.setWoCode(skuTypeExcel.getWoCode());
		}
		return skuTypeDTO;
	}
}
