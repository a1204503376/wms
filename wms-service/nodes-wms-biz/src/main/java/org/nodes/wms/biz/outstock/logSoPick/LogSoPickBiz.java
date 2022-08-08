package org.nodes.wms.biz.outstock.logSoPick;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.LogSoPickPageQuery;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickForSoDetailResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickIndexResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickPageResponse;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillIdRequest;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;
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
	 * 发货单查看明细：根据发货单id分页查询获取拣货记录日志信息
	 *
	 * @param soBillIdRequest: 发货单id请求对象
	 * @param query            分页参数
	 * @return Page<LogSoPickForSoDetailResponse> 发货单查看明细 拣货记录日志信息分页响应对象
	 */
	Page<LogSoPickForSoDetailResponse> pageLogSoPickForSoDetailBySoBillId(Query query, SoBillIdRequest soBillIdRequest);

	/**
	 * 分页查询
	 *
	 * @param query:              分页参数
	 * @param logSoPickPageQuery: 分页条件参数
	 * @return Page<LogSoPickPageResponse> 拣货记录发分页查询响应对象
	 */
	Page<LogSoPickPageResponse> page(Query query, LogSoPickPageQuery logSoPickPageQuery);

	/**
	 * 导出
	 *
	 * @param logSoPickPageQuery: 导出时条件参数
	 * @param response 响应对象
	 */
	void export(LogSoPickPageQuery logSoPickPageQuery, HttpServletResponse response);

	/**
	 * 根据拣货记录id获取拣货记录日志信息
	 *
	 * @param lsopIdList 拣货记录id
	 * @return List<LogSoPick> 拣货记录信息
	 */
	List<LogSoPick> findByIds(List<Long> lsopIdList);

}
