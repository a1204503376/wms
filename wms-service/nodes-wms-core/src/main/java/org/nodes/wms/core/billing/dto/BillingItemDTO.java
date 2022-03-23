package org.nodes.wms.core.billing.dto;

import org.nodes.wms.core.billing.entity.BillingItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 计费项目数据传输对象实体类
 *
 * @author NodeX
 * @since 2021-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BillingItemDTO extends BillingItem {
	private static final long serialVersionUID = 1L;

}
