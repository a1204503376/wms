package org.nodes.utils;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.common.utils.NoGeneratorUtil;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;
import org.springblade.core.tool.utils.AesUtil;
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
public class NoGeneratorUtilTest {

	@Autowired
	private NoGeneratorUtil noGeneratorUtil;

	@Test
	public void documentCodeUtilTest() {
		System.out.println(noGeneratorUtil.createAsnBillNo());
		System.out.println(noGeneratorUtil.createAsnBillNo());
		System.out.println(noGeneratorUtil.createAsnBillNo());
		System.out.println(noGeneratorUtil.createAsnBillNo());
		System.out.println(noGeneratorUtil.createReceiveBillNo());
		System.out.println(noGeneratorUtil.createSaleBillNo());
		System.out.println(noGeneratorUtil.createSoBillNo());
		System.out.println(noGeneratorUtil.createShipBillNo());
	}

	@Test
	public void AesUtilTest() {
		String key = AesUtil.genAesKey();
		System.out.println(key);
	}
}
