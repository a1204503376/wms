package org.nodes.asn;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.application.AsnManageBiz;
import org.nodes.wms.biz.instock.asn.impl.AsnBizImpl;
import org.nodes.wms.dao.instock.asn.dto.input.AddOrEditAsnBillRequest;
import org.nodes.wms.dao.instock.asn.dto.input.AsnDetailRequest;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.AsnBillViewResponse;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * ASN单模块测试类
 */
@RunWith(BladeSpringRunner.class)
@BladeBootTest(appName = "wms3.3-test", profile = "test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AsnTest {

	@Autowired
	private AsnBizImpl asnBiz;

	@Autowired
	private AsnManageBiz asnManageBiz;

	@Test
	public void selectTest() throws ParseException {
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
//		pageParamsQuery.setWhIdList("库房编码002");
		pageParamsQuery.setSupplier("供应商");

		Query query = new Query();
		query.setDescs("asn_bill_id");
		IPage<PageResponse> asnPage = asnBiz.getPageAsnBill(Condition.getPage(query), pageParamsQuery);

		System.out.println(asnPage.getRecords());
		Assertions.assertEquals(5, asnPage.getTotal());

	}

	@Test
	public void findAsnBillViewDetailByIdTest(){
		AsnBillViewResponse asnBillViewResponse = asnBiz.findAsnBillViewDetailByAsnBillId(1527561445477265410L);
//		System.out.println(asnDetailResponse);
//		Assertions.assertEquals(1,asnDetailResponse);
	}

	@Test
	public void deleteTest(){
		List<Long> idList = new ArrayList<>();
		idList.add(1234568L);
		asnManageBiz.remove(idList);
	}

	@Test
	public void insertTest(){
		AddOrEditAsnBillRequest addOrEditAsnBillRequest = new AddOrEditAsnBillRequest();
		for (int i = 0; i < 99; i++) {
			addOrEditAsnBillRequest.setBillTypeCd("10");
			addOrEditAsnBillRequest.setSupplierId(2L);
			addOrEditAsnBillRequest.setWhId(1L);
			addOrEditAsnBillRequest.setAsnBillRemark("ASN头备注");

			List<AsnDetailRequest> list = new ArrayList<>();
			for (int j = 0; j < 2; j++) {
				AsnDetailRequest asnDetailRequest = new AsnDetailRequest();
				asnDetailRequest.setAsnLineNo("10");
				asnDetailRequest.setRemark("明细备注");
				asnDetailRequest.setPlanQty(new BigDecimal(0));
				asnDetailRequest.setSkuId(1464200583655456770L);
				asnDetailRequest.setUmCode("WF");
				list.add(asnDetailRequest);
			}
			addOrEditAsnBillRequest.setAsnDetailList(list);
			asnBiz.add(addOrEditAsnBillRequest);
		}
	}
}
