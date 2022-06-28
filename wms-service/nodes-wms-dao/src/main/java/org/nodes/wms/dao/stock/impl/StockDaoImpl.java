package org.nodes.wms.dao.stock.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.nodes.wms.dao.stock.StockDao;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.mapper.StockMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 库存Dao接口实现类
 **/
@Repository
public class StockDaoImpl extends BaseServiceImpl<StockMapper, Stock> implements StockDao {

	@Override
	public Map<String, Object> getStockQtyByLocIdList(List<Long> locIdList) {
		return super.baseMapper.selectStockQtyByLocIdList(locIdList);
	}

	@Override
	public Integer getStockSkuCountByLocIdList(List<Long> locIdList) {
		return super.baseMapper.selectStockSkuCountByLocIdList(locIdList);
	}
}
