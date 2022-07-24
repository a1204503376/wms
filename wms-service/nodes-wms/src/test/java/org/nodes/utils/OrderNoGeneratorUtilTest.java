package org.nodes.utils;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.basics.lpntype.LpnTypeBiz;
import org.nodes.wms.biz.common.utils.OrderNoGeneratorUtil;
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
public class OrderNoGeneratorUtilTest {

	@Autowired
	private OrderNoGeneratorUtil orderNoGeneratorUtil;
	@Autowired
	private LpnTypeBiz lpnTypeBiz;

	@Test
	public void documentCodeUtilTest() {
		System.out.println(orderNoGeneratorUtil.createAsnBillNo());
		System.out.println(orderNoGeneratorUtil.createAsnBillNo());
		System.out.println(orderNoGeneratorUtil.createAsnBillNo());
		System.out.println(orderNoGeneratorUtil.createAsnBillNo());
		System.out.println(orderNoGeneratorUtil.createAsnBillNo());
		System.out.println(orderNoGeneratorUtil.createReceiveBillNo());
		System.out.println(orderNoGeneratorUtil.createSaleBillNo());
		System.out.println(orderNoGeneratorUtil.createSoBillNo());
		System.out.println(orderNoGeneratorUtil.createShipBillNo());
		System.out.println(lpnTypeBiz.generateLpnCode("A"));
		System.out.println(lpnTypeBiz.generateLpnCode("B"));
		System.out.println(lpnTypeBiz.generateLpnCode("C"));
		System.out.println(lpnTypeBiz.generateLpnCode("D"));
	}

	@Test
	public void AesUtilTest() {
		String key = AesUtil.genAesKey();
		System.out.println(key);
	}
}
