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
package org.nodes.wms.core.outstock.loading.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.outstock.loading.entity.TruckSerial;

/**
 * 装车发货序列号日志视图实体类
 *
 * @author pengwei
 * @since 2020-04-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "TruckSerialVO对象", description = "装车发货序列号日志")
public class TruckSerialVO extends TruckSerial {
	private static final long serialVersionUID = 1L;

	/**
	 * 车次编号
	 */
	@ApiModelProperty("车次编号")
	private String truckCode;
	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;

}
