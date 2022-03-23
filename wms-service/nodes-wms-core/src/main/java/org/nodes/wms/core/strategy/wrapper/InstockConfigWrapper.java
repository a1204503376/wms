package org.nodes.wms.core.strategy.wrapper;

import org.nodes.wms.core.basedata.cache.BillTypeCache;
import org.nodes.wms.core.basedata.cache.SkuTypeCache;
import org.nodes.wms.core.basedata.entity.BillType;
import org.nodes.wms.core.basedata.entity.SkuType;
import org.nodes.wms.core.basedata.service.ISkuTypeService;
import org.nodes.wms.core.strategy.entity.InstockConfig;
import org.nodes.wms.core.strategy.vo.InstockConfigVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;

public class InstockConfigWrapper extends BaseEntityWrapper<InstockConfig, InstockConfigVO> {
	public static InstockConfigWrapper build() {
		return new InstockConfigWrapper();
	}

	@Override
	public InstockConfigVO entityVO(InstockConfig entity) {
		InstockConfigVO config = BeanUtil.copy(entity, InstockConfigVO.class);

		if (ObjectUtil.isNotEmpty(config)) {
			ISkuTypeService skuTypeService = SpringUtil.getBean(ISkuTypeService.class);
			SkuType skuType = skuTypeService.getById(config.getSkuTypeId());
			if (ObjectUtil.isNotEmpty(skuType)) {
				config.setSkuTypeIdDesc(skuType.getTypeName());
			}
			BillType billType = BillTypeCache.getByCode(config.getBillTypeCd());
			if (ObjectUtil.isNotEmpty(billType)) {
				config.setBillTypeIdDesc(billType.getBillTypeName());
			}
		}

		return config;
	}
}
