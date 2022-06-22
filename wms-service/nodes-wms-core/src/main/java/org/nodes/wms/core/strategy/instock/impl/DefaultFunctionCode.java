package org.nodes.wms.core.strategy.instock.impl;

import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.strategy.entity.InstockDetail;
import org.nodes.wms.core.strategy.instock.FunctionCodeBase;
import org.nodes.wms.core.strategy.instock.IFunctionCode;
import org.nodes.wms.core.strategy.vo.InstockExecuteVO;
import org.springframework.context.annotation.Primary;

/**
 * author: pengwei
 * date: 2021/5/26 13:44
 * description: DefaultFunctionCode
 */
@Primary
public class DefaultFunctionCode extends FunctionCodeBase implements IFunctionCode {

	@Override
	public void execute(Stock stock, InstockDetail instockDetail, InstockExecuteVO instockExecute) {
	}
}
