package org.nodes.wms.core.billing.dto;

import org.nodes.wms.core.billing.entity.BillingAgreement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 计费协议数据传输对象实体类
 *
 * @author NodeX
 * @since 2021-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BillingAgreementDTO extends BillingAgreement {
	private static final long serialVersionUID = 1L;

}
