package org.nodes.basics.suppliers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.basics.suppliers.impl.SupplierBizImpl;
import org.nodes.wms.dao.basics.suppliers.dto.input.AddOrEditSupplierRequest;
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
		boolean deleteBoolean = supplierBiz.removeByIdList(ids);
		Assertions.assertTrue(deleteBoolean);
	}

	@Test
	public void insertTest() {
		Random random = new Random();
		AddOrEditSupplierRequest addOrEditSupplierRequest = new AddOrEditSupplierRequest();
		addOrEditSupplierRequest.setCode(System.nanoTime() + random.nextInt() + "");
//		supplierRequest.setCode("375410144335001");
		addOrEditSupplierRequest.setName("供应商ZZZZ001");
		addOrEditSupplierRequest.setRemark("无DDDDWWWW");
		addOrEditSupplierRequest.setStatus(0);
		addOrEditSupplierRequest.setSimpleName("001");
		addOrEditSupplierRequest.setWoId(4000000001213L);
		supplierBiz.newSupplier(addOrEditSupplierRequest);
		//Assertions.assertTrue(saveBoolean);

	}

	@Test
	public void saveTest() {
		Random random = new Random();
		AddOrEditSupplierRequest addOrEditSupplierRequest = new AddOrEditSupplierRequest();
		addOrEditSupplierRequest.setId(1536637391215792129L);
		addOrEditSupplierRequest.setCode(System.nanoTime() + random.nextInt() + "");
		addOrEditSupplierRequest.setName("供应商ABCDEFG");
		addOrEditSupplierRequest.setRemark("0614新增或修改接口测试-修改");
		addOrEditSupplierRequest.setStatus(1);
		addOrEditSupplierRequest.setSimpleName("ABCDEFG");
		addOrEditSupplierRequest.setWoId(1530073756668657666L);
		supplierBiz.save(addOrEditSupplierRequest);
		//Assertions.assertTrue(saveBoolean);

	}
}

