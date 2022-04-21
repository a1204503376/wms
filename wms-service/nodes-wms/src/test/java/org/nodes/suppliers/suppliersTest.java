package org.nodes.suppliers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.basics.suppliers.impl.SuppliersBizImpl;
import org.nodes.wms.dao.basics.suppliers.dto.input.DeleteSuppliersRequest;
import org.nodes.wms.dao.basics.suppliers.dto.input.SuppliersPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.input.SuppliersRequest;
import org.nodes.wms.dao.basics.suppliers.dto.output.SuppliersPageResponse;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class suppliersTest {

	@Autowired
	private SuppliersBizImpl suppliersBiz;

	@Test
	public void selectTest() throws ParseException {
		SuppliersPageQuery suppliersPageQuery = new SuppliersPageQuery();
//		suppliersPageQuery.setCode("123");
		suppliersPageQuery.setName("001");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		suppliersPageQuery.setCreateTimeBegin(sdf.parse("2022-04-21 15:00:00"));
//		OrderItem orderItem = new OrderItem();
//		orderItem.setColumn("id");
//		orderItem.setAsc(false);
//		page.orders().add(orderItem);
		Query query = new Query();
		query.setAscs("id");
		IPage<Object> page = Condition.getPage(query);
//		IPage<?> page = new Page<>(1, 10);

		Page<SuppliersPageResponse> pageSuppliers = suppliersBiz.getPageSuppliers(page, suppliersPageQuery);
		System.out.println(pageSuppliers.getRecords());
		Assertions.assertEquals(1, pageSuppliers.getTotal());
	}

	@Test
	public void deleteByBatchIdTest(){
		Long[] ids = {1517042468103733249L,1517042468103733250L};
		DeleteSuppliersRequest deleteSuppliersRequest = new DeleteSuppliersRequest();
		deleteSuppliersRequest.setIds(ids);
		Boolean deleteBoolean = suppliersBiz.removeSuppliersByIds(deleteSuppliersRequest);
		Assertions.assertTrue(deleteBoolean);
	}

	@Test
	public void  saveTest(){
		Random random = new Random();
		SuppliersRequest suppliersRequest = new SuppliersRequest();
		suppliersRequest.setCode(System.nanoTime()+random.nextInt()+"");
		suppliersRequest.setCreateDept(50124324543L);
		suppliersRequest.setName("供应商ZZZZ001");
		suppliersRequest.setRemark("无DDDDWWWW");
		suppliersRequest.setStatus(0);
		suppliersRequest.setCreateUser(60L);
		suppliersRequest.setSimpleName("001");
		suppliersRequest.setTenantId(3000000000L);
		suppliersRequest.setUpdateUser(60000000000L);
		suppliersRequest.setWoId(4000000001213L);
		suppliersRequest.setIsDeleted(0);
		Boolean saveBoolean = suppliersBiz.saveSuppliers(suppliersRequest);
		Assertions.assertTrue(saveBoolean);

	}

	@Test
	public void updateTest(){
		SuppliersRequest suppliersRequest = new SuppliersRequest();
		suppliersRequest.setId(1517049840066936834L);
		suppliersRequest.setCode("BBBBBBBB100");
		suppliersRequest.setCreateDept(99999999L);
		suppliersRequest.setName("供应商B432001");
		suppliersRequest.setRemark("无芜湖");
		suppliersRequest.setStatus(0);
		suppliersRequest.setCreateUser(66666666L);
		suppliersRequest.setSimpleName("001");
		suppliersRequest.setTenantId(33333333L);
		suppliersRequest.setUpdateUser(6666600000L);
		suppliersRequest.setWoId(404040404044L);
		Boolean updateBealean = suppliersBiz.updateSuppliersById(suppliersRequest);
		Assertions.assertTrue(updateBealean);
	}
}
