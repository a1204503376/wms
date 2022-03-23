
package org.nodes.modules.wms.warehouse.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.dto.LocationDTO;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.enums.ZoneVirtualTypeEnum;
import org.nodes.wms.core.warehouse.excel.LocationExcel;
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.nodes.wms.core.warehouse.vo.LocationVO;
import org.nodes.wms.core.warehouse.wrapper.LocationWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.nodes.wms.core.warehouse.cache.LocationCache.LOCATION_CACHE;

/**
 * 库位管理 控制器
 *
 * @author zhongls
 * @since 2019-12-11
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/warehouse/location")
@Api(value = "库位管理", tags = "库位管理")
public class LocationController extends BladeController {

	private ILocationService locationService;

	/**
	 * 详情
	 */
	@ApiLog("库位-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入location")
	public R<LocationVO> detail(Location location){
		if (Func.isEmpty(location)){
			throw new ServiceException("传参Location为空");
		}
		Location detail = LocationCache.getById(location.getLocId());
		return R.data(LocationWrapper.build().entityVO(detail));
	}

	/**
	 * 列表
	 */
	@ApiLog("库位-列表")
	@GetMapping("/list")
	@ApiOperation(value = "列表", notes = "传入location")
	public R<List<LocationVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<Location> list = locationService.list(Condition.getQueryWrapper(params, Location.class));
		return R.data(LocationWrapper.build().listVO(list));
	}
	/**
	 * 分页
	 */
	@ApiLog("库位-分页")
	@GetMapping("/page")
	@ApiOperation(value = "分页", notes = "传入location")
	public R<IPage<LocationVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<Location> pages = locationService.page(Condition.getPage(query), Condition.getQueryWrapper(params, Location.class));
		return R.data(LocationWrapper.build().pageVO(pages));
	}

	/**
	 * 新增或修改
	 */
	@ApiLog("库位-提交")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入location")
	public R submit(@Valid @RequestBody Location location) {
		CacheUtil.clear(LOCATION_CACHE);
		return R.status(locationService.saveOrUpdate(location));
	}


	/**
	 * 删除
	 */
	@ApiLog("库位-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		ILocationService locationService = SpringUtil.getBean(ILocationService.class);
		List<Location> locationList = locationService.listByIds(Func.toLongList(ids));
		for (Location location : locationList) {
			// 判断是否真实库位
			if (!AuthUtil.isDeveloper() && ZoneVirtualTypeEnum.isVirtual(location.getLocCode())) {
				throw new ServiceException(String.format("库位[%s]是系统生成虚拟库区不可删除", location.getLocCode()));
			}
		}
		CacheUtil.clear(LOCATION_CACHE);
		return R.status(locationService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 锁定库位
	 *
	 * @param
	 * @return
	 */
	@ApiLog("库位-锁定")
	@PostMapping("/lock")
	@ApiOperation(value = "锁定库位", notes = "传入库位ids")
	public R lock(String locIds) {
		CacheUtil.clear(LOCATION_CACHE);
		return R.status(locationService.lockById(locIds, StringPool.N.toUpperCase()));
	}

	/**
	 * 解锁库位
	 *
	 * @param
	 * @return
	 */
	@ApiLog("库位-解锁")
	@PostMapping("/unlock")
	@ApiOperation(value = "解锁库位", notes = "传入库位ids")
	public R unlock(String locIds) {
		CacheUtil.clear(LOCATION_CACHE);
		return R.status(locationService.lockById(locIds, null));
	}

	/**
	 * 打印
	 * @return
	 */
	@ApiLog("库位-打印")
	@PostMapping("/print")
	@ApiOperation(value = "打印库位标签", notes = "传入库位ids")
	public R print(@ApiParam(value = "主键集合", required = true) @RequestParam String ids){
		return R.data(locationService.print(Func.toLongList(ids)));
	}

	/**
	 * 导出
	 */
	@ApiLog("库位-导出")
	@GetMapping("export")
	@ApiOperation(value = "导出", notes = "查询条件")
	public void export(@ApiIgnore @RequestParam HashMap<String, Object> params, HttpServletResponse response) {
		locationService.exportExcel(params, response);
	}

	/**
	 * 导出模板
	 */
	@ApiLog("库位-导出模板")
	@GetMapping("export-template")
	@ApiOperation(value = "导出模板")
	public void exportTemplate(HttpServletResponse response) {
		List<LocationExcel> locExportList = new ArrayList<>();
		ExcelUtil.export(response, "库位", "库位数据表", locExportList, LocationExcel.class);
	}

	/**
	 * 导入验证
	 */
	@ApiLog("库位-导入验证")
	@PostMapping("import-valid")
	@ApiOperation(value = "导入验证")
	public R<List<DataVerify>> importValid(MultipartFile file) {
		return R.data(locationService.validExcel(ExcelUtil.read(file, LocationExcel.class)));
	}

	/**
	 * 导入验证通过的数据
	 */
	@ApiLog("库位-导入数据")
	@PostMapping("import-data")
	@ApiOperation(value = "导入数据")
	public R<Boolean> importData(@RequestBody List<DataVerify> dataVerifyList) {
		return R.data(locationService.importData(dataVerifyList));
	}
}
