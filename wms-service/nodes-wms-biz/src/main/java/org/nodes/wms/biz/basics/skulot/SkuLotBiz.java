package org.nodes.wms.biz.basics.skulot;

import org.nodes.wms.dao.basics.skulot.dto.input.FindAllSkuLotRequest;
import org.nodes.wms.dao.basics.skulot.dto.output.FindAllSkuLotResponse;

/**
 * 批属性Biz
 */
public interface SkuLotBiz {
	/**
	 * PDA组件获取信息
	 *
	 * @param request 请求对象 包含货主id
	 * @return PDA批属性组件响应对象
	 */
	FindAllSkuLotResponse selectsAllSkuLotByWoId(FindAllSkuLotRequest request);
}
