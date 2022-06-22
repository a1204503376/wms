package org.nodes.wms.dao.common.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.common.log.dto.input.LogApiPageQuery;
import org.nodes.wms.dao.common.log.dto.output.LogApiPageResponse;
import org.nodes.wms.dao.common.log.entities.LogApi;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 异常日志Mapper接口
 */
@Repository("LogApiRepository")
public interface LogApiMapper extends BaseMapper<LogApi> {
	/**
	 * 分页查询
	 *
	 * @param logApiPageQuery: 分页查询条件dto
	 * @param page:            分页参数
	 * @return IPage<LogApiPageResponse>
	 */
	IPage<LogApiPageResponse> getPage(@Param("param") LogApiPageQuery logApiPageQuery, IPage<LogApiPageResponse> page);

	/**
	 * 根据pageQuery中的条件查询请求日志
	 *
	 * @param logApiPageQuery: 分页查询条件dto
	 * @return List<LogApiPageResponse>
	 */
	List<LogApiPageResponse> getLogApiResponseByQuery(@Param("param") LogApiPageQuery logApiPageQuery);
}
