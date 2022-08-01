package org.nodes.wms.pdaController.stock.devanning;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.stockManage.DevanningBiz;
import org.nodes.wms.dao.stock.dto.input.DevanningSubmitRequest;
import org.nodes.wms.dao.stock.dto.input.FindAllSerialNumberManageRequest;
import org.nodes.wms.dao.stock.dto.output.FindAllSerialNumberManageResponse;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PDA库内管理: 拆箱Api
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/stock/devanning")
public class DevanningController {
	private final DevanningBiz devanningBiz;

	/**
	 * 根据箱码查询序列号列表/库存列表
	 *
	 * @param request 箱码
	 * @return 响应对象
	 */
	@PostMapping("/findAllSerialNumberManage")
	public R<FindAllSerialNumberManageResponse> findAllSerialNumberManage(@RequestBody FindAllSerialNumberManageRequest request) {
		return R.data(devanningBiz.getAllSerialNumberManage(request));
	}

	/**
	 * 根据序列号列表/物品集合 拆箱
	 *
	 * @param request 包含序列号集合 目标库位编码/物品集合 箱码 是否整箱上架
	 * @return 是否成功
	 */
	@PostMapping("/devanningSubmit")
	public R<String> devanningSubmit(@RequestBody DevanningSubmitRequest request) {
		devanningBiz.devanning(request);
		return R.success("拆箱成功");
	}


}
