package org.nodes.wms.core.billing.vo;

import org.nodes.wms.core.billing.entity.BillingItem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

/**
 * 计费项目视图实体类
 *
 * @author NodeX
 * @since 2021-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BillingItemVO对象", description = "计费项目")
public class BillingItemVO extends BillingItem {
	private static final long serialVersionUID = 1L;

}
