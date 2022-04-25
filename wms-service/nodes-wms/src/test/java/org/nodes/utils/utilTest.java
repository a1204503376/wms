package org.nodes.utils;


import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.common.utils.DocumentCodeUtil;
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

public class utilTest {
	@Autowired
	private DocumentCodeUtil documentCodeUtil;

	@Test
	public void documentCodeUtilTest() {
		System.out.println(documentCodeUtil.asnDocumentCode());
		System.out.println(documentCodeUtil.receiveDocumentCode());
		System.out.println(documentCodeUtil.saleDocumentCode());
		System.out.println(documentCodeUtil.soDocumentCode());
		System.out.println(documentCodeUtil.shipDocumentCode());
	}
}
