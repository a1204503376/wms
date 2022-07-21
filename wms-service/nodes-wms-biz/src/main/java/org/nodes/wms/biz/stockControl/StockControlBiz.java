package org.nodes.wms.biz.stockControl;

import org.nodes.wms.dao.stock.dto.input.*;

/**
 * 库存控制BIZ
 */
public interface StockControlBiz {
	/**
	 * 按库位冻结
	 *
	 * @param request 请求对象-内部包含库位
	 */
	void freezeByLocCodeAction(FreezeByLocCodeRequest request);

	/**
	 * 按批次号冻结
	 *
	 * @param request 请求对象-内部包含批次号
	 */
	void freezeByLotNumberAction(FreezeByLotNumberRequest request);

	/**
	 * 按序列号冻结
	 *
	 * @param request 请求对象-内部包含序列号
	 */
	void freezeBySerialNumberAction(FreezeBySerialNumberRequest request);

	/**
	 * 部分冻结
	 *
	 * @param request 请求对象-内部包含 物品编码-库位编码-批次号-序列号
	 */
	void portionFreezeAction(PortionFreezeRequest request);

	/**
	 * 按库位解冻
	 *
	 * @param request 请求对象-内部包含库位
	 */
	void unFreezeByLocCodeAction(UnFreezeByLocCodeRequest request);

	/**
	 * 按批次号解冻
	 *
	 * @param request 请求对象-内部包含批次号
	 */
	void unFreezeByLotNumberAction(UnFreezeByLotNumberRequest request);

	/**
	 * 按序列号解冻
	 *
	 * @param request 请求对象-内部包含序列号
	 */
	void unFreezeBySerialNumberAction(UnFreezeBySerialNumberRequest request);

	/**
	 * 部分解冻
	 *
	 * @param request 请求对象-内部包含 物品编码-库位编码-批次号-序列号
	 */
	void portionUnFreezeAction(PortionUnFreezeRequest request);
}
