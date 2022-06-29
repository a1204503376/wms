package org.nodes.wms.dao.stock.impl;

import org.nodes.wms.dao.stock.StockOccupyDao;
import org.nodes.wms.dao.stock.entities.StockOccupy;
import org.nodes.wms.dao.stock.mapper.StockOccupyMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * 库存占用Dao接口实现类
 **/
@Repository
public class StockOccupyDaoImpl
	extends BaseServiceImpl<StockOccupyMapper, StockOccupy> implements StockOccupyDao {
}
