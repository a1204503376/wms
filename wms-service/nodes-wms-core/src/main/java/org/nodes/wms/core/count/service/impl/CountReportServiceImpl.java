
package org.nodes.wms.core.count.service.impl;

import org.nodes.wms.core.count.entity.CountReport;
import org.nodes.wms.core.count.excel.CountReportExcel;
import org.nodes.wms.core.count.mapper.CountReportMapper;
import org.nodes.wms.core.count.service.ICountReportService;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 盘点差异报告表 服务实现类
 *
 * @author NodeX
 * @since 2020-02-20
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class CountReportServiceImpl<M extends CountReportMapper, T extends CountReport>
	extends BaseServiceImpl<CountReportMapper, CountReport>
	implements ICountReportService {

	/**
	 * 根据盘点单ID查询报告
	 *
	 * @param id 盘点单ID
	 * @return 报告集合
	 */
	@Override
	public List<CountReport> listByBillId(Long id) {
		CountReport countReport = new CountReport();
		countReport.setCountBillId(id);

		return super.list(Condition.getQueryWrapper(countReport));
	}

	@Override
	public List<CountReport> selectCountReportGroup(String countBillNo) {
		return baseMapper.selectCountReportGroup(countBillNo);
	}

	@Override
	public void exportCountReportsExcel(String id, HttpServletResponse response) {
		CountReport countReport = new CountReport();
		countReport.setCountBillId(Func.toLong(id));
		List<CountReport> reportList = baseMapper.selectList(Condition.getQueryWrapper(countReport));
		//待导出的差异报告集合
		List<CountReportExcel> countReportExcelList = new ArrayList<>();

		if (Func.isNotEmpty(reportList)) {
			for (CountReport report:reportList){
				CountReportExcel countReportExcel = BeanUtil.copy(report, CountReportExcel.class);
				countReportExcel.setDifferentNum(report.getCountQty().subtract(report.getWmsQty()));
				countReportExcelList.add(countReportExcel);
			}
		}
		ExcelUtil.export(response, "差异报告", "差异报告", countReportExcelList, CountReportExcel.class);
	}
}
