package org.nodes.wms.biz.common.log.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.common.log.impl.modular.LogFactory;
import org.nodes.wms.dao.common.log.LogActionDao;
import org.nodes.wms.dao.common.log.LogMessageDao;
import org.nodes.wms.dao.common.log.dto.*;
import org.nodes.wms.dao.common.log.entities.LogAction;
import org.nodes.wms.dao.common.log.entities.LogMessage;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
		logMessageDao.insertLogMessage(logMessage);
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
		actionDao.insertLogAction(logAction);
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
		actionDao.insertLogAction(logAction);
	}

	@Override
	public void auditLog(AuditLogType type, Long billId, String log) {
		LogAction logAction = logFactory.createLogAction(type, billId, log);
		actionDao.insertLogAction(logAction);
	}

	/**
	 * 审计日志 审计请求对象存储
	 *
	 * @param auditLogRequest 参数
	 */
	@Override
	public void auditLog(AuditLogRequest auditLogRequest) {
		LogAction logAction = logFactory.createLogAction(auditLogRequest);
		actionDao.insertLogAction(logAction);
	}

	@Override
	public List<LogAction> getLogByBillId(Long billId) {
		return actionDao.findLogByBillId(billId);
	}

	@Override
	public void auditLog(String userName, AuditLogType cronTask, Long id, String log) {
		LogAction logAction = logFactory.createLogAction(userName,cronTask,id,log);
		actionDao.insertLogAction(logAction);
	}

	@Override
	public Page<LogResponse> getPage(LogPageQuery logPageQuery, Query query) {
		IPage<LogAction> page = Condition.getPage(query);
		return actionDao.getPage(logPageQuery,page);
	}

    @Override
    public Integer getLogMsgCount() {
        return logMessageDao.getLogMsgCount();
    }

	@Override
	public List<LogMessageResponse> getLogMsgList(Long num) {
			return logMessageDao.getLogMsgList(num, DateUtil.formatDate(new Date()));
	}


	@Override
	public void editLogMsgReaded(Long num, Long id) {
		logMessageDao.updateLogMsgReaded(num,id);
	}

	/**
	 * 业务日志分页查询
	 * @param logActionPageQuery 业务日志查询条件
	 * @param query 分页参数
	 * @return 业务日志响应对象
	 */
	@Override
	public Page<LogActionPageResponse> getLists(LogActionPageQuery logActionPageQuery, Query query) {
		IPage<LogAction> page = Condition.getPage(query);
		return actionDao.getLists(logActionPageQuery,page);
	}

	@Override
	public void exportActionLists(LogActionPageQuery logActionPageQuery, HttpServletResponse response) {
		List<LogActionExcelResponse> actionLists = actionDao.getActionLists(logActionPageQuery);
		ExcelUtil.export(response, "业务日志", "业务日志数据表",actionLists, LogActionExcelResponse.class);
	}


}
