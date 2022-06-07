package org.nodes.wms.biz.basics.warehouse;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.ss.formula.functions.T;
import org.nodes.wms.dao.basics.warehouse.dto.input.LocationExcelRequest;
import org.nodes.wms.dao.basics.warehouse.dto.input.LocationPageQuery;
import org.nodes.wms.dao.basics.warehouse.dto.input.LocationSelectQuery;
import org.nodes.wms.dao.basics.warehouse.dto.output.LocationPageResponse;
import org.nodes.wms.dao.basics.warehouse.dto.output.LocationSelectResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 客户管理业务层接口
 */
public interface LocationBiz {
	/**
	 * 获取库位下拉列表最近十条数据
	 * @param locationSelectQuery 前端传传入查询条件
	 * @return 库位下拉列表集合
	 */
	List<LocationSelectResponse> getLocationSelectResponseTop10List(LocationSelectQuery locationSelectQuery);

	/**
	 * 导入
	 *
	 * @param locationDataList: 导入数据集合
	 * @return true: 导入成功，false：导入失败
	 */
	boolean importData(List<LocationExcelRequest> locationDataList);

	/**
	 * 分页
	 *
	 * @param page               分页参数
	 * @param locationPageQuery: 分页查询条件
	 * @return IPage<LocationPageResponse>
	 */
	Page<LocationPageResponse> page(IPage<T> page, LocationPageQuery locationPageQuery);

	/**
	 * 导出
	 *
	 * @param locationPageQuery: 条件
	 * @param response: 响应信息
	 */
	void exportExcel(LocationPageQuery locationPageQuery, HttpServletResponse response);
}
