package org.nodes.wms.core.stock.transfer.service;

import org.nodes.wms.core.stock.core.dto.StockMoveByBoxQueryDTO;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.vo.StockSkuMoveSubmitVO;
import org.nodes.wms.core.stock.core.vo.StockSkuMoveVO;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 库存内部接口
 * @Author zx
 * @Date 2020/3/18
 */
public interface IStockInnerService extends BaseService<Stock> {

	/**
	 * 查询库房列表
	 * @return
	 */
	List<StockSkuMoveVO> listWarehouse(StockSkuMoveSubmitVO stockSkuMove);

	/**
	 * 通过容器编码查询库位编号
	 * @param stockSkuMove
	 * @return
	 */
	StockSkuMoveVO getLocCodeByLpnCode(StockSkuMoveSubmitVO stockSkuMove);

	/**
	 * 通过物品ID获得物品信息
	 * @param stockSkuMove
	 * @return
	 */
	StockSkuMoveVO getSkuInfoBySkuId(StockSkuMoveSubmitVO stockSkuMove);

	/**
	 * 检验物品数量
	 * @param stockSkuMove
	 * @return
	 */
	StockSkuMoveVO verifySkuQty(StockSkuMoveSubmitVO stockSkuMove);

	/**
	 * 校验批次数量
	 * @param stockSkuMove
	 * @return
	 */
	StockSkuMoveVO verifyLotQty(StockSkuMoveSubmitVO stockSkuMove);

	/**
	 * 移动库存物品
	 * @param stockSkuMove
	 * @return
	 */
	boolean moveStockSku(StockSkuMoveSubmitVO stockSkuMove);

	/**
	 * 按箱移动库存
	 *
	 * @param stockMoveByBoxQueryDTO 按箱移动参数
	 * @return
	 */
	boolean submitSkuInfoForBox(StockMoveByBoxQueryDTO stockMoveByBoxQueryDTO);

}
