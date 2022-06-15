package org.nodes.wms.dao.basics.owner.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 货主实体类
 */
@Data
@TableName("wms_owner")
public class Owner extends TenantEntity {

	private static final long serialVersionUID = -4755521835893759438L;
	public static final int DATA_TYPE = 3;

	/**
	 * 货主ID
	 */
	@TableId(value = "wo_id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long woId;

	/**
	 * 货主编码
	 */
	private String ownerCode;

	/**
	 * 货主名称
	 */
	private String ownerName;

	/**
	 * 货主简称
	 */
	private String ownerNameS;

	/**
	 * 城市
	 */
	private String ownerCity;

	/**
	 * 省
	 */
	private String ownerProvince;

	/**
	 * 邮政编码
	 */
	private String ownerZipCode;

	/**
	 * 国家
	 */
	private String ownerCountry;

	/**
	 * 是否启用（1：启用，-1：未启用）
	 */
	private Integer status;
}
