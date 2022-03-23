
package org.nodes.wms.core.outstock.loading.service;

import org.nodes.wms.core.outstock.loading.entity.SoTruckDetail;
import org.nodes.wms.core.outstock.loading.vo.SoTruckDetailVO;
import org.springblade.core.mp.base.BaseService;

/**
 * 车次明细 服务类
 *
 * @author chz
 * @since 2020-03-03
 */
public interface ISoTruckDetailService extends BaseService<SoTruckDetail> {

	/**
	 * 车次明细和发货信息详情
	 *
	 * @param truckId
	 * @return
	 */
	SoTruckDetailVO getStruckDetail(String truckId);

}
