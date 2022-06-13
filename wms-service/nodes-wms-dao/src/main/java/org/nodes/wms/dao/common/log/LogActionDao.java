package org.nodes.wms.dao.common.log;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.common.log.dto.LogPageQuery;
import org.nodes.wms.dao.common.log.dto.LogResponse;
import org.nodes.wms.dao.common.log.entities.LogAction;

import java.util.List;

/**
 * 审计日志Dao
 * @author 王智勇
 */
public interface LogActionDao {
	/**
	 * 添加审计日志添加对象
	 * @param logAction 日志审批对象
	 * @return 是否成功
	 */
	Boolean insertLogAction(LogAction logAction);

	/**
	 * 根据billId获取日志集合
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
	Page<LogResponse> getPage(LogPageQuery logPageQuery, IPage<LogAction> page);
}
