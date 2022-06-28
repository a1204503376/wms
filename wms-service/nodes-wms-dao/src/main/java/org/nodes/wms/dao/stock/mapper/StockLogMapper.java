package org.nodes.wms.dao.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.stock.dto.input.StockLogPageQuery;
import org.nodes.wms.dao.stock.dto.output.StockLogExcelResponse;
import org.nodes.wms.dao.stock.dto.output.StockLogPageResponse;
import org.nodes.wms.dao.stock.entities.StockLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 库存日志mapper接口
 **/
@Repository("stockLogRepository")
public interface StockLogMapper extends BaseMapper<StockLog> {
	/**
	 * 库存日志分页查询
	 *
	 * @param page: 分页参数
	 * @param stockLogPageQuery: 查询条件dto对象
	 * @return Page<StockLogPageResponse>
	 */
	Page<StockLogPageResponse> page(IPage<?> page, @Param("param") StockLogPageQuery stockLogPageQuery);

	/**
	 * 根据Query若干条件查询库存日志信息
	 *
	 * @param stockLogPageQuery: 查询条件dto对象
	 * @return List<StockLogExcelResponse>
	 */
	List<StockLogExcelResponse> listByQuery(@Param("param") StockLogPageQuery stockLogPageQuery);
}
