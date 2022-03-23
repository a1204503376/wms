package org.nodes.wms.core.billing.service;

import org.nodes.wms.core.billing.dto.BillingRuleHeaderDTO;
import org.nodes.wms.core.billing.entity.BillingRuleHeader;
import org.nodes.wms.core.billing.vo.BillingRuleHeaderVO;
import org.springblade.core.mp.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;

/**
 * 计费规则主表 服务类
 *
 * @author NodeX
 * @since 2021-06-19
 */
public interface IBillingRuleHeaderService extends BaseService<BillingRuleHeader> {

	/**
	 * 保存-计费规则
	 *
	 * @param billingRuleHeaderDTO
	 * @return
	 */
	boolean save(BillingRuleHeaderDTO billingRuleHeaderDTO);

	/**
	 * 更新-计费规则
	 *
	 * @param billingRuleHeaderDTO
	 * @return
	 */
	boolean updateById(BillingRuleHeaderDTO billingRuleHeaderDTO);

	/**
	 * 保存或更新
	 *
	 * @param entity
	 * @return
	 */
	boolean saveOrUpdate(BillingRuleHeaderDTO entity);

	/**
	 * 获取详细信息
	 *
	 * @param id
	 * @return
	 */
	BillingRuleHeaderVO getDetail(Serializable id);
}
