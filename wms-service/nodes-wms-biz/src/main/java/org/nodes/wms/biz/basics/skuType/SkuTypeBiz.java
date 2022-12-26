package org.nodes.wms.biz.basics.skuType;

import org.nodes.wms.dao.basics.skuType.dto.input.SkuTypeAddOrEditRequest;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;

import java.util.List;

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

	/**
	 * 根据id查找物品分类
	 *
	 * @param id 物品分类id
	 * @return 物品分类
	 */
	SkuType findById(Long id);

	/**
	 * 查询所有物品分类
	 */
	List<SkuType> findAll();
}
