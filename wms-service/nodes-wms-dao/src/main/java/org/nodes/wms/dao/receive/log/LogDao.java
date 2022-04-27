package org.nodes.wms.dao.receive.log;

import org.nodes.wms.dao.receive.log.dto.output.LogResponse;

import java.util.List;

/**
 * 清点记录 DAO 接口
 */
public interface LogDao {
	List<LogResponse> selectByDetailIds(List<Long> detailIds);
}
