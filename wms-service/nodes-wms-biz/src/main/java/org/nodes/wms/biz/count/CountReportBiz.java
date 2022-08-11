package org.nodes.wms.biz.count;

import org.nodes.wms.dao.count.entity.CountReport;

/**
 * 盘点差异报告 Biz接口
 *
 * @author nodes
 */
public interface CountReportBiz {
	/**
	 * 生成盘点差异报告
	 *
	 * @param countReport 必填
	 * @return 是否成功
	 */
	void insertCountReport(CountReport countReport);
}
