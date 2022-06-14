package org.nodes.wms.core.strategy.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictConstant;
import org.nodes.wms.core.strategy.entity.OutstockLog;
import org.nodes.wms.core.strategy.vo.OutstockLogVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;

import java.util.ArrayList;
import java.util.List;


/**
 * 分配记录包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2020-05-06
 */
public class OutstockLogWrapper extends BaseEntityWrapper<OutstockLog, OutstockLogVO> {

	public static OutstockLogWrapper build() {
		return new OutstockLogWrapper();
	}

	@Override
	public OutstockLogVO entityVO(OutstockLog outstockLog) {
		OutstockLogVO outstockLogVO = BeanUtil.copy(outstockLog, OutstockLogVO.class);
		if (Func.isNotEmpty(outstockLogVO)) {
			Warehouse warehouse = WarehouseCache.getById(outstockLog.getWhId());
			if (Func.isNotEmpty(warehouse)) {
				outstockLogVO.setWhName(warehouse.getWhName());
			}
			outstockLogVO.setSuccessDesc(new Integer(0).equals(outstockLog.getIsSuccess()) ? "否" : "是");
		}
		if (Func.isNotEmpty(outstockLogVO.getOutstockFunction())) {
			List<String> descList = new ArrayList<>();
			List<Integer> valueList = Func.toIntList(outstockLogVO.getOutstockFunction());
			valueList.forEach(value -> {
				descList.add(DictCache.getValue(DictConstant.OUTSTOCK_FUNCTION, value));
			});

			outstockLogVO.setOutstockFunctionDesc(Func.join(descList));
		}

		return outstockLogVO;
	}
}
