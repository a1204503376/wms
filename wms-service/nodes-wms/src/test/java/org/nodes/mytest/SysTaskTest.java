package org.nodes.mytest;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.dao.crontab.entity.CrontabTask;
import org.nodes.wms.biz.crontab.ICrontabTaskService;
import org.nodes.wms.core.timed.SysTaskController;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(BladeSpringRunner.class)
@BladeBootTest(appName = "wms3.3-test", profile = "test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SysTaskTest {
	@Resource
	private ICrontabTaskService CrontabTaskService;
	@Resource
	private SysTaskController sysTaskController;

	@Test
	public void test01() {
		List<CrontabTask> list = CrontabTaskService.getCrontabTaskList();
		sysTaskController.cancel(list.get(0).getId());
	}

}
