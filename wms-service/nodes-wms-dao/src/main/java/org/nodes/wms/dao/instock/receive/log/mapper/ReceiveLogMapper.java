package org.nodes.wms.dao.instock.receive.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.instock.receive.log.dto.output.ReceiveLogResponse;
import org.nodes.wms.dao.instock.receive.log.entities.ReceiveLog;

import java.util.List;

public interface ReceiveLogMapper extends BaseMapper<ReceiveLog> {
	List<ReceiveLogResponse> selectByDetailIds(@Param("detailIds") List<Long> detailIds);
}
