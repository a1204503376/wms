package org.nodes.utils;


import lombok.AllArgsConstructor;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.common.utils.NoGeneratorUtil;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;
import org.springframework.beans.factory.annotation.Autowired;



/**
 * 供应商单元测试
 *
 * @author 王智勇
 * @date 2022-04-22 14:53
 **/
@RunWith(BladeSpringRunner.class)
@BladeBootTest(appName = "wms3.3-test", profile = "test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AllArgsConstructor
public class NoGeneratorUtilTest {

	private NoGeneratorUtil noGeneratorUtil;

	@Test
	public void documentCodeUtilTest() {
		System.out.println(noGeneratorUtil.createAsnBillNo());
		System.out.println(noGeneratorUtil.createReceiveNo());
		System.out.println(noGeneratorUtil.saleDocumentCode());
		System.out.println(noGeneratorUtil.soDocumentCode());
		System.out.println(noGeneratorUtil.shipDocumentCode());
	}
}
