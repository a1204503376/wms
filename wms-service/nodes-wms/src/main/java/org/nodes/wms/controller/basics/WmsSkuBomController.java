package org.nodes.wms.controller.basics;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.basics.bom.WmsSkuBomBiz;
import org.nodes.wms.dao.basics.bom.dto.input.WmsSkuBomPageQuery;
import org.nodes.wms.dao.basics.bom.dto.output.WmsSkuBomResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Query;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springblade.core.tool.api.R;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL +"WmsSkuBom")
public class WmsSkuBomController {
	private final WmsSkuBomBiz skuBomBiz;
	/**
	 * 分页查询
	 * @param query 底部分页参数
	 * @param skuBomPageQuery 查询条件
	 * @return 分页数据
	 */
	@PostMapping("/findAllWmsSkuBom")
	public R<IPage<WmsSkuBomResponse>> findAllWmsSkuBom(@RequestBody WmsSkuBomPageQuery skuBomPageQuery, Query query){
		Page<WmsSkuBomResponse> skuBomPage = skuBomBiz.getSkuBomPage(query, skuBomPageQuery);
		return R.data(skuBomPage);
	}

	/**
	 * 物料清单导出
	 */
	@ApiLog("物料清单-导出物料清单")
	@PostMapping("/excel")
	public void excel(@ApiIgnore @RequestBody HashMap<String, Object> params, HttpServletResponse response) {
		skuBomBiz.exportExcel(params,response);
	}

}
