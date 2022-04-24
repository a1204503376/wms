package org.nodes.wms.controller.basics;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.basics.carriers.CarriersBiz;
import org.nodes.wms.dao.basics.carriers.dto.input.DeleteCarriersRequest;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersPageQuery;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersRequest;
import org.nodes.wms.dao.basics.carriers.dto.output.CarriersResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;
/**
 *  承运商管理API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL +"carriers")
public class CarriersController {
	private  final CarriersBiz carriersBiz;
	/**
	 * 承运商管理分页查询
	 */
	@GetMapping("/queryPage")
	public R<IPage<CarriersResponse>> queryPage(@RequestParam CarriersPageQuery carriersPageQuery, Query query) {
		IPage<CarriersResponse> pages = carriersBiz.getPage(query,carriersPageQuery);
		return R.data(pages);
	}
	/**
	 * 承运商管理新增
	 */
	@ApiLog("承运商管理-新增")
	@PostMapping("/newCarriersRequest")
	public R newCarriersRequest (@RequestParam CarriersRequest carriersRequest) {
		return R.status(carriersBiz.saveCarriers(carriersRequest));
	}
	/**
	 * 承运商管理删除
	 */
	@ApiLog("承运商管理-逻辑删除")
	@PostMapping("/delete")
	public R delete(@RequestParam DeleteCarriersRequest deleteRequest) {
		return R.status(carriersBiz.remove(deleteRequest));
	}
}
