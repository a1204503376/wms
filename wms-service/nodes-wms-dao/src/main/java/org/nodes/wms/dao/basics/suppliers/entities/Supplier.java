package org.nodes.wms.dao.basics.suppliers.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 供应商实体
 *
 * @author 彭永程
 * @date 2022-04-20 10:49
 **/
@Data
@EqualsAndHashCode
@TableName("basics_suppliers")
public class Supplier extends TenantEntity {
	/**
	 * 供应商id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 供应商编码
	 */
	private String code;

	/**
	 * 供应商名称
	 */
	private String name;

	/**
	 * 供应商简称
	 */
	private String simpleName;

	/**
	 * 货主id
	 */
	private Long woId;

	/**
	 * 备注
	 */
	private String remark;
}
