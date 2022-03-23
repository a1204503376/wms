
package org.nodes.wms.core.system.service;

import org.nodes.wms.core.system.entity.BarcodeRule;
import org.springblade.core.mp.base.BaseService;

/**
 *  条码规则服务类
 *
 * @author liangmei
 * @since 2019-12-16
 */
public interface IBarcodeRuleService extends BaseService<BarcodeRule> {

	/**
	 * 新增条码规则
	 * @param barcodeRule
	 * @return
	 */
	boolean save(BarcodeRule barcodeRule);
	/**
	 * 修改方法
	 * @param barcodeRule
	 * @return
	 */
	boolean updateById(BarcodeRule barcodeRule);
}
