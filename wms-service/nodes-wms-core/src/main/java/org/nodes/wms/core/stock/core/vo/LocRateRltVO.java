package org.nodes.wms.core.stock.core.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pengwei
 * @program WmsCore
 * @description 库位使用频率
 * @create 20200408
 */
@Data
public class LocRateRltVO {

	/**
	 * 库房ID
	 */
	@ApiModelProperty("库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;
	/**
	 * 库区ID
	 */
	@ApiModelProperty("库区ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long zoneId;
	/**
	 * 库区名称
	 */
	@ApiModelProperty("库区名称")
	private String zoneName;
	/**
	 * 库位ID
	 */
	@ApiModelProperty("库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;
	/**
	 * 库位编码
	 */
	@ApiModelProperty("库位编码")
	@JsonSerialize(using = ToStringSerializer.class)
	private String locCode;
	/**
	 * 频率
	 */
	@ApiModelProperty("频率")
	private Integer rate;
}
