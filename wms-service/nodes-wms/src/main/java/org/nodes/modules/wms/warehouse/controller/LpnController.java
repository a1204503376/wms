
package org.nodes.modules.wms.warehouse.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.core.warehouse.dto.LpnDTO;
import org.nodes.wms.core.warehouse.entity.Lpn;
import org.nodes.wms.core.warehouse.excel.LpnExcel;
import org.nodes.wms.core.warehouse.service.ILpnService;
import org.nodes.wms.core.warehouse.vo.LpnVO;
import org.nodes.wms.core.warehouse.wrapper.LpnWrapper;
import org.springblade.core.boot.ctrl.BladeController;
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
 * 容器管理控制器
 *
 * @author liangmei
 * @since 2019-12-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/warehouse/lpn")
@Api(value = "容器管理", tags = "容器管理")
public class LpnController extends BladeController {

	private ILpnService lpnService;

	/**
	 * 容器详情
	 */
	@ApiLog("容器-详情")
	@GetMapping("/detail")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "lpnId", value = "月台ID", paramType = "query", dataType = "string")
	})
	@ApiOperation(value = "容器详情", notes = "传入lpn")
	public R<LpnVO> detail(@ApiIgnore @RequestParam Map<String, Object> lpn) {
		Lpn detail = lpnService.getOne(Condition.getQueryWrapper(lpn, Lpn.class));
		return R.data(LpnWrapper.build().entityVO(detail));
	}

	/**
	 * 列表
	 */
	@ApiLog("容器-列表")
	@GetMapping("/list")
	@ApiOperation(value = "容器列表", notes = "传入lpn")
	public R<List<LpnVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<Lpn> pages = lpnService.list(Condition.getQueryWrapper(params, Lpn.class));
		return R.data(LpnWrapper.build().listVO(pages));
	}

	/**
	 * 分页
	 */
	@ApiLog("容器-分页")
	@GetMapping("/page")
	@ApiOperation(value = "容器分页", notes = "传入lpn")
	public R<IPage<LpnVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<Lpn> pages = lpnService.page(Condition.getPage(query), Condition.getQueryWrapper(params, Lpn.class));
		return R.data(LpnWrapper.build().pageVO(pages));
	}

	/**
	 * 新增或修改容器
	 */
	@ApiLog("容器-提交")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改容器", notes = "传入lpn")
	public R submit(@Valid @RequestBody Lpn lpn) {
		return R.status(lpnService.saveOrUpdate(lpn));
	}


	/**
	 * 删除容器
	 */
	@ApiLog("容器-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除容器", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(lpnService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 导出
	 */
	@ApiLog("容器-导出")
	@GetMapping("export")
	@ApiOperation(value = "导出", notes = "查询条件")
	public void export(@ApiIgnore @RequestParam HashMap<String, Object> params, HttpServletResponse response) {
		lpnService.exportExcel(params, response);
	}

	/**
	 * 导出模板
	 */
	@ApiLog("容器-导出模板")
	@GetMapping("export-template")
	@ApiOperation(value = "导出模板")
	public void exportTemplate(HttpServletResponse response) {
		List<LpnExcel> lpnExportList = new ArrayList<>();
		ExcelUtil.export(response, "容器", "容器数据表", lpnExportList, LpnExcel.class);
	}

	/**
	 * 导入验证
	 */
	@ApiLog("容器-导入验证")
	@PostMapping("import-valid")
	@ApiOperation(value = "导入验证")
	public R<List<DataVerify>> importValid(MultipartFile file) {
		return R.data(lpnService.validExcel(ExcelUtil.read(file, LpnExcel.class)));
	}

	/**
	 * 导入验证通过的数据
	 */
	@ApiLog("容器-导入数据")
	@PostMapping("import-data")
	@ApiOperation(value = "导入数据")
	public R<Boolean> importData(@RequestBody List<DataVerify> dataVerifyList) {
		return R.data(lpnService.importData(dataVerifyList));
	}
}
