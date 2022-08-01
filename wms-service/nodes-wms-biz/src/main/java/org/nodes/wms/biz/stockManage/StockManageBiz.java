package org.nodes.wms.biz.stockManage;

import org.nodes.wms.dao.stock.dto.input.*;
import org.nodes.wms.dao.stock.dto.output.EstimateStockMoveResponse;

import java.util.List;

/**
 * 库存控制BIZ
 */
public interface StockManageBiz {
	/**
	 * 按库位冻结
	 *
	 * @param locCode 库位编码
	 */
	void freezeByLocCodeAction(String locCode, Long whId);

	/**
	 * 按批次号冻结
	 *
	 * @param lotNumber 批次号
	 */
	void freezeByLotNumberAction(String lotNumber);

	/**
	 * 按序列号冻结
	 *
	 * @param serialNumber 序列号
	 */
	void freezeBySerialNumberAction(List<String> serialNumber);

	/**
	 * 部分冻结
	 *
	 * @param request 请求对象-内部包含 物品编码-库位编码-批次号-序列号
	 */
	void portionFreezeAction(PortionFreezeRequest request);

	/**
	 * 按箱冻结
	 * @param boxCode 箱码 必填
	 */
	void freezeStockByBoxCodeAction(String boxCode);

	/**
	 * 按库位解冻
	 *
	 * @param locCode 库位
	 */
	void unFreezeByLocCodeAction(String locCode, Long whId);

	/**
	 * 按批次号解冻
	 *
	 * @param lotNumber 批次号
	 */
	void unFreezeByLotNumberAction(String lotNumber);

	/**
	 * 按序列号解冻
	 *
	 * @param serialNumber 序列号
	 */
	void unFreezeBySerialNumberAction(String serialNumber);

	/**
	 * 部分解冻
	 *
	 * @param request 请求对象-内部包含 物品编码-库位编码-批次号-序列号
	 */
	void portionUnFreezeAction(PortionUnFreezeRequest request);

	/**
	 * 按箱解冻
	 * @param boxCode 箱码 必填
	 */
	void unFreezeStockByBoxCodeAction(String boxCode);

	/**
	 * PDA库存管理:标准移动
	 *
	 * @param request Pda库存移动-请求对象
	 */
	void stockMove(StockMoveRequest request);


	/**
	 * PDA库存管理:LPN移动
	 *
	 * @param request Pda库存移动-请求对象
	 */
	void stockMoveByLpn(StockMoveByLpnRequest request);

	/**
	 * PDA库存管理:按箱移动
	 *
	 * @param request Pda库存按箱移动-请求对象
	 */
	void stockMoveByBox(StockMoveByBoxCodeRequest request);

	/**
	 * Pda判断库存是否可移动
	 *
	 * @param request 请求对象
	 * @return 是否序列号管理
	 */
	EstimateStockMoveResponse skuIsSn(EstimateStockMoveRequest request);
}
