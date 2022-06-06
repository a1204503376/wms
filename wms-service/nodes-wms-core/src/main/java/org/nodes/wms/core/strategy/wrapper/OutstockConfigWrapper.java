
package org.nodes.wms.core.strategy.wrapper;

import org.nodes.wms.core.basedata.cache.BillTypeCache;
import org.nodes.wms.core.basedata.cache.SkuTypeCache;
import org.nodes.wms.dao.basics.billType.entities.BillType;
import org.nodes.wms.dao.basics.sku.entities.SkuType;
import org.nodes.wms.core.basedata.service.ISkuTypeService;
import org.nodes.wms.core.strategy.entity.OutstockConfig;
import org.nodes.wms.core.strategy.vo.OutstockConfigVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;

/**
 * 分配策略执行条件包装类,返回视图层所需的字段
 *
 * @author pengwei
 * @since 2020-02-12
 */
public class OutstockConfigWrapper extends BaseEntityWrapper<OutstockConfig, OutstockConfigVO> {

	public static OutstockConfigWrapper build() {
		return new OutstockConfigWrapper();
	}

	@Override
	public OutstockConfigVO entityVO(OutstockConfig outstockConfig) {
		OutstockConfigVO outstockConfigVO = BeanUtil.copy(outstockConfig, OutstockConfigVO.class);
		if (ObjectUtil.isNotEmpty(outstockConfigVO)) {
			ISkuTypeService skuTypeService = SpringUtil.getBean(ISkuTypeService.class);
			SkuType skuType = skuTypeService.getById(outstockConfigVO.getSkuTypeId());
			if (Func.isNotEmpty(skuType)) {
				outstockConfigVO.setSkuTypeIdDesc(skuType.getTypeName());
			}
			BillType billType = BillTypeCache.getByCode(outstockConfigVO.getBillTypeCd());
			if (Func.isNotEmpty(billType)) {
				outstockConfigVO.setBillTypeCdDesc(billType.getBillTypeName());
			}
		}

		return outstockConfigVO;
	}
}
