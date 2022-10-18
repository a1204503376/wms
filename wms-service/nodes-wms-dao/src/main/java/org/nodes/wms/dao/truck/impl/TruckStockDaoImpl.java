package org.nodes.wms.dao.truck.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.truck.TruckStockDao;
import org.nodes.wms.dao.truck.entities.TruckStock;
import org.nodes.wms.dao.truck.mapper.TruckStockMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 发运记录DAO
 *
 * @author nodesc
 */
@Repository
@RequiredArgsConstructor
public class TruckStockDaoImpl
	extends BaseServiceImpl<TruckStockMapper, TruckStock>
	implements TruckStockDao {
	@Override
	public TruckStock getByBoxCodeAndHeaderId(TruckStock truckStock) {
		AssertUtil.notNull(truckStock.getBoxCode(), "查询发运记录失败，箱码为空");
		AssertUtil.notNull(truckStock.getBillHeaderId(), "查询发运记录失败，单据头表id为空");
		return super.lambdaQuery()
			.eq(TruckStock::getBoxCode, truckStock.getBoxCode())
			.eq(TruckStock::getBillHeaderId, truckStock.getBillHeaderId())
			.one();
	}

	@Override
	public List<TruckStock> getList(Long billHeaderId) {
		AssertUtil.notNull(billHeaderId, "查询发运记录失败，单据头表id为空");
		return super.lambdaQuery()
			.eq(TruckStock::getBillHeaderId, billHeaderId)
			.list();
	}
}
