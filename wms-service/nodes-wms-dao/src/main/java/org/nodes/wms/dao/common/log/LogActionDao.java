package org.nodes.wms.dao.common.log;

import org.nodes.wms.dao.common.log.entities.LogAction;

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
	Boolean addLogAction(LogAction logAction);
}
