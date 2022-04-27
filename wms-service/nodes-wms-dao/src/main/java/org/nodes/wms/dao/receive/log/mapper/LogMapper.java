package org.nodes.wms.dao.receive.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.receive.log.dto.output.LogResponse;
import org.nodes.wms.dao.receive.log.entities.ReceiveLog;

import java.util.List;

public interface LogMapper extends BaseMapper<ReceiveLog> {
	List<LogResponse> selectByDetailIds(@Param("detailIds") List<Long> detailIds);
}
