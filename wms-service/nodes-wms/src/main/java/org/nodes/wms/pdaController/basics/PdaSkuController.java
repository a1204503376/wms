package org.nodes.wms.pdaController.basics;

import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.dao.basics.sku.dto.input.FindSkuByCodeRequest;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 收货管理API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/sku")
public class PdaSkuController {
	private final SkuBiz skuBiz;

	/**
	 * PDA查询型号下拉框组件
	 *
	 * @return 型号集合
	 */
	@GetMapping("/findSkuDropDownBox")
	public R<List<String>> findSkuDropDownBox() {
		List<String> dropDownBox = skuBiz.getSkuDropDownBox();
		return R.data(dropDownBox);
	}

	/**
	 * PDA根据物料编码查询型号下拉框组件
	 *
	 * @param request 包含物料编码
	 * @return 物品型号集合
	 */
	@PostMapping("/findSkuByCode")
	public R<List<String>> findSkuByCode(@RequestBody FindSkuByCodeRequest request) {
		List<Sku> skus = skuBiz.selectSkuListByNo(request.getNo());
		List<String> skuList = skus.stream()
			.filter(sku -> Func.isNotEmpty(sku.getSkuSpec()))
			.map(Sku::getSkuSpec)
			.distinct()
			.collect(Collectors.toList());
		return R.data(skuList);
	}


}
