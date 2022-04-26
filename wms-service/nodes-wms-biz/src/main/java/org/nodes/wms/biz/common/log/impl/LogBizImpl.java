package org.nodes.wms.biz.common.log.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.common.log.impl.modular.LogFactory;
import org.nodes.wms.dao.common.log.LogActionDao;
import org.nodes.wms.dao.common.log.LogMessageDao;
import org.nodes.wms.dao.common.log.dto.AuditLogRequest;
import org.nodes.wms.dao.common.log.dto.NoticeMessageRequest;
import org.nodes.wms.dao.common.log.entities.LogAction;
import org.nodes.wms.dao.common.log.entities.LogMessage;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

/**
 * 日志实现类，实现通知日志和审计日志的存储
 * @author 王智勇
 */
@Service
@RequiredArgsConstructor
public class LogBizImpl implements LogBiz {
	private final LogFactory logFactory;
	private final LogMessageDao logMessageDao;
    private final LogActionDao actionDao;
	/**
	 * 实现通知日志智能存储
	 *
	 * @param noticeMessageRequest 参数
	 */
	@Override
	public void noticeMesssage(NoticeMessageRequest noticeMessageRequest) {
		if (Func.isNull(noticeMessageRequest.getExpirationDate())) {
			noticeMessageRequest.setExpirationDate(DateUtil.plusDays(new Date(), 7));
		}
		Collection<LogMessage> logMessage = logFactory.createLogMessage(noticeMessageRequest);
		logMessageDao.addLogMessage(logMessage);
	}

	/**
	 * 审计日志传输类型和日志详细信息存储
	 *
	 * @param type 日志类型
	 * @param log  日志
	 */
	@Override
	public void auditLog(AuditLogType type, String log) {
		LogAction logAction = logFactory.createLogAction(type, log);
		actionDao.addLogAction(logAction);
	}

	/**
	 * 审计日志 日志类型,单据id,单据编码,日志详细信息存储
	 *
	 * @param type   日志类型
	 * @param billId 单据id
	 * @param billNo 单据编码
	 * @param log    日志
	 */
	@Override
	public void auditLog(AuditLogType type, Long billId, String billNo, String log) {
		LogAction logAction = logFactory.createLogAction(type, billId, billNo, log);
		actionDao.addLogAction(logAction);
	}

	/**
	 * 审计日志 审计请求对象存储
	 *
	 * @param auditLogRequest 参数
	 */
	@Override
	public void auditLog(AuditLogRequest auditLogRequest) {
		LogAction logAction = logFactory.createLogAction(auditLogRequest);
		actionDao.addLogAction(logAction);
	}

}
