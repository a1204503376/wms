package org.nodes.wms.controller.basics;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.biz.basics.warehouse.WarehouseBiz;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.dto.WarehouseDTO;
import org.nodes.wms.core.warehouse.excel.WarehouseExcel;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.nodes.wms.core.warehouse.vo.WarehouseVO;
import org.nodes.wms.core.warehouse.wrapper.WarehouseWrapper;
import org.nodes.wms.dao.basics.warehouse.dto.output.WarehouseResponse;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.nodes.wms.core.warehouse.cache.WarehouseCache.WAREHOUSE_CACHE;

/**
 * 仓库管理 API
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL +"warehouse/warehouse")
public class WarehouseController extends BladeController {

	private final IWarehouseService warehouseService;
	private final WarehouseBiz warehouseBiz;

	/**
	 * 获取库房下拉框集合
	 */
	@GetMapping("getWarehouseSelectResponseList")
	public R<List<WarehouseResponse>> getWarehouseSelectResponseList(){
         List<WarehouseResponse> warehouseResponseList =  warehouseBiz.getWarehouseSelectResponseList();
		 return R.data(warehouseResponseList);
	}

	/**
	 * 通过仓库ID获取详情
	 */
	@GetMapping("/detail")
	public R<WarehouseVO> detail(Long id) {
		Warehouse warehouse = WarehouseCache.getById(id);
		WarehouseVO detail = WarehouseWrapper.build().entityVO(warehouse);
		return R.data(detail);
	}

	/**
	 * 列表 仓库
	 */
	@GetMapping("/list")
	public R<List<WarehouseVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<Warehouse> list = warehouseService.list(Condition.getQueryWrapper(params, Warehouse.class).lambda()
			.orderByDesc(Warehouse::getCreateTime));
		return R.data(WarehouseWrapper.build().listVO(list));
	}

	/**
	 * 自定义分页 仓库
	 */
	@GetMapping("/page")
	public R<IPage<WarehouseVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<Warehouse> pages = warehouseService.page(Condition.getPage(query), Condition.getQueryWrapper(params, Warehouse.class)
			.lambda()
			.orderByDesc(Warehouse::getCreateTime));
		return R.data(WarehouseWrapper.build().pageVO(pages));
	}

	/**
	 * 新增或修改 仓库
	 */
	@ApiLog("库房-提交")
	@PostMapping("/submit")
	public R submit(@Validated @RequestBody WarehouseDTO warehouseDTO) {
		CacheUtil.clear(WAREHOUSE_CACHE);
		return R.status(warehouseService.saveOrUpdate(warehouseDTO));
	}


	/**
	 * 删除 仓库
	 */
	@ApiLog("库房-删除")
	@PostMapping("/remove")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		CacheUtil.clear(WAREHOUSE_CACHE);
		return R.status(warehouseService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 导出
	 */
	@GetMapping("export")
	public void export(@ApiIgnore @RequestParam HashMap<String, Object> params, HttpServletResponse response) {
		warehouseService.exportExcel(params, response);
	}

	/**
	 * 导出模板
	 */
	@GetMapping("export-template")
	public void exportTemplate(HttpServletResponse response) {
		List<WarehouseExcel> warehouseExportList = new ArrayList<>();
		ExcelUtil.export(response, "库房", "库房数据表", warehouseExportList, WarehouseExcel.class);
	}

	/**
	 * 导入验证
	 */
	@PostMapping("import-valid")
	public R<List<DataVerify>> importValid(MultipartFile file) {
		return R.data(warehouseService.validExcel(ExcelUtil.read(file, WarehouseExcel.class)));
	}

	/**
	 * 导入验证通过的数据
	 */
	@PostMapping("import-data")
	public R<Boolean> importData(@RequestBody List<DataVerify> dataVerifyList) {
		return R.data(warehouseService.importData(dataVerifyList));
	}
}
