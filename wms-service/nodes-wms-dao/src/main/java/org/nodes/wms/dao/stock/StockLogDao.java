package org.nodes.wms.dao.stock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.stock.dto.input.StockLogPageQuery;
import org.nodes.wms.dao.stock.dto.output.StockLogExcelResponse;
import org.nodes.wms.dao.stock.dto.output.StockLogPageResponse;
import org.nodes.wms.dao.stock.entities.StockLog;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 库存日志Dao接口
 **/
public interface StockLogDao extends BaseService<StockLog> {

	/**
	 * 库存日志分页查询
	 *
	 * @param page: 分页参数
	 * @param stockLogPageQuery: 查询条件dto对象
	 * @return Page<StockLogPageResponse>
	 */
	Page<StockLogPageResponse> page(IPage<?> page, StockLogPageQuery stockLogPageQuery);

	/**
	 * 根据查询条件获取库存日志信息
	 *
	 * @param stockLogPageQuery:  查询条件dto对象
	 * @return List<StockLogExcelResponse>
	 */
	List<StockLogExcelResponse> listByQuery(StockLogPageQuery stockLogPageQuery);
}
