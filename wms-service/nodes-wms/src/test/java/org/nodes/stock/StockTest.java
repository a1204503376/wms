package org.nodes.stock;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 库存测试类
 **/
@RunWith(BladeSpringRunner.class)
@BladeBootTest(appName = "wms3.3-test", profile = "test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StockTest {

	@Autowired
	private StockQueryBiz stockQueryBiz;

	@Test
	public void testStockQuery() {
		Long whId = 1540154178775576578L;
		Long skuId = 1544937646084644866L;
		SkuLotBaseEntity skuLot = new SkuLotBaseEntity();
		skuLot.setSkuLot1("190904");
		List<Stock> stock = stockQueryBiz.findEnableStockByLocation(whId, skuId, null, null, skuLot);
		Assertions.assertNotNull(stock);
	}
}
