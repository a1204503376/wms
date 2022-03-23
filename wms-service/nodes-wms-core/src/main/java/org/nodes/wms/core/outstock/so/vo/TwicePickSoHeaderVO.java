package org.nodes.wms.core.outstock.so.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 二次分拣单据信息VO
 * @Author zx
 * @Date 2020/5/8
 **/
@Data
public class TwicePickSoHeaderVO {
	/**
	 * 单据编号
	 */
	@ApiModelProperty("单据编号")
	private String soBillNo;
	/**
	 * 单据ID
	 */
	@ApiModelProperty("单据ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;
	/**
	 * 客户名称
	 */
	@JsonProperty("cName")
	@ApiModelProperty("客户名称")
	private String cName;
	/**
	 * 货主名称
	 */
	@ApiModelProperty("货主名称")
	private String ownerName;

}
