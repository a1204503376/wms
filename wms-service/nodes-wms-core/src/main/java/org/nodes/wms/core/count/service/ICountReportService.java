
package org.nodes.wms.core.count.service;

import org.nodes.wms.core.count.entity.CountReport;
import org.springblade.core.mp.base.BaseService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 盘点差异报告表 服务类
 *
 * @author chz
 * @since 2020-02-20
 */
public interface ICountReportService extends BaseService<CountReport> {

	/**
	 * 根据盘点单ID查询报告
	 *
	 * @param id 盘点单ID
	 * @return 报告集合
	 */
	List<CountReport> listByBillId(Long id);

	List<CountReport> selectCountReportGroup(String countBillNo);

    void exportCountReportsExcel(String id, HttpServletResponse response);
}
