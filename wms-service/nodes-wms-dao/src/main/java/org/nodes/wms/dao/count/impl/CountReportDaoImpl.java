package org.nodes.wms.dao.count.impl;

import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.count.CountReportDao;
import org.nodes.wms.dao.count.entity.CountReport;
import org.nodes.wms.dao.count.mapper.CountReportMapper;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * 盘点差异报告表 服务实现类
 */
@Repository
public class CountReportDaoImpl
	extends BaseServiceImpl<CountReportMapper, CountReport>
	implements CountReportDao {


	@Override
	public void insert(CountReport countReport) {
		AssertUtil.notNull(countReport,"生成盘点差异报告时失败,盘点差异报告对象为空");
		if(!super.save(countReport)){
			throw new ServiceException("生成盘点差异报告时失败");
		}
	}
}
