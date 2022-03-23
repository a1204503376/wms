package org.nodes.wms.core.outstock.loading.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.wms.core.outstock.loading.entity.SoTruckHeader;
import org.nodes.wms.core.outstock.loading.vo.SoTruckHeaderVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.*;

/**
 * @program: WmsCore
 * @description: 装车头表封装类
 * @author: pengwei
 * @create: 2020-04-30 11:00
 **/
public class SoTruckHeaderWrapper extends BaseEntityWrapper<SoTruckHeader, SoTruckHeaderVO> {

	public static SoTruckHeaderWrapper build() {
		return new SoTruckHeaderWrapper();
	}


	@Override
	public SoTruckHeaderVO entityVO(SoTruckHeader entity) {
		SoTruckHeaderVO soTruckHeaderVO = BeanUtil.copy(entity, SoTruckHeaderVO.class);
		if (ObjectUtil.isNotEmpty(soTruckHeaderVO)) {
			soTruckHeaderVO.setTruckStateDesc(DictCache.getValue("truck_state", entity.getTruckState()));
		}
		return soTruckHeaderVO;
	}
}
