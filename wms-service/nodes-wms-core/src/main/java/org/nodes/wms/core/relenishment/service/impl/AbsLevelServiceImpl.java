package org.nodes.wms.core.relenishment.service.impl;

import org.nodes.wms.core.relenishment.service.IFunctionCode;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.context.annotation.Primary;

@Primary
public abstract class AbsLevelServiceImpl implements IFunctionCode {
	IStockService stockService;
	public AbsLevelServiceImpl(){
		 stockService = SpringUtil.getBean(IStockService.class);
	}
}
