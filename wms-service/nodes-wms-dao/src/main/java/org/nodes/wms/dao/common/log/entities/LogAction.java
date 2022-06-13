package org.nodes.wms.dao.common.log.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 审计日志类
 *
 * @author 王智勇
 */
@Data
@TableName("log_action")
public class LogAction extends TenantEntity {

	private static final long serialVersionUID = -2029269185139790925L;
	/**
	 * 主键ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
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


}
