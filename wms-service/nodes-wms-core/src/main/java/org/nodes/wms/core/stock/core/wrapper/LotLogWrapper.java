
package org.nodes.wms.core.stock.core.wrapper;

import com.sun.xml.bind.v2.model.core.ID;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.entity.User;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.UserCache;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.base.service.IUserService;
import org.nodes.wms.core.stock.core.entity.LotLog;
import org.nodes.wms.core.stock.core.vo.LotLogVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.*;

/**
 * 批次号日志包装类,返回视图层所需的字段
 *
 * @author pengwei
 * @since 2020-03-03
 */
public class LotLogWrapper extends BaseEntityWrapper<LotLog, LotLogVO> {

	public static LotLogWrapper build() {
		return new LotLogWrapper();
	}

	@Override
	public LotLogVO entityVO(LotLog lotLog) {
		LotLogVO lotLogVO = BeanUtil.copy(lotLog, LotLogVO.class);
		Warehouse warehouse = WarehouseCache.getById(lotLog.getWhId());
		if (ObjectUtil.isNotEmpty(warehouse)) {
			lotLogVO.setWhName(warehouse.getWhName());
		}
		lotLogVO.setProTypeDesc(DictCache.getValue(LotLog.proTypeDict, lotLog.getProType()));
		if (ObjectUtil.isNotEmpty(lotLog.getCreateUser())) {
			User user = UserCache.getById(lotLog.getCreateUser());
			if (ObjectUtil.isNotEmpty(user)) {
				lotLogVO.setUpdateUserName(user.getName());
			}
		}

		return lotLogVO;
	}
}
