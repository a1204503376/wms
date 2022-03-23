
package org.nodes.wms.core.strategy.service;

import org.nodes.wms.core.common.entity.AttributeBase;
import org.nodes.wms.core.strategy.entity.OutstockConfigUdf;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 服务类
 *
 * @author NodeX
 * @since 2019-12-10
 */
public interface IOutstockConfigUdfService extends BaseService<OutstockConfigUdf> {

	/**
	 * 比较单据自定义属性
	 *
	 * @param outstockConfigUdfs 单据自定义属性集合
	 * @param soHeader           验证单据主表
	 * @return 自定义属性成立结果集合
	 */
	List<Boolean> matchBillUdf(List<OutstockConfigUdf> outstockConfigUdfs, AttributeBase soHeader);
}
