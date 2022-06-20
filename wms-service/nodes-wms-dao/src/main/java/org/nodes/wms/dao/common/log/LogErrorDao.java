package org.nodes.wms.dao.common.log;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.dao.common.log.dto.input.LogErrorPageQuery;
import org.nodes.wms.dao.common.log.dto.output.LogErrorPageResponse;

import java.util.List;

/**
 * 异常日志dao接口
 */
public interface LogErrorDao {
	/**
	 * 获取异常日志分页列表
	 * @param logErrorPageQuery
	 * @param page
	 * @return
	 */
	IPage<LogErrorPageResponse> selectPage(LogErrorPageQuery logErrorPageQuery, IPage<LogErrorPageResponse> page);

	/**
	 * 获取异常日志导出列表
	 * @param logErrorPageQuery
	 * @return
	 */
    List<LogErrorPageResponse> getLogErrorResponseByQuery(LogErrorPageQuery logErrorPageQuery);
}
