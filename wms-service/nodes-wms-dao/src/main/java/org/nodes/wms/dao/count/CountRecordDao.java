package org.nodes.wms.dao.count;


import org.nodes.wms.dao.count.entity.CountRecord;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 盘点单记录 DAO接口
 */
public interface CountRecordDao extends BaseService<CountRecord> {
	/**
	 * 添加盘点单记录
	 *
	 * @param countRecord notNull
	 */
	void insert(CountRecord countRecord);

	/**
	 * 根据盘点单号、库位获取盘点单集合
	 *
	 * @param countBillId countBillId
	 * @param locId       locId
	 * @return 盘点单集合
	 */
	List<CountRecord> getCountRecordListByCountBillId(Long countBillId, Long locId);

}
