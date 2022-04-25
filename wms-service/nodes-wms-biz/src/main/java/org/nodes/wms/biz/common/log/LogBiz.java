package org.nodes.wms.biz.common.log;

import org.nodes.wms.dao.common.log.dto.AuditLogRequest;
import org.nodes.wms.dao.common.log.dto.NoticeMessageRequest;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;

public interface LogBiz {

	/**
	 * 发布通知类型的消息
	 * @param noticeMessageRequest 参数
	 */
	void noticeMesssage(NoticeMessageRequest noticeMessageRequest);

	/**
	 * 添加审计日志
	 * @param type 日志类型
	 * @param log 日志
	 */
	void auditLog(AuditLogType type, String log);

	/**
	 * 审计日志
	 * @param type 日志类型
	 * @param billId 单据id
	 * @param billNo 单据编码
	 * @param log 日志
	 */
	void auditLog(AuditLogType type, Long billId, String billNo, String log);

	/**
	 * 审计日志
	 * @param auditLogRequest 参数
	 */
	void auditLog(AuditLogRequest auditLogRequest);
}
