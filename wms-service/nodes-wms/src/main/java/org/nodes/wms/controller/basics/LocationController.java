
package org.nodes.wms.controller.basics;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.core.tool.validation.Update;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.nodes.wms.core.warehouse.vo.LocationVO;
import org.nodes.wms.core.warehouse.wrapper.LocationWrapper;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 库位管理 API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL +"warehouse/location")
public class LocationController {

	private final LocationBiz locationBiz;
	private final ILocationService locationService;

	/**
	 * 物品管理-出入库设置-库位列表
	 */
	@GetMapping("/list")
	public R<List<LocationVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<org.nodes.wms.core.warehouse.entity.Location> list = locationService.list(Condition.getQueryWrapper(params, org.nodes.wms.core.warehouse.entity.Location.class));
		return R.data(LocationWrapper.build().listVO(list));
	}

	/**
	 * 库位：分页
	 */
	@PostMapping("/page")
	public R<Page<LocationPageResponse>> page(Query query, @RequestBody LocationPageQuery locationPageQuery){
		Page<LocationPageResponse> pageResponse = locationBiz.page(query, locationPageQuery);
		return R.data(pageResponse);
	}

	/**
	 * 库位：详情
	 */
	@GetMapping("/detail")
	public R<LocationDetailResponse> detail(@RequestParam Long locId){
		LocationDetailResponse detail = locationBiz.getLocationDetailById(locId);
		return R.data(detail);
	}

	/**
	 * 库位：新增
	 */
	@ApiLog("库位-新增")
	@PostMapping("/add")
	public R<String> add(@Valid @RequestBody LocationAddOrEditRequest locationAddOrEditRequest) {
		Location location = locationBiz.add(locationAddOrEditRequest);
		return R.success("新增库位成功，库位编码:" + location.getLocCode());
	}

	/**
	 * 库位：删除
	 */
	@ApiLog("库位-删除")
	@PostMapping("/remove")
	public R<String> remove(@Valid @RequestBody LocationRemoveRequest locationRemoveRequest) {
		boolean remove = locationBiz.remove(locationRemoveRequest.getIdList());
		return remove ? R.success("删除成功") : R.fail("删除失败");
	}

	@ApiLog("库位-冻结")
	@PostMapping("/freeze")
	public R<String> freeze(@Valid @RequestBody LocationFreezeThawRequest locationFreezeThawRequest){
		locationBiz.freezeBatch(locationFreezeThawRequest.getLocIdList());
		return R.success("冻结成功");
	}

	@ApiLog("库位-解冻")
	@PostMapping("/thaw")
	public R<String> thaw(@Valid @RequestBody LocationFreezeThawRequest locationFreezeThawRequest){
		locationBiz.thawBatch(locationFreezeThawRequest.getLocIdList());
		return R.success("解冻成功");
	}

	/**
	 * 库位编辑：根据库位id获取库位信息
	 */
	@ApiLog("库位-编辑")
	@GetMapping("/detailByEdit")
	public R<LocationEditResponse> detailByEdit(@RequestParam Long locId){
		return R.data(locationBiz.findLocationById(locId));
	}

	/**
	 * 库位：编辑
	 */
	@ApiLog("库位-编辑")
	@PostMapping("/edit")
	public R<String> edit(@Validated({ Update.class }) @RequestBody LocationAddOrEditRequest locationAddOrEditRequest){
		Location location = locationBiz.edit(locationAddOrEditRequest);
		return R.success("编辑成功，库位编码为：" + location.getLocCode());
	}

	/**
	 * 库位：服务端导出
	 */
	@PostMapping("/export")
	public void export(@RequestBody LocationPageQuery locationPageQuery,HttpServletResponse response) {
		locationBiz.exportExcel(locationPageQuery, response);
	}

	/**
	 * 库位：模板导出
	 */
	@GetMapping("export-template")
	public void exportTemplate(HttpServletResponse response) {
		List<LocationExcelRequest> locExportList = new ArrayList<>();
		ExcelUtil.export(response, "库位", "库位数据表", locExportList, LocationExcelRequest.class);
	}

	/**
	 * 库位：导入
	 */
	@ApiLog("库位-导入")
	@PostMapping("import-data")
	public R<String> importData(MultipartFile file) {
		return locationBiz.importData(file) ? R.success("导入成功") : R.fail("导入失败");
	}

	/**
	 * 库位组件：根据库位编码或名称获取最近更新的10个库位信息
	 */
	@PostMapping("getLocationSelectResponseTop10List")
	public R<List<LocationSelectResponse>> getLocationSelectResponseTop10List(@RequestBody LocationSelectQuery locationSelectQuery) {
		return R.data(locationBiz.getLocationSelectResponseTop10List(locationSelectQuery));
	}
}
