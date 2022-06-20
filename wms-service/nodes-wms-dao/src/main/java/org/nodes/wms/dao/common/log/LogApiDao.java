package org.nodes.wms.dao.common.log;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.dao.common.log.dto.input.LogApiPageQuery;
import org.nodes.wms.dao.common.log.dto.output.LogApiPageResponse;

import java.util.List;

/**
 * 异常日志dao接口
 */
public interface LogApiDao {
	/**
	 * 获取异常日志分页列表
	 * @param logApiPageQuery: 分页查询条件dto
	 * @param page: 分页参数
	 * @return IPage<LogApiPageResponse>
	 */
	IPage<LogApiPageResponse> selectPage(LogApiPageQuery logApiPageQuery, IPage<LogApiPageResponse> page);

	/**
	 * 获取异常日志导出列表
	 * @param logApiPageQuery: 分页查询条件dto
	 * @return IPage<LogApiPageResponse>
	 */
    List<LogApiPageResponse> getLogApiResponseByQuery(LogApiPageQuery logApiPageQuery);
}
