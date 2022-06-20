package org.nodes.wms.dao.common.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.common.log.dto.input.LogErrorPageQuery;
import org.nodes.wms.dao.common.log.dto.output.LogErrorPageResponse;
import org.nodes.wms.dao.common.log.entities.LogError;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 异常日志Mapper接口
 */
@Repository("LogErrorRepository")
public interface LogErrorMapper extends BaseMapper<LogError> {
	IPage<LogErrorPageResponse> getPage(@Param("query")  LogErrorPageQuery logErrorPageQuery, IPage<LogErrorPageResponse> page);

	List<LogErrorPageResponse> getLogErrorResponseByQuery(@Param("query")  LogErrorPageQuery logErrorPageQuery);
}
