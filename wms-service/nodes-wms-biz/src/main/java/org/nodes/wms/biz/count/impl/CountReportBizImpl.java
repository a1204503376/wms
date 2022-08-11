package org.nodes.wms.biz.count.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.count.CountReportBiz;
import org.nodes.wms.dao.count.CountReportDao;
import org.nodes.wms.dao.count.entity.CountReport;
import org.springframework.stereotype.Service;

/**
 * 盘点差异报告  业务
 */
@Service
@RequiredArgsConstructor
public class CountReportBizImpl implements CountReportBiz {
	private final CountReportDao countReportDao;

	@Override
	public void insertCountReport(CountReport countReport) {
		 countReportDao.insert(countReport);
	}
}
