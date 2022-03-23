package org.nodes.mytest;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;

import java.math.BigDecimal;

@RunWith(BladeSpringRunner.class)
@BladeBootTest(appName = "blade-runner", profile = "test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MyTest {

	@Test
	public void mytest(){
		String convert1 = SkuPackageDetailCache.convert1(1391672962916241410L, 10, new BigDecimal(230),false);
		System.out.println(convert1);
	}

}
