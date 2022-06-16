package org.nodes.wms.dao.common.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.common.log.dto.*;
import org.nodes.wms.dao.common.log.entities.LogAction;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 审计日志mapper
 * @author 王智勇
 */
@SuppressWarnings("ALL")
@Repository
@Mapper
public interface LogActionMapper extends BaseMapper<LogAction> {
    Page<LogResponse> getPage(@Param("query") LogPageQuery logPageQuery, IPage<LogAction> page);

	/**
	 * 业务日志分页查询
	 * @param logActionPageQuery 业务日志查询条件
	 * @param page 分页参数
	 * @return 业务日志响应对象
	 */
    Page<LogActionPageResponse> getLists(@Param("query") LogActionPageQuery logActionPageQuery, IPage<LogAction> page);

	/**
	 * 获取业务日志集合
	 * @param logActionPageQuery 导出条件
	 * @return 业务日志集合
	 */
	List<LogActionExcelResponse> getActionLists(@Param("query") LogActionPageQuery logActionPageQuery);
}
