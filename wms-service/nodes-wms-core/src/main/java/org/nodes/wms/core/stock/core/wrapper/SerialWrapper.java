package org.nodes.wms.core.stock.core.wrapper;

import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.stock.core.entity.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.entity.StockDetail;
import org.nodes.wms.core.stock.core.vo.SerialVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;

/**
 * 序列号封装类
 *
 * @Author zx
 * @Date 2020/2/21
 **/
public class SerialWrapper extends BaseEntityWrapper<Serial, SerialVO> {

	public static SerialWrapper build() {
		return new SerialWrapper();
	}

	public Serial entity(String serialNumber, Stock stock, StockDetail stockDetail, Long systemProcId) {
		Serial serial = new Serial();
		serial.setStockId(stock.getStockId());
		serial.setStockDetailId(stockDetail.getId());
		serial.setWhId(stock.getWhId());
		serial.setSerialNumber(serialNumber);
		serial.setSkuId(stock.getSkuId());
		Sku sku = SkuCache.getById(stock.getSkuId());
		if (Func.isNotEmpty(sku)) {
			serial.setSkuCode(sku.getSkuCode());
			serial.setSkuName(sku.getSkuName());
		}
		serial.setLotNumber(stock.getLotNumber());
		serial.setSystemProcId(systemProcId);
		return serial;
	}

	@Override
	public SerialVO entityVO(Serial entity) {
		SerialVO serialVO = BeanUtil.copy(entity, SerialVO.class);
		if (Func.isNotEmpty(serialVO)) {
			Warehouse warehouse = WarehouseCache.getById(entity.getWhId());
			if (Func.isNotEmpty(warehouse)) {
				serialVO.setWhName(warehouse.getWhName());
			}
		}
		return serialVO;
	}
}
