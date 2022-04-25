package org.nodes.wms.biz.common.log.impl.modular;

import org.nodes.wms.dao.common.log.dto.AuditLogRequest;
import org.nodes.wms.dao.common.log.dto.NoticeMessageRequest;
import org.nodes.wms.dao.common.log.entities.LogAction;
import org.nodes.wms.dao.common.log.entities.LogMessage;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
* 日志工厂
 * @author 王智勇
* */
@Service
public class LogFactory {
	/**
	 * 工厂对象返回LogMessage集合
	 * @param messageRequest 发送通知类型的通知对象
	 * @return 通知日志集合
	 */
	public Collection<LogMessage> createLogMessage(NoticeMessageRequest messageRequest)
	{
		// 创建Collection的实现
		List<LogMessage> coll = new ArrayList<>();

		//判断当前消息对象的用户集合是否存在 存在则循环遍历取出对象并返回
		if(Func.isNotEmpty(messageRequest.getNoticeUserList()))
		{
			for (BladeUser bladeUser:messageRequest.getNoticeUserList()) {
				LogMessage logMessage=new LogMessage();
				logMessage.setExpirationDate(messageRequest.getExpirationDate());
				logMessage.setLog(messageRequest.getLog());
                logMessage.setUserId(bladeUser.getUserId());
				coll.add(logMessage);
			}
           return coll;
		}
		//不存在则直接返回当前对象，并且UserId为空，示意为全部消息
		LogMessage logMessage=new LogMessage();
		logMessage.setExpirationDate(messageRequest.getExpirationDate());
		logMessage.setLog(messageRequest.getLog());
		coll.add(logMessage);
        return coll;
	}


	/**
	 * 构造审计日志对象
	 * @param type 类型
	 * @param log 内容
	 * @return 审计日志对象
	 */
	public LogAction createLogAction(AuditLogType type, String log){
		LogAction logAction=new LogAction();
		logAction.setType(type.getIndex());
        logAction.setLog(log);
		logAction.setUserAccount(AuthUtil.getUserAccount());
		logAction.setUserRealName(AuthUtil.getUserName());
		return logAction;
	}

	/**
	 * 构造审计日志对象
	 * @param type 类型
	 * @param billId 单据id
	 * @param billNo 单据编码
	 * @param log 内容
	 * @return 审计日志对象
	 */
	public LogAction  createLogAction(AuditLogType type, Long billId, String billNo, String log){
		LogAction logAction=new LogAction();
		logAction.setType(type.getIndex());
		logAction.setBillId(billId);
		logAction.setBillNo(billNo);
		logAction.setLog(log);
		logAction.setUserAccount(AuthUtil.getUserAccount());
		logAction.setUserRealName(AuthUtil.getUserName());
		return logAction;
	}

	/**
	 * 构造审计日志对象
	 * @param auditLogRequest 审计日期对象 请求对象
	 * @return 审计日志对象
	 */
	public LogAction createLogAction(AuditLogRequest auditLogRequest){
		LogAction logAction=new LogAction();
		logAction.setType(auditLogRequest.getType().getIndex());
		logAction.setLog(auditLogRequest.getLog());
		logAction.setBillId(auditLogRequest.getBillId());
		logAction.setBillNo(auditLogRequest.getBillNo());
		logAction.setUserAccount(auditLogRequest.getActionUser().getUserName());
		logAction.setUserAccount(AuthUtil.getUserAccount());
		logAction.setUserRealName(AuthUtil.getUserName());
		return logAction;
	}
}
