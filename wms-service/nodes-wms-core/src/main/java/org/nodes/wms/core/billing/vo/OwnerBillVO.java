package org.nodes.wms.core.billing.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * author: pengwei
 * date: 2021/7/2 15:56
 * description: OwnerBillVO
 */
@Data
@ApiModel("货主账单")
public class OwnerBillVO {

	/**
	 * 货主名称
	 */
	@ApiModelProperty("货主名称")
	private String ownerName;
	/**
	 * 生效日期
	 */
	@ApiModelProperty("生效日期")
	private LocalDate effectiveDate;
	/**
	 * 解约日期
	 */
	@ApiModelProperty("解约日期")
	private LocalDate terminationDate;
	/**
	 * 结算日期
	 */
	@ApiModelProperty("结算日期")
	private LocalDate closingDate;
	/**
	 * 合计(万元)
	 */
	@ApiModelProperty("合计(万元)")
	private BigDecimal totalAmount;
}
