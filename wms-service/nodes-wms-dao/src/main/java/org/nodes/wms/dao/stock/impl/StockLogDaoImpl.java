package org.nodes.wms.dao.stock.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.stock.StockLogDao;
import org.nodes.wms.dao.stock.dto.input.StockLogPageQuery;
import org.nodes.wms.dao.stock.dto.output.StockLogExcelResponse;
import org.nodes.wms.dao.stock.dto.output.StockLogPageResponse;
import org.nodes.wms.dao.stock.entities.StockLog;
import org.nodes.wms.dao.stock.mapper.StockLogMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 库存日志dao实现类
 **/
@Repository
public class StockLogDaoImpl extends BaseServiceImpl<StockLogMapper, StockLog> implements StockLogDao {

	@Override
	public Page<StockLogPageResponse> page(IPage<?> page, StockLogPageQuery stockLogPageQuery) {
		return super.baseMapper.page(page, stockLogPageQuery);
	}

	@Override
	public List<StockLogExcelResponse> listByQuery(StockLogPageQuery stockLogPageQuery) {
		return super.baseMapper.listByQuery(stockLogPageQuery);
	}
}
