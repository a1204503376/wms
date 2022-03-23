package org.nodes.wms.core.billing.dto;

import org.nodes.wms.core.billing.entity.BillingRuleDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 计费规则明细数据传输对象实体类
 *
 * @author NodeX
 * @since 2021-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BillingRuleDetailDTO extends BillingRuleDetail {
	private static final long serialVersionUID = 1L;

}
