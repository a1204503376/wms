package org.nodes.wms.biz.common.log;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.common.log.dto.*;
import org.nodes.wms.dao.common.log.entities.LogAction;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 日志业务层端口
 * @author 王智勇
 */
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
	 * @param type 日志类型
	 * @param billId 单据id
	 * @param log 日志
	 */
	void auditLog(AuditLogType type, Long billId, String log);

	/**
	 * 审计日志
	 * @param auditLogRequest 参数
	 */
	void auditLog(AuditLogRequest auditLogRequest);

	/**
	 * 根据BillId获取日志实体
	 * @param id
	 * @return
	 */
	List<LogAction> getLogByBillId(Long id);

	/**
	 * 系统定时任务添加日志
	 * @param UserName 用户名
	 * @param cronTask 日志类型
	 * @param id       任务id
	 * @param log      日志内容
	 */
	void auditLog(String UserName, AuditLogType cronTask, Long id, String log);

	/**
	 * 根据BillId获取日志分页列表
	 *
	 * @param logPageQuery 日志分页查询参数
	 * @param query        分页参数
	 */
	Page<LogResponse> getPage(LogPageQuery logPageQuery, Query query);

	/**
	 * 获取消息总条数
	 */
	Integer getLogMsgCount();

	/**
	 * 根据消息已读状态获取消息列表
	 * @param num  消息是否已读
	 */
	List<LogMessageResponse> getLogMsgList(Long num);

	/**
	 * 修改已读状态
	 */
	void editLogMsgReaded(Long num,Long id);

	/**
	 * 业务日志分页查询
	 * @param logActionPageQuery 业务日志查询条件
	 * @param query 分页参数
	 * @return 业务日志响应对象
	 */
	Page<LogActionPageResponse> getLists(LogActionPageQuery logActionPageQuery,Query query);

	/**
	 * @param logActionPageQuery 请求参数
	 * @param response 返回数据
	 */
	void exportActionLists(LogActionPageQuery logActionPageQuery, HttpServletResponse response);
}
