
package org.nodes.wms.core.strategy.service;

import org.nodes.core.tool.entity.SkuLotBaseEntity;
import org.nodes.wms.core.strategy.dto.InstockConfigLotDTO;
import org.nodes.wms.dao.putway.entities.StInstockConfigLot;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 上架策略批属性设定 服务类
 *
 * @author liangmei
 * @since 2019-12-09
 */
public interface IInstockConfigLotService extends BaseService<StInstockConfigLot> {

	/**
	 * 修改上架策略批属性设定
	 *
	 * @param instockConfigLotDTO
	 * @return
	 */
	boolean updateById(InstockConfigLotDTO instockConfigLotDTO);

	/**
	 * 匹配批属性设置
	 *
	 * @param instockConfigLotList 批属性设置集合
	 * @param skuLotEntity         批属性类
	 * @return 是否匹配
	 */
	boolean match(List<StInstockConfigLot> instockConfigLotList, SkuLotBaseEntity skuLotEntity);
}
