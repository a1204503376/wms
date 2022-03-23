
package org.nodes.wms.core.strategy.service;

import org.nodes.core.tool.entity.SkuLotBaseEntity;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.strategy.dto.InstockDetailDTO;
import org.nodes.wms.core.strategy.entity.Instock;
import org.nodes.wms.core.strategy.entity.InstockDetail;
import org.springblade.core.mp.base.BaseService;

import javax.validation.constraints.NotNull;

/**
 * 上架策略明细 服务类
 *
 * @author liangmei
 * @since 2019-12-09
 */
public interface IInstockDetailService extends BaseService<InstockDetail> {

	/**
	 * 新增上架策略明细
	 *
	 * @param instockDetailDTO
	 * @return
	 */
	boolean save(InstockDetailDTO instockDetailDTO);

	/**
	 * 修改上架策略
	 *
	 * @param instockDetailDTO
	 * @return
	 */
	boolean updateById(InstockDetailDTO instockDetailDTO);

	/**
	 * 找到满足条件的上架策略明细
	 *
	 * @param instock      上架策略
	 * @param billTypeCd   单据类型编码
	 * @param skuTypeId    物品分类编码
	 * @param skuLotEntity 物品批属性
	 * @return 上架策略明细
	 */
	InstockDetail find(@NotNull Instock instock, String billTypeCd, Long skuTypeId, SkuLotBaseEntity skuLotEntity);
}
