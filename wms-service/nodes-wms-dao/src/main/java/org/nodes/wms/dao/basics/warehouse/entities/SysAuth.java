package org.nodes.wms.dao.basics.warehouse.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 仓库授权实体
 **/
@Data
@TableName("sys_auth")
public class SysAuth extends TenantEntity {

	private static final long serialVersionUID = -429949611823615426L;

	/**
	 * 主键id
	 */
	@TableId(value = "id",type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 备注
	 */
	private String remark;
}
