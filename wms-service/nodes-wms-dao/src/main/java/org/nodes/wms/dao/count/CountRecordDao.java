package org.nodes.wms.dao.count;


import org.nodes.wms.dao.count.entity.CountRecord;
import org.springblade.core.mp.base.BaseService;

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

}
