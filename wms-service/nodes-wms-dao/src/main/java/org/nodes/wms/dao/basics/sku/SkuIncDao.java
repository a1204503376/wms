package org.nodes.wms.dao.basics.sku;

import org.nodes.wms.dao.basics.sku.entities.SkuInc;
import org.nodes.wms.dao.basics.sku.entities.SkuReplace;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 物品关联供应商 DAO接口
 **/
@Repository
public interface SkuIncDao {

	/**
	 * 批量保存物品关联供应商
	 *
	 * @param skuIncList: 物品关联供应商集合
	 */
	void saveBatchSkuInc(List<SkuInc> skuIncList);

	/**
	 * 根据id集合删除物品供应商关联
	 *
	 * @param skuIncIdList: id集合
	 */
    void deleteByIdList(List<Long> skuIncIdList);
}
