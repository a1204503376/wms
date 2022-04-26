package org.nodes.receive.header;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.biz.receive.header.impl.ReceiveBizImpl;
import org.nodes.wms.dao.receive.header.dto.input.HeaderPageQuery;
import org.nodes.wms.dao.receive.header.dto.output.HeaderDetailResponse;
import org.nodes.wms.dao.receive.header.dto.output.HeaderResponse;
import org.springblade.core.mp.support.Query;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(BladeSpringRunner.class)
@BladeBootTest(appName = "wms3.3-test", profile = "test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HeaderTest {
	@Autowired
	private ReceiveBizImpl receiveBiz;

	@Test
	public void test01() throws ParseException {
		HeaderPageQuery headerPageQuery = new HeaderPageQuery();
		Query query = new Query();
		query.setAscs("updateTime");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = simpleDateFormat.parse("2022-1-1");
		Date date2 =  simpleDateFormat.parse("2022-5-1");
		headerPageQuery.setSkuCode("4");
		headerPageQuery.setCreateTimeBegin(date1);
		headerPageQuery.setCreateTimeEnd(date2);
		IPage<HeaderResponse> cusPage = receiveBiz.getPage(headerPageQuery,query);
		//Assertions.assertEquals(1, cusPage.getTotal());
	}

	@Test
	public void test02() throws ParseException {
		 HeaderDetailResponse headerDetailResponse = receiveBiz.detail(1l);
		 HeaderResponse headerResponse = headerDetailResponse.getHeaderResponse();

        System.out.println(headerResponse);
        System.out.println(headerDetailResponse.getDetailList().size());

		//Assertions.assertEquals(1, cusPage.getTotal());
	}



}
