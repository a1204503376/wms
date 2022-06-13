package org.nodes.wms.dao.common.log.dto;

import lombok.Data;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;

import java.io.Serializable;
import java.util.Date;

/**
 * 日志分页列表返货前端视图图
 */
@Data
public class LogResponse implements Serializable {

	private static final long serialVersionUID = 8658238171114149673L;
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
	 * 目标单据id,可能为空
	 */
	private Long billId;

	/**
	 * 目标单据编码,可能为空
	 */
	private String billNo;

	/**
	 * 操作内容
	 */
	private String log;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 任务名称
	 */
	private String crontabTaskName;
}
