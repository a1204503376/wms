package org.nodes.wms.core.warehouse.wrapper;

import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.nodes.wms.core.warehouse.entity.LpnState;
import org.nodes.wms.core.warehouse.vo.LpnStateVO;


import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.nodes.wms.core.warehouse.service.ILpnStateService;


/**
 * 容器状态表包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2021-10-12
 */
public class LpnStateWrapper extends BaseEntityWrapper<LpnState, LpnStateVO>  {

	public static LpnStateWrapper build() {
		return new LpnStateWrapper();
 	}

	@Override
	public LpnStateVO entityVO(LpnState lpnState) {
		LpnStateVO lpnStateVO = BeanUtil.copy(lpnState, LpnStateVO.class);
        if (Func.isNotEmpty(lpnStateVO)) {

        }
		return lpnStateVO;
	}
}
