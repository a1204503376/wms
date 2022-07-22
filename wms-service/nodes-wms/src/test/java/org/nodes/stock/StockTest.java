package org.nodes.stock;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;
import org.nodes.wms.dao.common.skuLot.BaseSkuLotEntity;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;

/**
 * 库存测试类
 **/
@RunWith(BladeSpringRunner.class)
@BladeBootTest(appName = "wms3.3-test", profile = "test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StockTest {


	@Test
	public void testStockQuery(){
		BaseSkuLot skuLot1 = new BaseSkuLot();
		skuLot1.setSkuLot1("test1");
		skuLot1.setSkuLot2("test2");

		BaseSkuLotEntity skuLot2 = new BaseSkuLotEntity();
		SkuLotUtil.setAllSkuLot(skuLot1, skuLot2);

		Assertions.assertEquals(skuLot2.getSkuLot1(), skuLot1.getSkuLot1());
		Assertions.assertEquals(skuLot2.getSkuLot2(), skuLot1.getSkuLot2());
	}
}
