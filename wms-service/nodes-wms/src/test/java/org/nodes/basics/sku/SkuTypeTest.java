package org.nodes.basics.sku;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.basics.skuType.impl.SkuTypeBizImpl;
import org.nodes.wms.dao.basics.skuType.dto.input.SkuTypeAddOrEditRequest;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * 物品分类测试类
 **/
@RunWith(BladeSpringRunner.class)
@BladeBootTest(appName = "wms3.3-test", profile = "test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SkuTypeTest {

	@Autowired
	private SkuTypeBizImpl skuTypeBiz;

	@Test
	public void saveTest(){
		SkuTypeAddOrEditRequest skuTypeAddOrEditRequest = new SkuTypeAddOrEditRequest();
		skuTypeAddOrEditRequest.setSkuTypeId(1537338598557233154L);
		skuTypeAddOrEditRequest.setTypeCode("10007");
		skuTypeAddOrEditRequest.setTypeName("10005测试");
		skuTypeAddOrEditRequest.setWoId(1530072081803378690L);
		skuTypeAddOrEditRequest.setTypePreId(1537338045211095041L);
		skuTypeAddOrEditRequest.setGradeNum(new BigDecimal("3.14"));
		skuTypeAddOrEditRequest.setTypeRemark("4级测试......");
		SkuType skuType = skuTypeBiz.save(skuTypeAddOrEditRequest);
	}
}
