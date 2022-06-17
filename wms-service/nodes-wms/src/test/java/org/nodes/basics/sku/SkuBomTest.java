package org.nodes.basics.sku;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.basics.bom.impl.WmsSkuBomBizImpl;
import org.nodes.wms.dao.basics.bom.dto.input.SkuBomAddOrEditRequest;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * 物料清单测试类
 **/
@RunWith(BladeSpringRunner.class)
@BladeBootTest(appName = "wms3.3-test", profile = "test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SkuBomTest {

	@Autowired
	private WmsSkuBomBizImpl skuBomBiz;

	@Test
	public void saveTest(){
		SkuBomAddOrEditRequest skuBomAddOrEditRequest = new SkuBomAddOrEditRequest();
		skuBomAddOrEditRequest.setId(1537623781932036097L);
		skuBomAddOrEditRequest.setWoId(1530073675865391105L);
		skuBomAddOrEditRequest.setSkuId(1464200583655456777L);
		skuBomAddOrEditRequest.setWsuCode("KAR");
		skuBomAddOrEditRequest.setWsuName("KAR");
		skuBomAddOrEditRequest.setJoinSkuId(1464200583655456778L);
		skuBomAddOrEditRequest.setJoinWsuCode("GLL");
		skuBomAddOrEditRequest.setJoinWsuName("GLL");
		skuBomAddOrEditRequest.setQty(new BigDecimal(100));
		skuBomAddOrEditRequest.setRemark("这里是编辑测试");
		skuBomBiz.save(skuBomAddOrEditRequest);
	}
}
