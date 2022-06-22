package org.nodes.wms.dao.common.log.dto.output;

import lombok.Data;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;

import java.util.Date;

/**
 * 收货单操作日志返回前端视图类
 */
@Data
public class LogReceiveResponse {
	/**
	 * 操作人员账号
	 */
	private String userAccount;

	/**
	 * 操作人员真实名称
	 */
	private String userRealName;

	/**
	 * 操作类型
	 */
	private AuditLogType type;

	/**
	 * 操作内容
	 */
	private String log;
	/**
	 * 创建时间
	 */
	private Date createTime;
}
