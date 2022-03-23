package org.nodes.wms.core.instock.asn.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 入库 上架 提交VO
 *
 * @Author zx
 * @Date 2020/3/23
 **/
@Data
public class InStockSubmitVO {

	@ApiModelProperty("容器编码")
	private String lpnCode;

	@ApiModelProperty("库区编码")
	private String zoneCode;

	@ApiModelProperty("目标库位编码")
	private String locCode;

	@ApiModelProperty("库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;

	/**
	 * 原库位id
	 */
	private Long sourceLocId;
	/**
	 * 目标库位id
	 */
	private Long targetLocId;
}
