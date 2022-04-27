package org.nodes.basics.customers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.basics.customers.impl.CustomersBizImpl;
import org.nodes.wms.dao.basics.customers.dto.input.CustomerPageQuery;
import org.nodes.wms.dao.basics.customers.dto.input.newCustomerRequest;
import org.nodes.wms.dao.basics.customers.dto.input.DeleteCustomerRequest;
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
		CustomerPageQuery customerPageQuery = new CustomerPageQuery();
        Query query = new Query();
        query.setAscs("updateTime");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = simpleDateFormat.parse("2022-1-1");
		Date date2 =  simpleDateFormat.parse("2022-5-1");
		customerPageQuery.setCreateTimeBegin(date1);
		customerPageQuery.setCreateTimeEnd(date2);
		IPage<CustomersResponse> cusPage = customersBiz.getPage(customerPageQuery,query);
		Assertions.assertEquals(8, cusPage.getTotal());
	}

	@Test
	public void test02() {
		newCustomerRequest newCustomerRequest = new newCustomerRequest();
		newCustomerRequest.setCode("12");
		newCustomerRequest.setName("历史");
		Assertions.assertEquals(true, customersBiz.newCustomers(newCustomerRequest));
	}

	@Test
	public void test03() {
		DeleteCustomerRequest deleteRequest = new DeleteCustomerRequest();
		List<Long> list = new ArrayList<Long>();
		list.add(4l);
		list.add(5l);
		deleteRequest.setList(list);
		Assertions.assertEquals(true,customersBiz.remove(deleteRequest));
	}


}
