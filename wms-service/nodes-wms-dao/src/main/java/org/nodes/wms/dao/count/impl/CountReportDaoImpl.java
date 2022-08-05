package org.nodes.wms.dao.count.impl;

import org.nodes.wms.dao.count.CountReportDao;
import org.nodes.wms.dao.count.entity.CountReport;
import org.nodes.wms.dao.count.mapper.CountReportMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * 盘点差异报告表 服务实现类
 */
@Repository
public class CountReportDaoImpl
	extends BaseServiceImpl<CountReportMapper, CountReport>
	implements CountReportDao {


}
