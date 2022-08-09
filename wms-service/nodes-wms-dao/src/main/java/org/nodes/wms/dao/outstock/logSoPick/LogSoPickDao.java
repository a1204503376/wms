package org.nodes.wms.dao.outstock.logSoPick;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.LogSoPickPageQuery;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPicExcelResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickForSoDetailResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickIndexResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickPageResponse;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;

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

	/**
	 * 发货单查看明细：根据发货单id分页查询获取拣货记录日志信息
	 *
	 * @param soBillId: 发货单id
	 * @param page:     分页参数
	 * @return Page<LogSoPickForSoDetailResponse> 发货单查看明细 拣货记录日志信息分页响应对象
	 */
	Page<LogSoPickForSoDetailResponse> pageForSoDetailBySoBillId(IPage<?> page, Long soBillId);

	/**
	 * 分页查询
	 *
	 * @param page:               分页参数
	 * @param logSoPickPageQuery: 分页条件参数
	 * @return Page<LogSoPickPageResponse> 拣货记录发分页查询响应对象
	 */
	Page<LogSoPickPageResponse> page(IPage<?> page, LogSoPickPageQuery logSoPickPageQuery);

	/**
	 * 根据Query条件查询发货日志记录
	 *
	 * @param logSoPickPageQuery: 查询条件
	 * @return List<LogSoPicExcelResponse>
	 */
	List<LogSoPicExcelResponse> listByQuery(LogSoPickPageQuery logSoPickPageQuery);

	/**
	 * 根据拣货记录id获取拣货记录日志信息
	 *
	 * @param lsopIdList: 拣货记录id
	 * @return List<LogSoPick> 拣货记录信息
	 */
	List<LogSoPick> getByIds(List<Long> lsopIdList);

	/**
	 * 保存拣货记录
	 *
	 * @param logSoPick 拣货记录实体
	 */
	void saveLogSoPick(LogSoPick logSoPick);
}
