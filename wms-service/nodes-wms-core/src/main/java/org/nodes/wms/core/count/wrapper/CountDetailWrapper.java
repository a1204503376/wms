package org.nodes.wms.core.count.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.constant.DictConstant;
import org.nodes.wms.core.count.entity.CountDetail;
import org.nodes.wms.core.count.vo.CountDetailVO;
import org.nodes.wms.core.warehouse.entity.Location;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.*;

/**
 * @author pengwei
 * @program WmsCore
 * @description 盘点明细封装类
 * @create 20200401
 */
public class CountDetailWrapper extends BaseEntityWrapper<CountDetail, CountDetailVO> {

	public static CountDetailWrapper build() {
		return new CountDetailWrapper();
	}

	@Override
	public CountDetailVO entityVO(CountDetail entity) {
		CountDetailVO countDetailVO = BeanUtil.copy(entity, CountDetailVO.class);
		if (ObjectUtil.isNotEmpty(countDetailVO)) {
			countDetailVO.setLocStatusDesc(DictCache.getValue(Location.STATUS, entity.getLocState()));
		}
		return countDetailVO;
	}
}
