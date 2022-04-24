package org.nodes.basics.customers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.basics.customers.impl.CustomersBizImpl;
import org.nodes.wms.dao.basics.customers.dto.input.CustomersPageQuery;
import org.nodes.wms.dao.basics.customers.dto.input.CustomersRequest;
import org.nodes.wms.dao.basics.customers.dto.input.DeleteCustomersRequest;
import org.nodes.wms.dao.basics.customers.dto.output.CustomersResponse;
import org.springblade.core.mp.support.Query;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(BladeSpringRunner.class)
@BladeBootTest(appName = "wms3.3-test", profile = "test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomersTest {
	@Autowired
	private CustomersBizImpl customersBiz;

	@Test
	public void test01() throws ParseException {
		CustomersPageQuery customersPageQuery = new CustomersPageQuery();
        Query query = new Query();
        query.setAscs("updateTime");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = simpleDateFormat.parse("2022-1-1");
		Date date2 =  simpleDateFormat.parse("2022-5-1");
		customersPageQuery.setCreateTimeBegin(date1);
		customersPageQuery.setCreateTimeEnd(date2);
		IPage<CustomersResponse> cusPage = customersBiz.getPage(query,customersPageQuery);
		Assertions.assertEquals(8, cusPage.getTotal());
	}

	@Test
	public void test02() {
		CustomersRequest customersRequest = new CustomersRequest();
		customersRequest.setCode("12");
		customersRequest.setName("历史");
		Assertions.assertEquals(true, customersBiz.saveCustomers(customersRequest));
	}

	@Test
	public void test03() {
		DeleteCustomersRequest deleteRequest = new DeleteCustomersRequest();
		List list = new ArrayList<Long>();
		list.add(4l);
		list.add(5l);
		deleteRequest.setList(list);
		Assertions.assertEquals(true,customersBiz.remove(deleteRequest));
	}


}
