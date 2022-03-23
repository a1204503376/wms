package org.nodes.wms.core.billing.service;

import org.nodes.wms.core.billing.entity.BillingItem;
import org.nodes.wms.core.billing.vo.BillingItemVO;
import org.springblade.core.mp.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 计费项目 服务类
 *
 * @author NodeX
 * @since 2021-06-19
 */
public interface IBillingItemService extends BaseService<BillingItem> {

	/**
	 * 重写save
	 */
	boolean saveOrUpdate(BillingItem billingItem);
}
