package org.nodes.wms.core.warehouse.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.dto.PlatformInfoDTO;
import org.nodes.wms.core.warehouse.entity.PlatformInfo;
import org.nodes.wms.core.warehouse.excel.PlatformInfoExcel;
import org.nodes.wms.core.warehouse.vo.PlatformInfoVO;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;

public class PlatforminfoWrapper extends BaseEntityWrapper<PlatformInfo, PlatformInfoVO> {
	public static PlatforminfoWrapper build() {
		return new PlatforminfoWrapper();
	}

	@Override
	public PlatformInfoVO entityVO(PlatformInfo platformInfo) {
		PlatformInfoVO platformInfoVO = BeanUtil.copy(platformInfo, PlatformInfoVO.class);
		Warehouse warehouse = WarehouseCache.getById(platformInfo.getWhId());
		if (Func.isNotEmpty(warehouse)) {
			platformInfoVO.setWhName(warehouse.getWhName());
		}
		platformInfoVO.setPlatformTypeDesc(
			DictCache.getValue(DictCodeConstant.PLATFORM_TYPE, platformInfoVO.getPlatformType()));
		return platformInfoVO;
	}

	public PlatformInfoDTO entityDTO(PlatformInfoExcel platformInfoExcel) {
		PlatformInfoDTO entityDTO = BeanUtil.copy(platformInfoExcel, PlatformInfoDTO.class);
		return entityDTO;
	}
}
