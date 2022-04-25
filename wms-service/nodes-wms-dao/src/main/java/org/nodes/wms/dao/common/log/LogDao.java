package org.nodes.wms.dao.common.log;

import org.nodes.wms.dao.common.log.entities.LogAction;
import org.nodes.wms.dao.common.log.entities.LogMessage;

import java.util.Collection;

/**
 * 日志dao层
 * @author 王智勇
 */
public interface LogDao {
	/**
	 * 添加LogMessage日志
	 * @param messageCollection LogMessage
	 * @return 是否成功
	 */
    Boolean addLogMessage(Collection<LogMessage> messageCollection);

	/**
	 * 添加审计日志添加对象
	 * @param logAction 日志审批对象
	 * @return 是否成功
	 */
	Boolean addLogAction(LogAction logAction);
}
