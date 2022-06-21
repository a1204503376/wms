package org.nodes.wms.dao.instock.receiveLog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogResponse;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;

import java.util.List;

public interface ReceiveLogMapper extends BaseMapper<ReceiveLog> {
	List<ReceiveLogResponse> selectReceiveLogList(Long receiveId);
}
