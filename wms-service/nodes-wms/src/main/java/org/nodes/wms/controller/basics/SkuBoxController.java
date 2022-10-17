package org.nodes.wms.controller.basics;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.skuBox.skuBox.SkuBoxBiz;
import org.nodes.wms.dao.tianyi.skubox.dto.input.SkuBoxPageQuery;
import org.nodes.wms.dao.tianyi.skubox.dto.output.SkuBoxPageResponse;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 箱贴识别码控制器
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "skuBox")
public class SkuBoxController {

	private final SkuBoxBiz SkuBoxBiz;

	@PostMapping("/page")
	public R<Page<SkuBoxPageResponse>> page(@RequestBody Query query, SkuBoxPageQuery skuBoxPageQuery) {
		return R.data(SkuBoxBiz.getPage(query, skuBoxPageQuery));
	}

	@PostMapping("/export")
	public void export(@RequestBody SkuBoxPageQuery skuBoxPageQuery, HttpServletResponse response) {
		SkuBoxBiz.exportExcel(skuBoxPageQuery, response);
	}
}
