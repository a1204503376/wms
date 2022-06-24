package org.nodes.wms.dao.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.models.auth.In;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * 库存mapper接口
 **/
@Repository("stockRepository")
public interface StockMapper extends BaseMapper<Stock> {

	/**
	 * 根据库位id查询库存数量
	 *
	 * @param locIdList:
	 * @return BigDecimal
	 */
	HashMap<String,Object> selectStockQtyByLocIdList(List<Long> locIdList);

	/**
	 * 根据库位id查询物品总数
	 *
	 * @param locIdList:
	 * @return Integer
	 */
	Integer selectStockSkuCountByLocIdList(List<Long> locIdList);
}
