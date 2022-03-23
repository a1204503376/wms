package org.nodes.wms.core.billing.service;

import org.nodes.wms.core.billing.dto.BillingRuleDetailDTO;
import org.nodes.wms.core.billing.entity.BillingRuleDetail;
import org.springblade.core.mp.base.BaseService;

import java.util.Collection;
import java.util.List;

/**
 * 计费规则明细 服务类
 *
 * @author NodeX
 * @since 2021-06-19
 */
public interface IBillingRuleDetailService extends BaseService<BillingRuleDetail> {

	boolean saveOrUpdateBatch(List<BillingRuleDetailDTO> entityList);
}
