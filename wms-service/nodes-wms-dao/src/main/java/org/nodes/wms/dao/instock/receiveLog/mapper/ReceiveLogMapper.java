package org.nodes.wms.dao.instock.receiveLog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogIndexResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogResponse;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;

import java.util.List;

public interface ReceiveLogMapper extends BaseMapper<ReceiveLog> {
	List<ReceiveLogResponse> selectReceiveLogList(Long receiveId);

	/**
	 * 获取7天内入库量前10的物品
	 *
	 * @return List<ReceiveLogIndexResponse>
	 */
	List<ReceiveLogIndexResponse> selectReceiveSkuQtyTop10();
}
