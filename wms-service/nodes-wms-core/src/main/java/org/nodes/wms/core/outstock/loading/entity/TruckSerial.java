/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.nodes.wms.core.outstock.loading.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 装车发货序列号日志实体类
 *
 * @author pengwei
 * @since 2020-04-10
 */
@Data
@TableName("log_truck_serial")
@ApiModel(value = "TruckSerial对象", description = "装车发货序列号日志")
public class TruckSerial extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 发货序列号日志ID
	 */
	@ApiModelProperty(value = "发货序列号日志ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "ltsn_id", type = IdType.ASSIGN_ID)
	private Long ltsnId;
	/**
	 * 车次ID
	 */
	@ApiModelProperty(value = "车次ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long truckId;
	/**
	 * 序列号ID
	 */
	@ApiModelProperty(value = "序列号ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long serialId;
	/**
	 * 库存ID（只有序列号状态为空闲时，库存ID才为Null
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "库存ID（只有序列号状态为空闲时，库存ID才为Null")
	private Long stockId;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 序列号
	 */
	@ApiModelProperty(value = "序列号")
	private String serialNumber;
	/**
	 * 序列号状态
	 */
	@ApiModelProperty(value = "序列号状态")
	private Integer serialState;
	/**
	 * 物品ID
	 */
	@ApiModelProperty("物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;
	/**
	 * 物品编码
	 */
	@ApiModelProperty("物品编码")
	private String skuCode;
	/**
	 * 物品名称
	 */
	@ApiModelProperty("物品名称")
	private String skuName;
	/**
	 * 批次号
	 */
	@ApiModelProperty("批次号")
	private String lotNumber;
	/**
	 * 系统日志ID
	 */
	@ApiModelProperty("系统日志ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long systemProcId;
}
