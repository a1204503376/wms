package org.nodes.wms.biz.outstock.logSoPick;

import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickIndexResponse;

import java.util.List;

/**
 * 拣货记录日志业务接口
 **/
public interface LogSoPickBiz {

	/**
	 * 获取7天内出库量前10的物品
	 *
	 * @return List<LogSoPickIndexResponse>
	 */
	List<LogSoPickIndexResponse> findPickSkuQtyTop10();
}
