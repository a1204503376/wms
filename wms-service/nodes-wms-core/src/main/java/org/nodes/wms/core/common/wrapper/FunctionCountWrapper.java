package org.nodes.wms.core.common.wrapper;

import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.nodes.wms.core.common.entity.FunctionCount;
import org.nodes.wms.core.common.vo.FunctionCountVO;


import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.nodes.wms.core.common.service.IFunctionCountService;


/**
 * 功能计数	包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2021-07-23
 */
public class FunctionCountWrapper extends BaseEntityWrapper<FunctionCount, FunctionCountVO>  {

	public static FunctionCountWrapper build() {
		return new FunctionCountWrapper();
 	}

	@Override
	public FunctionCountVO entityVO(FunctionCount functionCount) {
		FunctionCountVO functionCountVO = BeanUtil.copy(functionCount, FunctionCountVO.class);
        if (Func.isNotEmpty(functionCountVO)) {

        }
		return functionCountVO;
	}
}
