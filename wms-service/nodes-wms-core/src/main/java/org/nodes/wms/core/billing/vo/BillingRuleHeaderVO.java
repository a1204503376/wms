package org.nodes.wms.core.billing.vo;

import io.swagger.annotations.ApiModelProperty;
import org.nodes.wms.core.billing.entity.BillingRuleHeader;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 计费规则主表视图实体类
 *
 * @author NodeX
 * @since 2021-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BillingRuleHeaderVO对象", description = "计费规则主表")
public class BillingRuleHeaderVO extends BillingRuleHeader {
	private static final long serialVersionUID = 1L;

	/**
	 * 明细集合
	 */
	@ApiModelProperty("明细集合")
	private List<BillingRuleDetailVO> detailList = new ArrayList<>();
}
