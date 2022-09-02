package org.nodes.wms.biz.stockManage;

import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.stock.dto.input.*;
import org.nodes.wms.dao.stock.dto.output.EstimateStockMoveResponse;
import org.nodes.wms.dao.stock.entities.Stock;

import java.util.List;

/**
 * 库存控制BIZ
 *
 * @author nodesc
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
	 *
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
	 *
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

	/**
	 * 批量冻结库存
	 *
	 * @param stockThawAndFrozenDto 包含箱码、生产批次、库位集合
	 */
	void stockFrozen(StockThawAndFrozenDto stockThawAndFrozenDto);

	/**
	 * 批量解冻库存
	 *
	 * @param stockThawAndFrozenDto 包含箱码、生产批次、库位集合
	 */
	void stockUnFrozen(StockThawAndFrozenDto stockThawAndFrozenDto);

	/**
	 * PDA库存管理:LPN移动-查询根据条件查询库存，判断库存是否唯一，并且是否可移动
	 *
	 * @param lpnCode 托盘号
	 */
	void decideStockLpn(String lpnCode);

	/**
	 * 库存余额：PC按件移动
	 *
	 * @param stockPcMoveRequest PC按件移动参数
	 */
	void stockMoveByPc(StockPcMoveRequest stockPcMoveRequest);

	/**
	 * 判断库存是否可以移动（天宜定制）
	 * 1. 不能移动到出库集货区和虚拟库区
	 * 2. 如果是自动区则要求目标库位必须是空库位（库位上没有库存）
	 * 3. 只能是同类型（自动与人工区）的库区之间移动
	 * 4. 校验目标库位的箱型
	 * 5. 校验载重
	 *
	 * @param sourceLocation sourceLocation
	 * @param targetLocation targetLocation
	 * @param stockList      stockList
	 * @param boxCode        boxCode
	 * @param checkLocType   是否校验同类型库位移动，True则校验，false则不校验
	 */
	void canMove(Location sourceLocation, Location targetLocation, List<Stock> stockList,
				 String boxCode, Boolean checkLocType);

	/**
	 * 库存移动校验库位的箱型
	 *
	 * @param targetLocation targetLocation
	 * @param sourceLocation sourceLocation
	 */
	void canMoveToBoxType(Location targetLocation, Location sourceLocation);

}
