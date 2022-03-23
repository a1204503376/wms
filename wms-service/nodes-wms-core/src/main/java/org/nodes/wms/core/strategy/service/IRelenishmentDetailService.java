
package org.nodes.wms.core.strategy.service;

import org.nodes.core.tool.entity.SkuLotBaseEntity;
import org.nodes.wms.core.strategy.dto.RelenishmentDetailDTO;
import org.nodes.wms.core.strategy.entity.Relenishment;
import org.nodes.wms.core.strategy.entity.RelenishmentDetail;
import org.springblade.core.mp.base.BaseService;

import javax.validation.constraints.NotNull;

/**
 * 补货策略明细 服务类
 *
 * @author liangmei
 * @since 2019-12-09
 */
public interface IRelenishmentDetailService extends BaseService<RelenishmentDetail> {

	/**
	 * 新增补货策略明细
	 *
	 * @param relenishmentDetailDTO
	 * @return
	 */
	boolean save(RelenishmentDetailDTO relenishmentDetailDTO);

	/**
	 * 修改补货策略
	 *
	 * @param relenishmentDetailDTO
	 * @return
	 */
	boolean updateById(RelenishmentDetailDTO relenishmentDetailDTO);

	/**
	 * 找到满足条件的补货策略明细
	 *
	 * @param relenishment      补货策略
	 * @param billTypeCd   单据类型编码
	 * @param skuTypeId    物品分类编码
	 * @param skuLotEntity 物品批属性
	 * @return 补货策略明细
	 */
	RelenishmentDetail find(@NotNull Relenishment relenishment, String billTypeCd, Long skuTypeId, SkuLotBaseEntity skuLotEntity);
}
