package org.nodes.wms.controller.basics;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.basics.carriers.CarriersBiz;
import org.nodes.wms.dao.basics.carriers.dto.input.DeleteCarriersRequest;
import org.nodes.wms.dao.basics.carriers.dto.input.CarrierPageQuery;
import org.nodes.wms.dao.basics.carriers.dto.input.NewCarrierRequest;
import org.nodes.wms.dao.basics.carriers.dto.output.CarrierResponse;
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
	@PostMapping("/page")
	public R<IPage<CarrierResponse>> page(@RequestBody CarrierPageQuery carrierPageQuery, Query query) {
		IPage<CarrierResponse> pages = carriersBiz.getPage(query, carrierPageQuery);
		return R.data(pages);
	}
	/**
	 * 承运商管理新增
	 */
	@ApiLog("承运商管理-新增")
	@PostMapping("/newCarrier")
	public R newCarrier(@RequestBody NewCarrierRequest newCarrierRequest) {
		return R.status(carriersBiz.newCarrier(newCarrierRequest));
	}
	/**
	 * 承运商管理删除
	 */
	@ApiLog("承运商管理-逻辑删除")
	@PostMapping("/delete")
	public R delete(@RequestBody DeleteCarriersRequest deleteRequest) {
		return R.status(carriersBiz.remove(deleteRequest));
	}
}
