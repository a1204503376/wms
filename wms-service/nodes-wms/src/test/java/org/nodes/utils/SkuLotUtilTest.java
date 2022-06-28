package org.nodes.utils;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;
import org.nodes.wms.dao.common.skuLot.BaseSkuLotEntity;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;

@RunWith(BladeSpringRunner.class)
@BladeBootTest(appName = "wms3.3-test", profile = "test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SkuLotUtilTest {

	@Test
	public void testSetAllSkuLot(){
		BaseSkuLot skuLot1 = new BaseSkuLot();
		skuLot1.setSkuLot1("test1");
		skuLot1.setSkuLot2("test2");

		BaseSkuLotEntity skuLot2 = new BaseSkuLotEntity();
		SkuLotUtil.setAllSkuLot(skuLot1, skuLot2);

		Assertions.assertEquals(skuLot2.getSkuLot1(), skuLot1.getSkuLot1());
		Assertions.assertEquals(skuLot2.getSkuLot2(), skuLot1.getSkuLot2());

		Assertions.assertTrue(SkuLotUtil.compareAllSkuLot(skuLot1, skuLot2));

		// 随便一个对象比较
		ReceiveHeader header = new ReceiveHeader();
		SkuLotUtil.setAllSkuLot(skuLot1, header);
		Assertions.assertFalse(SkuLotUtil.compareAllSkuLot(skuLot1, header));
	}
}
