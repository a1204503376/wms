package org.nodes.wms.dao.common.log;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.common.log.dto.input.LogActionPageQuery;
import org.nodes.wms.dao.common.log.dto.input.LogPageQuery;
import org.nodes.wms.dao.common.log.dto.output.LogActionExcelResponse;
import org.nodes.wms.dao.common.log.dto.output.LogActionPageResponse;
import org.nodes.wms.dao.common.log.dto.output.LogReceiveResponse;
import org.nodes.wms.dao.common.log.dto.output.LogTaskResponse;
import org.nodes.wms.dao.common.log.entities.LogAction;

import java.util.List;

/**
 * 审计日志Dao
 *
 * @author 王智勇
 */
public interface LogActionDao {
	/**
	 * 添加审计日志添加对象
	 *
	 * @param logAction 日志审批对象
	 * @return 是否成功
	 */
	Boolean insertLogAction(LogAction logAction);

	/**
	 * 根据billId获取日志集合
	 *
	 * @param billId
	 * @return
	 */
	List<LogAction> findLogByBillId(Long billId);

	/**
	 * 根据billId获取日志分页列表
	 *
	 * @param logPageQuery
	 * @param page         日子分页参数
	 * @return
	 */
	Page<LogTaskResponse> getPage(LogPageQuery logPageQuery, IPage<LogAction> page);

	/**
	 * 业务日志分页查询
	 *
	 * @param logActionPageQuery 业务日志查询条件
	 * @param page               分页参数
	 * @return 业务日志响应对象
	 */
	Page<LogActionPageResponse> getLists(LogActionPageQuery logActionPageQuery, IPage<LogAction> page);

	/**
	 * 获取业务日志集合
	 *
	 * @param logActionPageQuery 导出条件
	 * @return 业务日志集合
	 */
	List<LogActionExcelResponse> getActionLists(LogActionPageQuery logActionPageQuery);

	/**
	 * 根据收货单id获取日志列表
	 *
	 * @param receiveId 收货单id
	 */
	List<LogReceiveResponse> findLogByReceiveId(Long receiveId);

	/**
	 * 根据单据id分页查询单据日志
	 *
	 * @param billId: 单据id
	 * @param page:   分页参数
	 * @return Page<LogAction> 单据日志分页信息
	 */
	IPage<LogAction> pageLogByBillId(IPage<LogAction> page, Long billId);
}
