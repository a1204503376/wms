package org.nodes.wms.dao.count.impl;

import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.count.CountRecordDao;
import org.nodes.wms.dao.count.entity.CountRecord;
import org.nodes.wms.dao.count.mapper.CountRecordMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * 盘点单记录
 */
@Repository
public class CountRecordDaoImpl
	extends BaseServiceImpl<CountRecordMapper, CountRecord>
	implements CountRecordDao {


	@Override
	public void insert(CountRecord countRecord) {
		AssertUtil.notNull(countRecord, "新增盘点单记录失败，盘点单为空");
		if (!super.save(countRecord)) {
			AssertUtil.notNull(countRecord, "新增盘点单记录失败，保存数据时失败");
		}
	}
}
