package org.nodes.modules.wms.basedata.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuInc;
import org.nodes.wms.core.basedata.entity.SkuPackage;
import org.nodes.wms.core.basedata.service.ISkuIncService;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * author: pengwei
 * date: 2021/5/18 13:12
 * description: SkuIncController
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/basedata/sku-inc")
@Api(value = "物品企业", tags = "物品企业接口")
public class SkuIncController {

	ISkuIncService skuIncService;

	/**
	 * 获取物品包装
	 */
	@ApiLog("物品企业接口-获取包装")
	@GetMapping("/getSkuPackage")
	@ApiOperation(value = "获取包装", notes = "传入sku")
	public R<SkuPackage> getSkuPackage(@NotNull SkuInc entity) {
		SkuInc skuInc = skuIncService.getOne(Condition.getQueryWrapper(new SkuInc()).lambda()
			.eq(SkuInc::getSkuId, entity.getSkuId())
			.eq(SkuInc::getPeId, entity.getPeId())
			.last("limit 1"));
		SkuPackage skuPackage = null;
		if (Func.isEmpty(skuInc)) {
			Sku sku = SkuCache.getById(entity.getSkuId());
			if (Func.isNotEmpty(sku)) {
				skuPackage = SkuPackageCache.getById(sku.getWspId());
			}
		} else {
			skuPackage = SkuPackageCache.getById(skuInc.getWspId());
		}
		return R.data(skuPackage);
	}
}
