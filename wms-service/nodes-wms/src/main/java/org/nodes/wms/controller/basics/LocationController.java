
package org.nodes.wms.controller.basics;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.core.tool.validation.Update;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.dao.basics.location.dto.input.*;
import org.nodes.wms.dao.basics.location.dto.output.LocationDetailResponse;
import org.nodes.wms.dao.basics.location.dto.output.LocationEditResponse;
import org.nodes.wms.dao.basics.location.dto.output.LocationPageResponse;
import org.nodes.wms.dao.basics.location.dto.output.LocationSelectResponse;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 库位管理 API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL +"warehouse/location")
public class LocationController {

	private final LocationBiz locationBiz;

	@PostMapping("/page")
	public R<Page<LocationPageResponse>> page(Query query, @RequestBody LocationPageQuery locationPageQuery){
		Page<LocationPageResponse> pageResponse = locationBiz.page(query, locationPageQuery);
		return R.data(pageResponse);
	}

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	public R<LocationDetailResponse> detail(@RequestParam Long locId){
		LocationDetailResponse detail = locationBiz.getLocationDetailById(locId);
		return R.data(detail);
	}

	/**
	 * 列表
	 */
//	@ApiLog("库位-列表")
//	@GetMapping("/list")
//	@ApiOperation(value = "列表", notes = "传入location")
//	public R<List<LocationVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
//		List<Location> list = locationService.list(Condition.getQueryWrapper(params, Location.class));
//		return R.data(LocationWrapper.build().listVO(list));
//	}

	/**
	 * 新增
	 */
	@ApiLog("库位-新增")
	@PostMapping("/add")
	public R<String> add(@Valid @RequestBody LocationAddOrEditRequest locationAddOrEditRequest) {
		Location location = locationBiz.add(locationAddOrEditRequest);
		return R.success("新增库位成功，库位编码:" + location.getLocCode());
	}

	/**
	 * 删除
	 */
	@ApiLog("库位-删除")
	@PostMapping("/remove")
	public R<String> remove(@Valid @RequestBody LocationRemoveRequest locationRemoveRequest) {
		boolean remove = locationBiz.remove(locationRemoveRequest.getIdList());
		return remove ? R.success("删除成功") : R.fail("删除失败");
	}

	@GetMapping("/detailByEdit")
	public R<LocationEditResponse> detailByEdit(@RequestParam Long locId){
		return R.data(locationBiz.findLocationById(locId));
	}

	@ApiLog("库位-编辑")
	@PostMapping("/edit")
	public R<String> edit(@Validated({ Update.class }) @RequestBody LocationAddOrEditRequest locationAddOrEditRequest){
		Location location = locationBiz.edit(locationAddOrEditRequest);
		return R.success("编辑成功，库位编码为：" + location.getLocCode());
	}

	/**
	 * 锁定库位
	 *
	 * @param
	 * @return
	 */
//	@ApiLog("库位-锁定")
//	@PostMapping("/lock")
//	@ApiOperation(value = "锁定库位", notes = "传入库位ids")
//	public R lock(String locIds) {
//		CacheUtil.clear(LOCATION_CACHE);
//		return R.status(locationService.lockById(locIds, StringPool.N.toUpperCase()));
//	}

	/**
	 * 解锁库位
	 *
	 * @param
	 * @return
	 */
//	@ApiLog("库位-解锁")
//	@PostMapping("/unlock")
//	@ApiOperation(value = "解锁库位", notes = "传入库位ids")
//	public R unlock(String locIds) {
//		CacheUtil.clear(LOCATION_CACHE);
//		return R.status(locationService.lockById(locIds, null));
//	}

	/**
	 * 打印
	 * @return
	 */
//	@ApiLog("库位-打印")
//	@PostMapping("/print")
//	@ApiOperation(value = "打印库位标签", notes = "传入库位ids")
//	public R print(@ApiParam(value = "主键集合", required = true) @RequestParam String ids){
//		return R.data(locationService.print(Func.toLongList(ids)));
//	}

	/**
	 * 导出
	 */
	@PostMapping("/export")
	public void export(@RequestBody LocationPageQuery locationPageQuery,HttpServletResponse response) {
		locationBiz.exportExcel(locationPageQuery, response);
	}

	/**
	 * 导出模板
	 */
	@GetMapping("export-template")
	public void exportTemplate(HttpServletResponse response) {
		List<LocationExcelRequest> locExportList = new ArrayList<>();
		ExcelUtil.export(response, "库位", "库位数据表", locExportList, LocationExcelRequest.class);
	}

	/**
	 * 导入数据
	 */
	@PostMapping("import-data")
	public R<String> importData(MultipartFile file) {
		List<LocationExcelRequest> locationDataList = ExcelUtil.read(file, LocationExcelRequest.class);
		boolean tag = locationBiz.importData(locationDataList);
		return tag ? R.success("导入成功") : R.fail("导入失败");
	}

	/**
	 * 获取客户下拉列表最近10条数据
	 */
	@PostMapping("getLocationSelectResponseTop10List")
	public R<List<LocationSelectResponse>> getLocationSelectResponseTop10List(@RequestBody LocationSelectQuery locationSelectQuery) {
		return R.data(locationBiz.getLocationSelectResponseTop10List(locationSelectQuery));
	}

}
