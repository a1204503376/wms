package org.nodes.wms.core.outstock.so.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 包装明细视图VO
 *
 * @Author zx
 * @Date 2020/3/5
 **/
@Data
public class SkuPackageDetailViewVO {

	/**
	 * 包装明细ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "包装明细ID")
	private Long wspdId;

	/**
	 * 计量单位编码
	 */
	@ApiModelProperty(value = "计量单位编码")
	private String wsuCode;
	/**
	 * 计量单位名称
	 */
	@ApiModelProperty(value = "计量单位名称")
	private String wsuName;
	/**
	 * 层级
	 */
	@ApiModelProperty(value = "层级")
	private Integer skuLevel;
	/**
	 * 换算倍率
	 */
	@ApiModelProperty(value = "换算倍率")
	private Integer convertQty;

}
