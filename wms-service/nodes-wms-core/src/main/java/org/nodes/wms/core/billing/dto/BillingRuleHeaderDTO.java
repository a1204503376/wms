package org.nodes.wms.core.billing.dto;

import io.swagger.annotations.ApiModelProperty;
import org.nodes.wms.core.billing.entity.BillingRuleHeader;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 计费规则主表数据传输对象实体类
 *
 * @author NodeX
 * @since 2021-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BillingRuleHeaderDTO extends BillingRuleHeader {
	private static final long serialVersionUID = 1L;

	/**
	 * 明细集合
	 */
	@ApiModelProperty("明细集合")
	private List<BillingRuleDetailDTO> detailList = new ArrayList<>();
}
