
package org.nodes.wms.core.basedata.service;

import org.nodes.wms.dao.basics.sku.entities.SkuInc;
import org.nodes.wms.core.basedata.vo.SkuIncVO;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 物品供应商关联 服务类
 *
 * @author pengwei
 * @since 2019-12-23
 */
public interface ISkuIncService extends BaseService<SkuInc> {
	/**
	 * 根据 SkuId 获取物品供应商关联集合
	 * @param skuId Sku 主键ID
	 * @return 物品供应商关联集合
	 */
	List<SkuIncVO> listBySkuId(Long skuId);
}
