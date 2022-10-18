package org.nodes.wms.biz.tuck.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.tuck.TruckStockBiz;
import org.nodes.wms.dao.truck.TruckStockDao;
import org.nodes.wms.dao.truck.entities.TruckStock;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TruckStockBizImpl implements TruckStockBiz {
	private final TruckStockDao truckStockDao;

	@Override
	public void insert(TruckStock truckStock) {
		truckStock.setCreateUser(AuthUtil.getUserId());
		TruckStock truckStocks = truckStockDao.getByBoxCodeAndHeaderId(truckStock);
		if (Func.isEmpty(truckStocks)) {
			truckStockDao.save(truckStock);
		}
	}

	@Override
	public List<TruckStock> findList(Long billHeaderId) {
		return truckStockDao.getList(billHeaderId);
	}
}
