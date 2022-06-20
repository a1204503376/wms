package org.nodes.wms.biz.basics.skuType.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.owner.OwnerBiz;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.skuType.SkuTypeDao;
import org.nodes.wms.dao.basics.skuType.dto.input.SkuTypeAddOrEditRequest;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkuTypeFactory {

	private final SkuTypeDao skuTypeDao;

	private final OwnerBiz ownerBiz;

	public SkuType createSkuType(SkuTypeAddOrEditRequest skuTypeAddOrEditRequest) {
		String isAdd = Func.isEmpty(skuTypeAddOrEditRequest.getSkuTypeId()) ? "新增" : "编辑";
		SkuType skuType = new SkuType();
		Func.copy(skuTypeAddOrEditRequest, skuType);
		// 校验货主 根据货主id获取货主信息
		Owner owner = ownerBiz.findById(skuTypeAddOrEditRequest.getWoId());
		if (Func.isEmpty(owner)){
			throw new ServiceException(String.format("%s失败，货主不存在",isAdd));
		}
		// 校验 分类编码和货主不能同时重复 新增：异常 编辑：判断查出的id与传入的id是否相同 相同不异常 不相同异常
		SkuType result = skuTypeDao.selectSkuTypeByCodeAndWoId(skuTypeAddOrEditRequest.getTypeCode(),owner.getWoId());
		if(Func.isNotEmpty(result) && !skuTypeAddOrEditRequest.getSkuTypeId().equals(result.getSkuTypeId())){
			throw new ServiceException(String.format("%s失败，分类编码和货主不能同时重复",isAdd));
		}
		return skuType;
	}
}
