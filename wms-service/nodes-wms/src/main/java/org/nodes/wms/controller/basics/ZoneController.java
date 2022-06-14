
package org.nodes.wms.controller.basics;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.core.warehouse.dto.ZoneDTO;
import org.nodes.wms.core.warehouse.excel.ZoneExcel;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.core.warehouse.vo.ZoneVO;
import org.nodes.wms.core.warehouse.wrapper.ZoneWrapper;
import org.nodes.wms.dao.basics.zone.dto.ZoneSelectQuery;
import org.nodes.wms.dao.basics.zone.dto.ZoneSelectResponse;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库区管理 控制器
 *
 * @author zhongls
 * @since 2019-12-06
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/wms/warehouse/zone")
public class ZoneController {

	private final IZoneService zoneService;

	private final ZoneBiz zoneBiz;

	/**
	 * 详情
	 */
	@ApiLog("库区-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入zone")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "zoneId", value = "库区ID", paramType = "query", dataType = "Long")
	})
	public R<Zone> detail(@ApiIgnore @RequestParam Map<String, Object> zone) {
		Zone detail = zoneService.getOne(Condition.getQueryWrapper(zone, Zone.class));
		return R.data(ZoneWrapper.build().entityVO(detail));
	}

	/**
	 * 列表
	 */
	@ApiLog("库区-列表")
	@GetMapping("/list")
	@ApiOperation(value = "列表", notes = "传入zone")
	public R<List<ZoneVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		List<Zone> list = zoneService.list(Condition.getQueryWrapper(params, Zone.class).lambda()
			.orderByAsc(Zone::getWhId, Zone::getZoneType));
		return R.data(ZoneWrapper.build().listVO(list));
	}

	@ApiLog("库区-分页")
	@GetMapping("/page")
	@ApiOperation(value = "分页查询所有库区（包括系统隐藏库区）")
	public R<IPage<ZoneVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<Zone> pages = zoneService.page(Condition.getPage(query), Condition.getQueryWrapper(params, Zone.class)
			.lambda()
			.orderByAsc(Zone::getWhId, Zone::getZoneType, Zone::getZoneName));
		return R.data(ZoneWrapper.build().pageVO(pages));
	}

	/**
	 * 新增或修改
	 */
	@ApiLog("库区-提交")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入zoneDTO")
	public R submit(@Valid @RequestBody ZoneDTO zoneDTO) {
		return R.status(zoneService.saveOrUpdate(zoneDTO));
	}


	/**
	 * 删除
	 */
	@ApiLog("库区-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(zoneService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 导出
	 */
	@ApiLog("库区-导出")
	@GetMapping("export")
	@ApiOperation(value = "导出", notes = "查询条件")
	public void export(@ApiIgnore @RequestParam HashMap<String, Object> params, HttpServletResponse response) {
		zoneService.exportExcel(params, response);
	}

	/**
	 * 导出模板
	 */
	@ApiLog("库区-导出模板")
	@GetMapping("export-template")
	@ApiOperation(value = "导出模板")
	public void exportTemplate(HttpServletResponse response) {
		List<ZoneExcel> zoneExportList = new ArrayList<>();
		ExcelUtil.export(response, "库区", "库区数据表", zoneExportList, ZoneExcel.class);
	}

	/**
	 * 导入验证
	 */
	@ApiLog("库区-导入验证")
	@PostMapping("import-valid")
	@ApiOperation(value = "导入验证")
	public R<List<DataVerify>> importValid(MultipartFile file) {
		return R.data(zoneService.validExcel(ExcelUtil.read(file, ZoneExcel.class)));
	}

	/**
	 * 导入验证通过的数据
	 */
	@ApiLog("库区-导入数据")
	@PostMapping("import-data")
	@ApiOperation(value = "导入数据")
	public R<Boolean> importData(@RequestBody List<DataVerify> dataVerifyList) {
		return R.data(zoneService.importData(dataVerifyList));
	}

	@PostMapping("/select")
	public R<List<ZoneSelectResponse>> getZoneSelectData(@RequestBody ZoneSelectQuery zoneSelectQuery){
		List<ZoneSelectResponse> zoneSelectResponseList = zoneBiz.getZoneSelectData(zoneSelectQuery.getWhIdList());
		return R.data(zoneSelectResponseList);
	}
}
