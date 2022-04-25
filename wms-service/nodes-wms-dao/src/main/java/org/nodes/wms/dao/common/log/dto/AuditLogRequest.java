package org.nodes.wms.dao.common.log.dto;

import lombok.Data;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.springblade.core.secure.BladeUser;

/**
 * 审计日期
 */
@Data
public class AuditLogRequest {
	/**
	 * 日志类型
	 */
	private AuditLogType type;

	/**
	 * 日志
	 */
	private String log;

	/**
	 * 单据id，可能为空
	 */
	private Long billId;

	/**
	 * 单据编码，可能为空
	 */
	private String billNo;

	/**
	 * 操作用户,注意：系统操作的时候可能为空
	 */
	private BladeUser actionUser;
}
