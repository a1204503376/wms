package org.nodes.wms.dao.common.log;

import org.nodes.wms.dao.common.log.entities.LogAction;
import org.nodes.wms.dao.common.log.entities.LogMessage;

import java.util.Collection;

/**
 * 通知日志dao层
 * @author 王智勇
 */
public interface LogMessageDao {
	/**
	 * 添加LogMessage日志
	 * @param messageCollection LogMessage
	 * @return 是否成功
	 */
    Boolean insertLogMessage(Collection<LogMessage> messageCollection);
}
