package org.nodes.wms.dao.count.impl;

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



}
