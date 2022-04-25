package org.nodes.basics.carriers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.basics.carriers.impl.CarriersBizImpl;
import org.nodes.wms.dao.basics.carriers.dto.input.DeleteCarriersRequest;
import org.nodes.wms.dao.basics.carriers.dto.input.CarrierPageQuery;
import org.nodes.wms.dao.basics.carriers.dto.input.newCarrierRequest;
import org.nodes.wms.dao.basics.carriers.dto.output.CarrierResponse;
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
public class CarriersTest {
	@Autowired
	private CarriersBizImpl carriersBiz;
	@Test
	public void test01() throws ParseException {
		CarrierPageQuery carrierPageQuery = new CarrierPageQuery();
		Query query = new Query();
		query.setAscs("updateTime");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = simpleDateFormat.parse("2022-1-1");
		Date date2 =  simpleDateFormat.parse("2022-5-1");
		carrierPageQuery.setCreateTimeBegin(date1);
		carrierPageQuery.setCreateTimeEnd(date2);
		carrierPageQuery.setName("张三");
		IPage<CarrierResponse> cusPage = carriersBiz.getPage(query, carrierPageQuery);
		Assertions.assertEquals(1, cusPage.getTotal());
	}

	@Test
	public void test02() {
		newCarrierRequest newCarrierRequest = new newCarrierRequest();
		newCarrierRequest.setCode("12");
		newCarrierRequest.setName("王五");
		Assertions.assertEquals(true, carriersBiz.newCarrier(newCarrierRequest));
	}

	@Test
	public void test03() {
		DeleteCarriersRequest deleteRequest = new DeleteCarriersRequest();
		List list = new ArrayList<Long>();
		list.add(4l);
		list.add(5l);
		deleteRequest.setList(list);
		Assertions.assertEquals(true,carriersBiz.remove(deleteRequest));
	}

}
