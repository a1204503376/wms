package org.nodes.wms.core.outstock.loading.wrapper;

/**
 * 装车日志封装类
 */

import org.nodes.wms.core.outstock.loading.entity.SoTruckHeader;
import org.nodes.wms.core.outstock.loading.entity.TruckSerial;
import org.nodes.wms.core.outstock.loading.service.ISoTruckHeaderService;
import org.nodes.wms.core.outstock.loading.vo.TruckSerialVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;

/**
 * @author pengwei
 * @program WmsCore
 * @description 装车日志封装类
 * @create 20200410
 */
public class TruckSerialWrapper extends BaseEntityWrapper<TruckSerial, TruckSerialVO> {

	private static ISoTruckHeaderService soTruckHeaderService;

	static {
		soTruckHeaderService = SpringUtil.getBean(ISoTruckHeaderService.class);
	}

	public static TruckSerialWrapper build(){
		return new TruckSerialWrapper();
	}

	@Override
	public TruckSerialVO entityVO(TruckSerial entity) {
		TruckSerialVO truckSerialVO = BeanUtil.copy(entity, TruckSerialVO.class);
		if (ObjectUtil.isNotEmpty(truckSerialVO)) {
			// 车次信息
			SoTruckHeader soTruckHeader = soTruckHeaderService.getById(entity.getTruckId());
			if (ObjectUtil.isNotEmpty(soTruckHeader)) {
				truckSerialVO.setTruckCode(soTruckHeader.getTruckCode());
			}
			// 库房
			Warehouse warehouse = WarehouseCache.getById(entity.getWhId());
			if (ObjectUtil.isNotEmpty(warehouse)) {
				truckSerialVO.setWhName(warehouse.getWhName());
			}
		}
		return truckSerialVO;
	}
}
