package org.nodes.wms.dao.truck;

import org.nodes.wms.dao.truck.entities.TruckStock;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 发运记录DAO
 *
 * @author nodesc
 */
public interface TruckStockDao extends BaseService<TruckStock> {
	TruckStock getByBoxCodeAndHeaderId(TruckStock truckStock);
	List<TruckStock> getList(Long billHeaderId);
}
