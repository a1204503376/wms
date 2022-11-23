package org.nodes.wms.dao.stock.impl;

import org.nodes.wms.dao.stock.StockBalanceDao;
import org.nodes.wms.dao.stock.entities.StockBalance;
import org.nodes.wms.dao.stock.mapper.StockBalanceMapper;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 库存Dao接口实现类
 *
 * @author nodesc
 **/
@Repository
public class StockBalanceDaoImpl extends BaseServiceImpl<StockBalanceMapper, StockBalance> implements StockBalanceDao {

	@Override
	public List<StockBalance> getStockBalanceList(String startTime, String endTime) {
		return super.baseMapper.selectStockBalanceList(startTime, endTime);
	}

	@Override
	public void savaStockBalanceBatch(List<StockBalance> stockBalanceList) {
		if (!super.saveBatch(stockBalanceList)) {
			throw new ServiceException("批量保存收发存库存失败,请检查参数");
		}
	}
}
