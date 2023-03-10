package org.nodes.wms.core.strategy.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictCodeConstant;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.nodes.wms.core.strategy.entity.WellenDetail;
import org.nodes.wms.core.strategy.vo.WellenDetailVO;


import org.springblade.core.tool.utils.Func;


/**
 * 波次策略明细包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2021-05-26
 */
public class WellenDetailWrapper extends BaseEntityWrapper<WellenDetail, WellenDetailVO> {

	public static WellenDetailWrapper build() {
		return new WellenDetailWrapper();
	}

	@Override
	public WellenDetailVO entityVO(WellenDetail wellenDetail) {
		WellenDetailVO wellenDetailVO = BeanUtil.copy(wellenDetail, WellenDetailVO.class);
		if (Func.isNotEmpty(wellenDetailVO)) {
			//订单字段
			wellenDetailVO.setBillFieldName(DictCache.getValue(DictCodeConstant.ST_WELLEN_FIELD, wellenDetail.getBillField()));
			//操作符
			wellenDetailVO.setOperationName(DictCache.getValue(DictCodeConstant.ST_WELLEN_OPERATION, wellenDetail.getOperation()));
			//条件
			wellenDetailVO.setCriteriaName(DictCache.getValue(DictCodeConstant.ST_WELLEN_CRITERIA, wellenDetail.getCriteria()));
		}
		return wellenDetailVO;
	}
}
