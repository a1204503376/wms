package org.nodes.wms.core.instock.asn.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.instock.asn.entity.AsnDetail;

import java.math.BigDecimal;
import java.util.List;

/**
 * 收货单明细提交VO
 * @Author zx
 * @Date 2020/4/1
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "收货单明细提交VO", description = "收货单明细提交VO")
public class AsnDetailDTO extends AsnDetail {

	/**
	 * 包装明细ID
	 */
	@ApiModelProperty("包装明细ID")
	private Long wspdId;

	/**
	 * 序列号列表
	 */
	@ApiModelProperty("序列号列表")
	private List<String> serialList;
	/**
	 * 计划数量
	 */
	@ApiModelProperty(value = "计划数量")
	private BigDecimal planQty;
	/**
	 * 实际数量
	 */
	@ApiModelProperty(value = "实际数量")
	private BigDecimal scanQty;
	/**
	 * 剩余数量
	 */
	@ApiModelProperty(value = "剩余数量")
	private BigDecimal surplusQty;
}
