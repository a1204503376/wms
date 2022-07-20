package org.nodes.wms.dao.common.log.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;

import java.io.Serializable;
import java.util.Date;

/**
 * 查看明细-单据审计日志响应类
 **/
@Data
public class LogDetailPageResponse implements Serializable {

	private static final long serialVersionUID = -6163394077716474792L;

	/**
	 * 审计日志id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

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
