package org.nodes.wms.biz.basics.skuType;

import org.nodes.wms.dao.basics.skuType.dto.input.SkuTypeAddOrEditRequest;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;

/**
 * 物品分类业务接口
 **/
public interface SkuTypeBiz {

	/**
	 * 新增或编辑
	 *
	 * @param skuTypeAddOrEditRequest: 新增或编辑请求dto对象
	 * @return SkuType
	 */
	SkuType save(SkuTypeAddOrEditRequest skuTypeAddOrEditRequest);
}
