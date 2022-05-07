package org.nodes.wms.dao.instock.receive.log;

import org.nodes.wms.dao.instock.receive.log.dto.output.ReceiveLogResponse;

import java.util.List;

/**
 * 清点记录 DAO 接口
 */
public interface ReceiveLogDao {
	List<ReceiveLogResponse> selectByDetailIds(List<Long> detailIds);
}
