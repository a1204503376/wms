package org.nodes.wms.core.instock.asn.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 收货单头表手持VO
 *
 * @Author zx
 * @Date 2020/1/13
 **/
@Data
public class AsnHeaderMinVO {

	/**
	 * 主键
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "主键")
	private Long asnBillId;

	/**
	 * 单据编码
	 */
	@ApiModelProperty(value = "单据编码")
	private String asnBillNo;

	/**
	 * 库房名称
	 */
	@ApiModelProperty(value = "库房名称")
	private String whName;
	/**
	 * 库房ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "库房名称")
	private Long whId;

	/**
	 * 货主名称
	 */
	@ApiModelProperty(value = "货主名称")
	private String ownerName;

	/**
	 * 供应商名称
	 */
	@ApiModelProperty(value = "供应商名称")
	private String sName;

	/**
	 * 收货单明细表
	 */
	@ApiModelProperty(value = "收货单明细表")
	private AsnDetailMinVO asnDetailMinVO;

}
