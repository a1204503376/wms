package org.nodes.wms.core.stock.transfer.service;


import org.nodes.wms.core.stock.core.dto.StockMoveByBoxQueryDTO;
import org.nodes.wms.core.stock.core.dto.StockMoveByBoxSubmitDTO;
import org.nodes.wms.core.stock.core.vo.StockMoveByBoxVO;
import org.nodes.wms.core.stock.core.vo.StockMoveByBoxVerifyVO;

import java.util.List;

/**
 * 按箱移动服务接口
 * @Author zx
 * @Date 2020/7/24
 **/
public interface IStockMoveByBoxService {

	/**
	 * 按箱移动 查询待移动库存
	 * @param stockMoveByBoxQueryDTO
	 * @return
	 */
	StockMoveByBoxVO getMoveStock(StockMoveByBoxQueryDTO stockMoveByBoxQueryDTO);

	/**
	 * 按箱移动 物品移动验证
	 * @param stockMoveByBoxSubmitDTOList
	 * @return
	 */
	List<StockMoveByBoxVerifyVO> verifyStockForMoveByBox(List<StockMoveByBoxSubmitDTO> stockMoveByBoxSubmitDTOList);

	/**
	 * 按箱移动 提交库存移动信息
	 * @param stockMoveByBoxSubmitDTOList
	 * @return
	 */
	boolean submitStockForMoveByBox(List<StockMoveByBoxSubmitDTO> stockMoveByBoxSubmitDTOList);

}
