package org.nodes.wms.core.billing.vo;

import org.nodes.wms.core.billing.entity.BillingRuleDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

/**
 * 计费规则明细视图实体类
 *
 * @author NodeX
 * @since 2021-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BillingRuleDetailVO对象", description = "计费规则明细")
public class BillingRuleDetailVO extends BillingRuleDetail {
	private static final long serialVersionUID = 1L;

}
