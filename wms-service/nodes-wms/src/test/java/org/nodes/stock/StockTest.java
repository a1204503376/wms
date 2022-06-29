package org.nodes.stock;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.dao.stock.entities.Serial;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 库存测试类
 **/
@RunWith(BladeSpringRunner.class)
@BladeBootTest(appName = "wms3.3-test", profile = "test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StockTest {

	@Autowired
	private StockBiz stockBiz;

	@Test
	public void findSerialBySerialNoTest(){
		List<String> serialList = new ArrayList<>();
		serialList.add("100");
		serialList.add("200");
		serialList.add("4000");
		List<Serial> serialListResult = stockBiz.findSerialBySerialNo(serialList);
	}
}
