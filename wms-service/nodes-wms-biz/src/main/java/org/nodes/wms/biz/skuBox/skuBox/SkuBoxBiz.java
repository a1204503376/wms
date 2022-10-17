package org.nodes.wms.biz.skuBox.skuBox;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.tianyi.skubox.dto.input.SkuBoxPageQuery;
import org.nodes.wms.dao.tianyi.skubox.dto.output.SkuBoxPageResponse;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;

/**
 * 天宜物品和型号对应箱型标识 业务接口
 */
public interface SkuBoxBiz {
	/**
	 * 分页查询
	 *
	 * @param query           分页参数
	 * @param skuBoxPageQuery 查询条件
	 * @return 分页数据
	 */
	Page<SkuBoxPageResponse> getPage(Query query, SkuBoxPageQuery skuBoxPageQuery);

	/**
	 * 根据条件导出对应的数据
	 *
	 * @param skuBoxPageQuery 导出条件
	 * @param response        响应对象
	 */
	void exportExcel(SkuBoxPageQuery skuBoxPageQuery, HttpServletResponse response);
}
