package org.nodes.wms.dao.basics.warehouse.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 库房 实体类
 **/
@Data
@TableName("wms_warehouse")
public class Warehouse extends TenantEntity {
	/**
	 * 仓库ID
	 */
	@TableId(value = "wh_id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 仓库编码
	 */
	private String whCode;
	/**
	 * 仓库名称
	 */
	private String whName;
	/**
	 * 城市
	 */
	private String city;
	/**
	 * 省
	 */
	private String province;
	/**
	 * 邮政编码
	 */
	private String zipCode;
	/**
	 * 国家
	 */
	private String country;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 入库暂存区
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stage;
	/**
	 * 出库暂存区
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long pick;
	/**
	 * 包装暂存区
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long pack;
	/**
	 * 移动暂存区
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long move;

	/**
	 * 所属机构
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long deptId;
}
