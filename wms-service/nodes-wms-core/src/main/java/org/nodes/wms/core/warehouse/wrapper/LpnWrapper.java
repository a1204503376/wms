package org.nodes.wms.core.warehouse.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.wms.core.warehouse.dto.LpnDTO;
import org.nodes.wms.core.warehouse.entity.Lpn;
import org.nodes.wms.core.warehouse.excel.LpnExcel;
import org.nodes.wms.core.warehouse.vo.LpnVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;

public class LpnWrapper extends BaseEntityWrapper<Lpn, LpnVO> {
	public static LpnWrapper build() {
		return new LpnWrapper();
	}

	@Override
	public LpnVO entityVO(Lpn entity) {
		LpnVO lpnVO = BeanUtil.copy(entity, LpnVO.class);
		if (Func.isNotEmpty(lpnVO)) {
			lpnVO.setLpnTypeDesc(DictCache.getValue("lpn_type", lpnVO.getLpnType()));
		}
		return lpnVO;
	}

	public LpnDTO entityDTO(LpnExcel lpnExcel) {
		LpnDTO lpnDTO = BeanUtil.copy(lpnExcel, LpnDTO.class);

		return lpnDTO;
	}
}
