package org.nodes.wms.dao.basics.customer.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 客户表 实体类
 **/
@Data
@TableName("basics_customers")
public class BasicsCustomer extends TenantEntity {
	private static final long serialVersionUID = 4500435308488858917L;
	/**
	 * 客户表ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 客户编码
	 */
	private String code;
	/**
	 * 客户名称
	 */
	private String name;
	/**
	 * 客户简称
	 */
	private String  simpleName;
	/**
	 * 货主ID
	 */
	private Long  woId;
	/**
	 * 国家
	 */
	private String  country;
	/**
	 * 省
	 */
	private String  province;
	/**
	 * 城市
	 */
	private String  city;
	/**
	 * 街道
	 */
	private String  address;
	/**
	 * 邮编
	 */
	private String  zipCode;
	/**
	 * 备注
	 */
	private String  remark;
	/**
	 * 租户ID
	 */
	private String  tenantId;

}
