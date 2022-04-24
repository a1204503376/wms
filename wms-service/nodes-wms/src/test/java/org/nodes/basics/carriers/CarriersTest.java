package org.nodes.basics.carriers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.basics.carriers.impl.CarriersBizImpl;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersDeleteRequest;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersPageQuery;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersRequest;
import org.nodes.wms.dao.basics.carriers.dto.output.CarriersResponse;
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
public class CarriersTest {
	@Autowired
	private CarriersBizImpl carriersBiz;

	@Test
	public void test01() {
		CarriersPageQuery carriersPageQuery = new CarriersPageQuery();
		Query query = new Query();
		query.setAscs("updateTime");
		IPage<CarriersResponse> page = Condition.getPage(query);
		carriersPageQuery.setName("王五");
		carriersPageQuery.setCreateTimeBegin("2022-1-1");
		carriersPageQuery.setCreateTimeEnd("2022-5-1");
		IPage<CarriersResponse> cusPage = carriersBiz.page(page,carriersPageQuery);
		Assertions.assertEquals(1, cusPage.getTotal());
	}

	@Test
	public void test02() {
		CarriersRequest carriersRequest = new CarriersRequest();
		carriersRequest.setName("张三");
		carriersRequest.setCode("11");
		int count = carriersBiz.save(carriersRequest);
		Assertions.assertEquals(1, count);

	}

	@Test
	public void test03() {
		CarriersDeleteRequest deleteRequest = new CarriersDeleteRequest();
		List list = new ArrayList();
		list.add(4);
		list.add(5);
		deleteRequest.setList(list);
		int count = carriersBiz.delete(deleteRequest);
		Assertions.assertEquals(2, count);
	}

	@Test
	public void test04() {
		CarriersRequest carriersRequest = new CarriersRequest();
		carriersRequest.setId(5l);
		carriersRequest.setCode("14");
		carriersRequest.setName("历史");
		int count = carriersBiz.update(carriersRequest);
		Assertions.assertEquals(1, count);
	}
}
