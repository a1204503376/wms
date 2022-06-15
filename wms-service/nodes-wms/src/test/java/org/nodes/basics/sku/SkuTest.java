package org.nodes.basics.sku;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.nodes.wms.biz.basics.sku.impl.SkuBizImpl;
import org.nodes.wms.dao.basics.sku.dto.input.SkuAddOrEditRequest;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuInc;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageAggregate;
import org.nodes.wms.dao.basics.sku.entities.SkuReplace;
import org.springblade.core.test.BladeBootTest;
import org.springblade.core.test.BladeSpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 物品测试类
 **/
@RunWith(BladeSpringRunner.class)
@BladeBootTest(appName = "wms3.3-test", profile = "test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SkuTest {
	@Autowired
	private SkuBizImpl skuBiz;

	@Test
	public void find(){
		SkuPackageAggregate skuPackageAggregate = skuBiz.findSkuPackageAggregateBySkuId(14L);
		System.out.println(skuPackageAggregate);
	}

	@Test
	public void saveTest(){
		SkuAddOrEditRequest skuAddOrEditRequest = new SkuAddOrEditRequest();
//		skuAddOrEditRequest.setSkuId();
		skuAddOrEditRequest.setSkuCode("Test0615"+new Date());
		skuAddOrEditRequest.setSkuName("测试0615");
		skuAddOrEditRequest.setSkuNameS("测试");
		skuAddOrEditRequest.setSkuRemark("我是备注0615");
		skuAddOrEditRequest.setWoId(1530073675865391105L);
		skuAddOrEditRequest.setSkuTypeId(1422084640906268681L); //物品分类id
		skuAddOrEditRequest.setWspId(1422115802332651534L); //包装id
		skuAddOrEditRequest.setWslId(1414527678734798850L); // 批属性设置id
		skuAddOrEditRequest.setWslvId(1414762747261939714L); // 批属性校验id
		skuAddOrEditRequest.setSkuBarcodeList("我是物品条码清单"); // 批属性校验id
		skuAddOrEditRequest.setSkuStorageType(1); // 存货类型 ：1货架,2恒温,3地堆
		skuAddOrEditRequest.setAbc(10);
		skuAddOrEditRequest.setIsSn(1); //序列号管理（1：序列号管理  0：非序列号管理）
		skuAddOrEditRequest.setSkuVolume(new BigDecimal(100)); // 体积
		skuAddOrEditRequest.setSkuNetWeight(new BigDecimal(100)); // 净重
		skuAddOrEditRequest.setSkuTareWeight(new BigDecimal(100)); // 皮重
		skuAddOrEditRequest.setSkuGrossWeight(new BigDecimal(100)); // 毛重
		skuAddOrEditRequest.setQualityHours(24); // 保质期小时数
//		skuAddOrEditRequest.setAttribute3(24); // 临期阈值

		List<SkuReplace> skuReplaceList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			SkuReplace skuReplace = new SkuReplace();
//			skuReplace.setSkuId();
			skuReplace.setWspId(1422115802332651537L); //包装id
			skuReplace.setWsuId(202112150001L); // 计量单位id
			skuReplace.setQty(new BigDecimal(10)); // 物品数量
			skuReplace.setWsrepSkuId(Long.parseLong("246420058365545677"+i)); // 替代物品id
			skuReplace.setWsrepWspId(1422115802332652059L); // 替代包装id
			skuReplace.setWsrepWsuId(1422115802332651543L); //替代计量单位id
			skuReplace.setWsrepQty(new BigDecimal(10)); // 替代数量
			skuReplace.setWsrepOrder(2); // 替代数量
			skuReplace.setCreateUser(1123598821738675201L);
			skuReplace.setUpdateTime(new Date());
			skuReplaceList.add(skuReplace);
		}
		skuAddOrEditRequest.setSkuReplaceList(skuReplaceList);

		List<SkuInc> skuSkuIncList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			SkuInc skuInc = new SkuInc();
			Random random = new Random();
//			skuInc.setSkuId();
			skuInc.setPeId(Long.parseLong("153156190994862489"+i));
			skuInc.setWspId(Long.parseLong("142211580233265154"+i));
			skuInc.setWssupOrder(10);
			skuInc.setSkuName("物品名称（针对当前企业的名称）");
			skuInc.setCreateUser(1123598821738675201L);
			skuInc.setUpdateTime(new Date());
			skuSkuIncList.add(skuInc);
		}
		skuAddOrEditRequest.setSkuIncList(skuSkuIncList);

		List<Long> replaceIdList = new ArrayList<>();
		replaceIdList.add(1537003766509821953L);
		skuAddOrEditRequest.setSkuReplaceIdList(replaceIdList);

		List<Long> incIdList = new ArrayList<>();
		replaceIdList.add(1537003766765674497L);
		skuAddOrEditRequest.setSkuIncIdList(incIdList);
		Sku sku = skuBiz.save(skuAddOrEditRequest);
		System.out.println(sku.getSkuCode());
	}
}
