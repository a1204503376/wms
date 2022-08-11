package org.nodes.wms.dao.count;


import org.nodes.wms.dao.count.entity.CountReport;
import org.springblade.core.mp.base.BaseService;

/**
 * 盘点差异报告 DAO接口
 *
 * @author nodes
 */
public interface CountReportDao extends BaseService<CountReport> {
	/**
	 * 生成盘点差异报告
	 *
	 * @param countReport 必填
	 * @return 是否成功
	 */
	void insert(CountReport countReport);
}
