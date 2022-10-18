package org.nodes.wms.biz.tuck;

import org.nodes.wms.dao.truck.entities.TruckStock;

import java.util.List;

public interface TruckStockBiz {
	void insert(TruckStock truckStock);
	List<TruckStock> findList(Long billHeaderId);
}
