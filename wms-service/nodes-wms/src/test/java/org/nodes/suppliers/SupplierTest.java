package org.nodes.suppliers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.basics.suppliers.impl.SupplierBizImpl;
import org.nodes.wms.dao.basics.suppliers.dto.input.AddSupplierRequest;
import org.nodes.wms.dao.basics.suppliers.dto.input.SupplierPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierPageResponse;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 供应商单元测试
 *
 * @author 彭永程
 * @date 2022-04-21 9:54
 **/
@RunWith(BladeSpringRunner.class)
@BladeBootTest(appName = "wms3.3-test", profile = "test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SupplierTest {

	@Autowired
	private SupplierBizImpl supplierBiz;

	@Test
	public void selectTest() throws ParseException {
		SupplierPageQuery supplierPageQuery = new SupplierPageQuery();
//		supplierPageQuery.setCode("123");
		supplierPageQuery.setName("001");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		supplierPageQuery.setCreateTimeBegin(sdf.parse("2022-04-21 15:00:00"));
//		OrderItem orderItem = new OrderItem();
//		orderItem.setColumn("id");
//		orderItem.setAsc(false);
//		page.orders().add(orderItem);
		Query query = new Query();
		query.setAscs("id");
		IPage<Object> page = Condition.getPage(query);
//		IPage<?> page = new Page<>(1, 10);

		Page<SupplierPageResponse> pageSupplier = supplierBiz.getPage(page, supplierPageQuery);
		System.out.println(pageSupplier.getRecords());
		Assertions.assertEquals(1, pageSupplier.getTotal());
	}

	@Test
	public void deleteByBatchIdTest() {
		List<Long> ids = new ArrayList<>();
//		ids.add(1517056812761829377L);
//		ids.add(1517042468103733250L);
		Boolean deleteBoolean = supplierBiz.removeByIds(ids);
		Assertions.assertTrue(deleteBoolean);
	}

	@Test
	public void saveTest() {
		Random random = new Random();
		AddSupplierRequest addSupplierRequest = new AddSupplierRequest();
		addSupplierRequest.setCode(System.nanoTime() + random.nextInt() + "");
//		supplierRequest.setCode("375410144335001");
		addSupplierRequest.setName("供应商ZZZZ001");
		addSupplierRequest.setRemark("无DDDDWWWW");
		addSupplierRequest.setStatus(0);
		addSupplierRequest.setSimpleName("001");
		addSupplierRequest.setWoId(4000000001213L);
		Boolean saveBoolean = supplierBiz.newSupplier(addSupplierRequest);
		Assertions.assertTrue(saveBoolean);

	}
}

