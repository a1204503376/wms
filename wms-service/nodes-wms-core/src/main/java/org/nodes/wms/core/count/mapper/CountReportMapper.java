
package org.nodes.wms.core.count.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.count.entity.CountReport;
import org.nodes.wms.core.count.vo.CountReportVO;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 盘点差异报告表 Mapper 接口
 *
 * @author chz
 * @since 2020-02-20
 */
@Primary
public interface CountReportMapper extends BaseMapper<CountReport> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param countReport
	 * @return
	 */
	List<CountReportVO> selectCountReportPage(IPage page, CountReportVO countReport);

	List<CountReport> selectCountReportGroup(String countBillNo);
}
