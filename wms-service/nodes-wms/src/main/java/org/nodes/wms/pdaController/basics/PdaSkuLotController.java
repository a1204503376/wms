package org.nodes.wms.pdaController.basics;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * PDA批属性API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/skuLot")
public class PdaSkuLotController {
	private final SkuBiz skuBiz;

	@GetMapping("/findSkuDropDownBox")
	public R<List<String>> findSkuDropDownBox() {
		List<String> dropDownBox = skuBiz.getSkuDropDownBox();
		return R.data(dropDownBox);
	}
}
