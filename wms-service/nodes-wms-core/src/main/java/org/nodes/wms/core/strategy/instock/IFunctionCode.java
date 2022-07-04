package org.nodes.wms.core.strategy.instock;

import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.putway.entities.StInstockDetail;
import org.nodes.wms.core.strategy.vo.InstockExecuteVO;

public interface IFunctionCode {

	/**
	 * 入库
	 * @param stock
	 */
	void execute(Stock stock, StInstockDetail instockDetail, InstockExecuteVO instockExecute);
}
