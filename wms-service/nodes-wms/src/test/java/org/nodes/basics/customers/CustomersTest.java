package org.nodes.basics.customers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.velocity.util.ArrayListWrapper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.basics.customers.impl.CustomersBizImpl;
import org.nodes.wms.biz.instock.asn.impl.AsnBizImpl;
import org.nodes.wms.dao.basics.customers.dto.input.CustomersPageQuery;
import org.nodes.wms.dao.basics.customers.dto.input.CustomersRequest;
import org.nodes.wms.dao.basics.customers.dto.input.DeleteRequest;
import org.nodes.wms.dao.basics.customers.dto.output.CustomersResponse;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@RunWith(BladeSpringRunner.class)
@BladeBootTest(appName = "wms3.3-test", profile = "test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomersTest {
	@Autowired
	private CustomersBizImpl customersBiz;

	@Test
	public void test01() {
		CustomersPageQuery customersPageQuery = new CustomersPageQuery();
        Query query = new Query();
        query.setAscs("updateTime");
		IPage<CustomersResponse> page = Condition.getPage(query);
		customersPageQuery.setCreateTimeBegin("2022-1-1");
		customersPageQuery.setCreateTimeEnd("2022-5-1");
		IPage<CustomersResponse> cusPage = customersBiz.page(page,customersPageQuery);
		Assertions.assertEquals(4, cusPage.getTotal());
	}

	@Test
	public void test02() {
		CustomersRequest customersRequest = new CustomersRequest();
		customersRequest.setCode("114");
		customersRequest.setName("历史");
		int count = customersBiz.save(customersRequest);
		Assertions.assertEquals(1, count);

	}

	@Test
	public void test03() {
		DeleteRequest deleteRequest = new DeleteRequest();
		List list = new ArrayList();
		list.add(4);
		list.add(5);
		deleteRequest.setList(list);
		int count = customersBiz.delete(deleteRequest);
		Assertions.assertEquals(1, count);
	}

	@Test
	public void test04() {
		CustomersRequest customersRequest = new CustomersRequest();
		customersRequest.setId(5l);
		customersRequest.setCode("14");
		customersRequest.setName("历史");
		int count = customersBiz.update(customersRequest);
		Assertions.assertEquals(1, count);
	}
}
