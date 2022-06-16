package org.nodes.wms.dao.basics.sku;

import org.nodes.wms.dao.basics.sku.entities.SkuReplace;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 物品替代 DAO接口
 **/
@Repository
public interface SkuReplaceDao {

	/**
	 * 批量保存物品替代
	 *
	 * @param skuReplaceList: 物品替代集合
	 */
	void saveBatchSkuReplace(List<SkuReplace> skuReplaceList);

	/**
	 * 根据id集合删除物品代替
	 *
	 * @param skuReplaceIdList: id集合
	 */
    void deleteByIdList(List<Long> skuReplaceIdList);
}
