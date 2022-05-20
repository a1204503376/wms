package org.nodes.basics.sku;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.basics.sku.impl.SkuBizImpl;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageAggregate;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 物品测试类
 **/
@RunWith(BladeSpringRunner.class)
@BladeBootTest(appName = "wms3.3-test", profile = "test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SkuTest {
	@Autowired
	private SkuBizImpl skuBiz;

	@Test
	public void find(){
		SkuPackageAggregate skuPackageAggregate = skuBiz.findSkuPackageAggregateBySkuId(14L);
		System.out.println(skuPackageAggregate);
	}
}
