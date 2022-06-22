package org.nodes.wms.pdaController.basics;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.dao.basics.sku.dto.output.PdaSkuSelectResponse;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 收货管理API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/sku")
public class PdaSkuController {
	private final SkuBiz skuBiz;

	@GetMapping("/findSkuDropDownBox")
	public R<List<PdaSkuSelectResponse>> findSkuDropDownBox() {
		List<PdaSkuSelectResponse> dropDownBox = skuBiz.getSkuDropDownBox();
		return R.data(dropDownBox);
	}
}
