package org.nodes.mytest;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.task.AgvTask;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试
 **/
@RunWith(BladeSpringRunner.class)
@BladeBootTest(appName = "wms3.3-test", profile = "test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AgvTaskTest {
	@Autowired
	private AgvTask agvTask;

	@Test
	public void test1(){
		List<WmsTask> putwayTask = new ArrayList<>();
		WmsTask wmsTask = new WmsTask();
		wmsTask.setTaskId(123L);
		wmsTask.setBillId(202208031028L);
		wmsTask.setBillDetailId(20220803102801L);
		wmsTask.setTaskState(WmsTaskStateEnum.ABNORMAL);
		putwayTask.add(wmsTask);
//		agvTask.sendToSchedule(putwayTask);
	}
}
