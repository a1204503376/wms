package org.nodes.wms.dao.outstock.logSoPick;

import org.nodes.wms.dao.outstock.logSoPick.dot.output.LogSoPickIndexResponse;

import java.util.List;

/**
 * 拣货记录日志Dao接口
 **/
public interface LogSoPickDao {

	/**
	 * 获取7天内出库量前10的物品
	 *
	 * @return List<LogSoPickIndexResponse>
	 */
	List<LogSoPickIndexResponse> getPickSkuQtyTop10();
}
