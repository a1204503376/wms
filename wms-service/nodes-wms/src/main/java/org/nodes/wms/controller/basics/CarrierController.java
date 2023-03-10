package org.nodes.wms.controller.basics;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.basics.carriers.CarrierBiz;
import org.nodes.wms.dao.basics.carrier.dto.input.*;
import org.nodes.wms.dao.basics.carrier.dto.output.CarrierDropDownResponse;
import org.nodes.wms.dao.basics.carrier.dto.output.CarrierResponse;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  承运商管理API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL +"carriers")
@Api(value = "承运商管理", tags = "承运商管理接口")
public class CarrierController {

	private final CarrierBiz carrierBiz;

	/**
	 * 承运商管理分页查询
	 */
	@ApiOperation(value = "分页")
	@PostMapping("/page")
	public R<IPage<CarrierResponse>> page(@RequestBody CarrierPageQuery carrierPageQuery, Query query) {
		IPage<CarrierResponse> pages = carrierBiz.getPage(query, carrierPageQuery);
		return R.data(pages);
	}
	/**
	 * 承运商管理新增
	 */
	@ApiLog("承运商管理-新增")
	@ApiOperation(value = "新增")
	@PostMapping("/newCarrier")
	public R newCarrier(@RequestBody NewCarrierRequest newCarrierRequest) {
		return R.status(carrierBiz.newCarrier(newCarrierRequest));
	}
	/**
	 * 承运商管理删除
	 */
	@ApiLog("承运商管理-逻辑删除")
	@ApiOperation(value = "删除")
	@PostMapping("/delete")
	public R delete(@RequestBody DeleteCarriersRequest deleteRequest) {
		return R.status(carrierBiz.remove(deleteRequest));
	}

	/**
	 * 承运商管理删除
	 */
	@ApiLog("承运商管理-导出承运商")
	@ApiOperation(value = "导出")
	@PostMapping("/excel")
	public void excel(@ApiIgnore @RequestBody HashMap<String, Object> params, HttpServletResponse response) {
		carrierBiz.exportExcel(params,response);
	}


	/**
	 * 承运商管理根据id修改状态
	 */
	@ApiLog("承运商管理-修改状态")
	@ApiOperation(value = "修改状态")
	@PostMapping("/updateStatusById")
	public R delete(@RequestBody UpdateStatusRequest updateStatusRequest) {
		return R.status(carrierBiz.updateStatusById(updateStatusRequest));
	}

	/**
	 * 承运商下拉数据
	 */
	@PostMapping("/getDropDown")
	@ApiOperation(value = "承运商组件数据")
	public R<List<CarrierDropDownResponse>> getDropDown(@RequestBody CarrierDropDownRequest carrierDropDownRequest) {
		return R.data(carrierBiz.getDropDown(carrierDropDownRequest));
	}

	/**
	 * 导出模板
	 */
	@GetMapping("/export-template")
	@ApiOperation(value = "导出Excel模板")
	public void exportTemplate(HttpServletResponse response){
		List<CarrierExcelRequest> importExcelList = new ArrayList<>();
		ExcelUtil.export(response, "承运商", "承运商数据表", importExcelList, CarrierExcelRequest.class);
	}

	@ApiLog("承运商管理-导入")
	@ApiOperation(value = "导入")
	@PostMapping("/import-data")
	public R<String> importData(MultipartFile file){
		List<CarrierExcelRequest> importDataList = ExcelUtil.read(file, CarrierExcelRequest.class);
		boolean importFlag = carrierBiz.importExcel(importDataList);
		return importFlag ? R.success("导入成功") : R.fail("导入失败");
	}
}
