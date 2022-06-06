package org.nodes.wms.dao.basics.billType.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 单据类型实体类
 */
@Data
@TableName("wms_bill_type")
public class BillType extends TenantEntity {

	private static final long serialVersionUID = -8609211036984116190L;

	/**
	 * 单据类型ID
	 */
	@TableId(value = "bill_type_id", type = IdType.ASSIGN_ID)
	private Long billTypeId;

	/**
	 * 单据类型编码
	 */
	private String billTypeCd;

	/**
	 * 单据类型名称
	 */
	private String billTypeName;

	/**
	 * 类型模式
	 */
	private String ioType;
}
