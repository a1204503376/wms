
package org.nodes.wms.core.basedata.service;

import org.nodes.wms.core.basedata.entity.SkuReplace;
import org.nodes.wms.core.basedata.vo.SkuReplaceVO;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 物品替代 服务类
 *
 * @author pengwei
 * @since 2019-12-23
 */
public interface ISkuReplaceService extends BaseService<SkuReplace> {
	/**
	 * 根据 skuId 查询物品替代集合
	 * @param skuId Sku 主键ID
	 * @return 物品替代集合
	 */
	List<SkuReplaceVO> listBySkuId(Long skuId);
}
