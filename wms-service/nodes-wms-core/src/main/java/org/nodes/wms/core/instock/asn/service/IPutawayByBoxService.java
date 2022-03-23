package org.nodes.wms.core.instock.asn.service;

import org.nodes.wms.core.instock.asn.vo.BoxItemVo;
import org.nodes.wms.core.strategy.vo.InstockExecuteVO;
import org.nodes.wms.core.instock.asn.dto.PutawayByBoxQueryDTO;
import org.nodes.wms.core.instock.asn.entity.AsnHeader;
import org.nodes.wms.core.instock.asn.vo.PutawayByBoxSubimitVO;
import org.springblade.core.mp.base.BaseService;

/**
 * 按箱上架接口
 *
 * @Author zx
 * @Date 2020/6/29
 **/
public interface IPutawayByBoxService extends BaseService<AsnHeader> {

	/**
	 * 按箱上架 获得上架信息
	 * @param putawayByBoxQueryDTO
	 * @return
	 */
	InstockExecuteVO queryStockForBox(PutawayByBoxQueryDTO putawayByBoxQueryDTO);

	/**
	 * 按箱上架 提交上架信息
	 * @param putawayByBoxSubimitVO
	 * @return
	 */
	boolean submitPutaway(PutawayByBoxSubimitVO putawayByBoxSubimitVO);

	BoxItemVo getRecommendInfoByBoxCode(String boxCode);

	boolean submitPutawayNew(PutawayByBoxSubimitVO putawayByBoxSubimitVO);

	boolean submitMoveForBoxNew(PutawayByBoxSubimitVO putawayByBoxSubimitVO);

	BoxItemVo getInfoMoveByBoxCode(String boxCode);
}
