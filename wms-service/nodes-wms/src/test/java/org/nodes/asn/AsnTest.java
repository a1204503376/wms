package org.nodes.asn;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.instock.asn.impl.AsnBizImpl;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@RunWith(BladeSpringRunner.class)
@BladeBootTest(appName = "wms3.3-test", profile = "test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AsnTest {

	@Autowired
	private AsnBizImpl asnBiz;

	@Test
	public void test022() {
		IPage<?> page = new Page<>(1, 10);

		PageParamsQuery pageParamsQuery = new PageParamsQuery();
		pageParamsQuery.setAsnBillNo("DML");
		pageParamsQuery.setSkuCode("abc");

		IPage<PageResponse> asnPage = asnBiz.getAsnPageForWrapper(page, pageParamsQuery);
		System.out.println(asnPage.getRecords());
		Assertions.assertEquals(1, asnPage.getTotal());

	}

	@Test
	public void test02() {
		IPage<?> page = new Page<>(1, 10);
		PageParamsQuery pageParamsQuery = new PageParamsQuery();
		pageParamsQuery.setAsnBillNo("DML1");
		pageParamsQuery.setSkuCode("abc1");

		IPage<PageResponse> asnPage = asnBiz.getAsnPage(page, pageParamsQuery);
		System.out.println(asnPage.getRecords());
		Assertions.assertEquals(1, asnPage.getTotal());

	}
}
