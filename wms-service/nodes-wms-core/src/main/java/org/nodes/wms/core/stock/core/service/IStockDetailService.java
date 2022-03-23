package org.nodes.wms.core.stock.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.core.stock.core.dto.StockDetailAddDTO;
import org.nodes.wms.core.stock.core.dto.StockDetailSubtractDTO;
import org.nodes.wms.core.stock.core.entity.StockDetail;
import org.nodes.wms.core.stock.core.vo.StockDetailVo;
import org.springblade.core.mp.base.BaseService;

import javax.validation.Valid;
import java.util.List;

/**
 * 库存明细表 服务类
 *
 * @author NodeX
 * @since 2021-10-12
 */
public interface IStockDetailService extends BaseService<StockDetail> {

	/**
	 * 库存明细增加
	 *
	 * @param stockDetailProc 库存明细增加参数
	 * @return 是否成功
	 */
	boolean add(@Valid StockDetailAddDTO stockDetailProc);

	/**
	 * 库存明细扣减
	 *
	 * @param stockDetailSubtract 库存明细扣减参数
	 * @return 是否成功
	 */
	boolean substract(@Valid StockDetailSubtractDTO stockDetailSubtract);

	/**
	 * 库存明细锁定
	 *
	 * @param stockDetailIdList 库存明细id集合
	 * @return 是否成功
	 */
	boolean lock(List<Long> stockDetailIdList);

	/**
	 * 库存明细解锁
	 *
	 * @param stockDetailIdList 库存明细id集合
	 * @return 是否成功
	 */
	boolean unlock(List<Long> stockDetailIdList);

	IPage<StockDetailVo> selectStockDetailsByStockId(IPage<StockDetailVo> page, Long stockId);
}
