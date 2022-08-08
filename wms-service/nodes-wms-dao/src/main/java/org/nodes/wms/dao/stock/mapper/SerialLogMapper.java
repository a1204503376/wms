package org.nodes.wms.dao.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.stock.dto.input.SerialLogPageQuery;
import org.nodes.wms.dao.stock.dto.output.SerialLogExcelResponse;
import org.nodes.wms.dao.stock.dto.output.SerialLogPageResponse;
import org.nodes.wms.dao.stock.entities.SerialLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 序列号日志
 */
@Repository("serialLogRepository")
public interface SerialLogMapper extends BaseMapper<SerialLog> {

	/**
	 * 序列号日志：分页查询
	 *
	 * @param page               分页参数
	 * @param serialLogPageQuery 条件参数
	 * @return 序列号日志分页信息
	 */
	Page<SerialLogPageResponse> page(IPage<Object> page, @Param("param") SerialLogPageQuery serialLogPageQuery);

	/**
	 * 根据Query中的条件查询序列号日志信息
	 *
	 * @param serialLogPageQuery 查询参数
	 * @return 序列号日志信息
	 */
	List<SerialLogExcelResponse> listByQuery(@Param("param") SerialLogPageQuery serialLogPageQuery);
}
