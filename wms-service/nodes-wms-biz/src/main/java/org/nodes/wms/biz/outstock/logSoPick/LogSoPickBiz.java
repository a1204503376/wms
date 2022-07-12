package org.nodes.wms.biz.outstock.logSoPick;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickIndexResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickForSoDetailResponse;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillIdRequest;
import org.springblade.core.mp.support.Query;

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

	/**
	 * 出库单查看明细：根据出库单id分页查询获取拣货记录日志信息
	 *
	 * @param soBillIdRequest: 出库单id请求对象
	 * @param query 分页参数
	 * @return Page<LogSoPickForSoDetailResponse> 出库单查看明细 拣货记录日志信息分页响应对象
	 */
	Page<LogSoPickForSoDetailResponse> pageLogSoPickForSoDetailBySoBillId(Query query, SoBillIdRequest soBillIdRequest);
}
