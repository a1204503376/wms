package org.nodes.wms.biz.stock;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.stock.dto.input.SerialLogPageQuery;
import org.nodes.wms.dao.stock.dto.input.SerialPageQuery;
import org.nodes.wms.dao.stock.dto.output.SerialLogPageResponse;
import org.nodes.wms.dao.stock.dto.output.SerialPageResponse;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 序列号管理BIZ
 */
public interface SerialBiz {

	/**
	 * 根据stockId获取在库的序列号列表
	 *
	 * @param stockId 库存id
	 * @return 序列号集合
	 */
	List<String> findSerialNoByStockId(Long stockId);

	/**
	 * 序列号日志：分页查询
	 *
	 * @param query              分页参数
	 * @param serialLogPageQuery 条件参数
	 * @return 序列号日志分页信息
	 */
	Page<SerialLogPageResponse> pageLog(Query query, SerialLogPageQuery serialLogPageQuery);

	/**
	 * 序列号日志：导出
	 *
	 * @param serialLogPageQuery 导出时条件
	 * @param response           响应对象
	 */
	void exportLog(SerialLogPageQuery serialLogPageQuery, HttpServletResponse response);

	/**
	 * 序列号：分页查询
	 *
	 * @param serialPageQuery 条件参数
	 * @param query           分页参数
	 * @return 序列号分页对象
	 */
	Page<SerialPageResponse> page(SerialPageQuery serialPageQuery, Query query);

	/**
	 * 序列号：导出
	 *
	 * @param serialPageQuery 条件参数
	 * @param response 响应对象
	 */
	void export(SerialPageQuery serialPageQuery, HttpServletResponse response);
}
