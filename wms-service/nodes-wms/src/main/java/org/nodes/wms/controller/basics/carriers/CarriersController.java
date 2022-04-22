package org.nodes.wms.controller.basics.carriers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.basics.carriers.CarriersBiz;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersDeleteRequest;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersPageQuery;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersRequest;
import org.nodes.wms.dao.basics.carriers.dto.output.CarriersResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
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
	@GetMapping("/page")
	public R<Object> page(@RequestParam CarriersPageQuery carriersPageQuery, Query query) {
		IPage<CarriersResponse> pages = carriersBiz.page(Condition.getPage(query),carriersPageQuery);
		return R.data(pages);
	}
	/**
	 * 承运商管理新增
	 */
	@ApiLog("承运商管理-新增")
	@PostMapping("/save")
	public R save(@RequestParam CarriersRequest carriersRequest) {
		int count = carriersBiz.save(carriersRequest);
		return count>0?R.success("提交成功"):R.fail("失败");
	}

	/**
	 * 承运商管理修改
	 */
	@ApiLog("承运商管理-修改")
	@PostMapping("/update")
	public R update(@RequestParam CarriersRequest carriersRequest) {
		int count = carriersBiz.update(carriersRequest);
		return count>0?R.success("修改成功"):R.fail("修改失败");
	}
	/**
	 * 承运商管理删除
	 */
	@ApiLog("承运商管理-逻辑删除")
	@PostMapping("/delete")
	public R delete(@RequestParam CarriersDeleteRequest deleteRequest) {
		int count = carriersBiz.delete(deleteRequest);
		return count>0?R.success("删除成功"):R.fail("删除失败");
	}
}
