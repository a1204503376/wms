package org.nodes;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.instock.receiveLog.ReceiveLogBiz;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 收货测试类
 **/
@RunWith(BladeSpringRunner.class)
@BladeBootTest(appName = "wms3.3-test", profile = "test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReceiveTest {
	@Autowired
	private ReceiveLogBiz receiveLogBiz;

	@Test
	public void revokeTest(){
		List<Long> list = new ArrayList<>();
		list.add(1542461428042588161L);
		list.add(1542460942849695746L);
		list.add(1542459626035380226L);
		list.add(1542322509787480065L);
		list.add(1542319211659444226L);
		list.add(1542315575902519298L);
		list.add(1542460979583410177L);
		list.add(1542459266365423618L);
		//receiveLogBiz.cancelReceive(list);
	}
}
