
package org.nodes.wms.core.strategy.service;

import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.core.strategy.dto.InstockDetailDTO;
import org.nodes.wms.dao.putaway.entities.StInstock;
import org.nodes.wms.dao.putaway.entities.StInstockDetail;
import org.springblade.core.mp.base.BaseService;

import javax.validation.constraints.NotNull;

/**
 * 上架策略明细 服务类
 *
 * @author liangmei
 * @since 2019-12-09
 */
public interface IInstockDetailService extends BaseService<StInstockDetail> {

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
	StInstockDetail find(@NotNull StInstock instock, String billTypeCd, Long skuTypeId, SkuLotBaseEntity skuLotEntity);
}
