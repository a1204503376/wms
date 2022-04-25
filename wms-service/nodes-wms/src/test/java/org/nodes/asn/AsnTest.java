package org.nodes.asn;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.instock.asn.impl.AsnBizImpl;
import org.nodes.wms.dao.instock.asn.dto.input.DeleteRequest;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailResponse;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
	public void test022() throws ParseException {
		IPage<?> page = new Page<>(1, 10);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		PageParamsQuery pageParamsQuery = new PageParamsQuery();
//		pageParamsQuery.setAsnBillNo("A001");
//		pageParamsQuery.setAsnBillState(new String[]{"10","20","3"});
//		pageParamsQuery.setSkuCode("wp001");
//		pageParamsQuery.setCreateTimeBegin(sdf.parse("2022-04-22"));
//		pageParamsQuery.setCreateTimeEnd(sdf.parse("2022-04-28"));
//		pageParamsQuery.setExternalOrderNo("2222223");
//		pageParamsQuery.setExternalCreateUser("管理员4");
		pageParamsQuery.setWhCode("库房编码002");
		pageParamsQuery.setSuppliers("供应商");

		Query query = new Query();
		query.setDescs("asn_bill_id");
		IPage<PageResponse> asnPage = asnBiz.getPageAsnBill(Condition.getPage(query), pageParamsQuery);

		System.out.println(asnPage.getRecords());
		Assertions.assertEquals(5, asnPage.getTotal());

	}

	@Test
	public void getAsnDetailByIdTest(){
		DeleteRequest deleteRequest = new DeleteRequest();
		List<Long> idList = new ArrayList<>();
		idList.add(123456L);
		deleteRequest.setAsnBillId(idList);
		AsnDetailResponse asnDetailResponse = asnBiz.getAsnDetail(deleteRequest);
		System.out.println(asnDetailResponse);
//		Assertions.assertEquals(1,asnDetailResponse);
	}
}
