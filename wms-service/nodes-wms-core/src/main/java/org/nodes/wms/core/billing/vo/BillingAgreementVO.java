package org.nodes.wms.core.billing.vo;

import io.swagger.annotations.ApiModelProperty;
import org.nodes.wms.core.billing.entity.BillingAgreement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

/**
 * 计费协议视图实体类
 *
 * @author NodeX
 * @since 2021-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BillingAgreementVO对象", description = "计费协议")
public class BillingAgreementVO extends BillingAgreement {
	private static final long serialVersionUID = 1L;

	/**
	 * 计费规则
	 */
	@ApiModelProperty("计费规则")
	private String ruleHeaderName;
	/**
	 * 计费协议执行状态
	 */
	@ApiModelProperty("计费协议执行状态")
	private String executeStateName;
	/**
	 * 计费协议状态
	 */
	@ApiModelProperty("计费协议状态")
	private String agreementStateName;
	/**
	 * 是否自动续约
	 */
	@ApiModelProperty("是否自动续约")
	private String isAutoName;
}
