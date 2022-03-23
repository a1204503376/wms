
package org.nodes.modules.wms.warehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.core.warehouse.dto.PlatformInfoDTO;
import org.nodes.wms.core.warehouse.entity.PlatformInfo;
import org.nodes.wms.core.warehouse.excel.PlatformInfoExcel;
import org.nodes.wms.core.warehouse.service.IPlatformInfoService;
import org.nodes.wms.core.warehouse.vo.PlatformInfoVO;
import org.nodes.wms.core.warehouse.wrapper.PlatforminfoWrapper;
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
 * 月台控制器
 *
 * @author liangmei
 * @since 2019-12-06
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/warehouse/platforminfo")
@Api(value = "月台管理", tags = "月台管理")
public class PlatformInfoController extends BladeController {

	private IPlatformInfoService platformInfoService;

	/**
	 * 月台详情
	 */
	@ApiLog("月台-详情")
	@GetMapping("/detail")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "spiId", value = "月台ID", paramType = "query", dataType = "string")
	})
	@ApiOperation(value = "月台详情", notes = "传入platformInfo")
	public R<PlatformInfo> detail(@ApiIgnore @RequestParam Map<String, Object> platformInfo) {
		PlatformInfo detail = platformInfoService.getOne(Condition.getQueryWrapper(platformInfo, PlatformInfo.class));
		return R.data(PlatforminfoWrapper.build().entityVO(detail));
	}

	/**
	 * 列表
	 */
	@ApiLog("月台-列表")
	@GetMapping("/list")
	@ApiOperation(value = "月台管理分页", notes = "传入platformInfo")
	public R<List<PlatformInfoVO>> list(PlatformInfoDTO platformInfo) {
		QueryWrapper<PlatformInfo> queryWrapper = Condition.getQueryWrapper(new PlatformInfo());
		if (Func.isNotEmpty(platformInfo.getPlatformCode())) {
			queryWrapper.lambda().eq(PlatformInfo::getPlatformCode, platformInfo.getPlatformCode());
		}
		if (Func.isNotEmpty(platformInfo.getPlatformName())) {
			queryWrapper.lambda().eq(PlatformInfo::getPlatformName, platformInfo.getPlatformName());
		}
		if (Func.isNotEmpty(platformInfo.getWhId())) {
			queryWrapper.lambda().eq(PlatformInfo::getWhId, platformInfo.getWhId());
		}
		if (Func.isNotEmpty(platformInfo.getWhId())) {
			queryWrapper.lambda().eq(PlatformInfo::getWhId, platformInfo.getWhId());
		}
		if (Func.isNotEmpty(platformInfo.getPlatformTypeDesc())) {
			List<Integer> typeList = Func.toIntList(platformInfo.getPlatformTypeDesc());
			queryWrapper.lambda().in(PlatformInfo::getPlatformType, typeList);
		} else if (Func.isNotEmpty(platformInfo.getPlatformType())) {
			queryWrapper.lambda().eq(PlatformInfo::getPlatformType, platformInfo.getPlatformType());
		}
		List<PlatformInfo> list = platformInfoService.list(queryWrapper);
		return R.data(PlatforminfoWrapper.build().listVO(list));
	}

	/**
	 * 分页
	 */
	@ApiLog("月台-分页")
	@GetMapping("/page")
	@ApiOperation(value = "月台管理分页", notes = "传入platformInfo")
	public R<IPage<PlatformInfoVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<PlatformInfo> pages = platformInfoService.page(Condition.getPage(query), Condition.getQueryWrapper(params, PlatformInfo.class));
		return R.data(PlatforminfoWrapper.build().pageVO(pages));
	}

	/**
	 * 新增或修改月台
	 */
	@ApiLog("月台-提交")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改月台", notes = "传入platformInfo")
	public R submit(@Valid @RequestBody PlatformInfo platformInfo) {
		return R.status(platformInfoService.saveOrUpdate(platformInfo));
	}


	/**
	 * 删除月台
	 */
	@ApiLog("月台-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除月台", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(platformInfoService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 导出
	 */
	@ApiLog("月台-导出")
	@GetMapping("export")
	@ApiOperation(value = "导出", notes = "查询条件")
	public void export(@ApiIgnore @RequestParam HashMap<String, Object> params, HttpServletResponse response) {
		platformInfoService.exportExcel(params, response);
	}

	/**
	 * 导出模板
	 */
	@ApiLog("月台-导出模板")
	@GetMapping("export-template")
	@ApiOperation(value = "导出模板")
	public void exportTemplate(HttpServletResponse response) {
		List<PlatformInfoExcel> platExportList = new ArrayList<>();
		ExcelUtil.export(response, "月台", "月台数据表", platExportList, PlatformInfoExcel.class);
	}

	/**
	 * 导入验证
	 */
	@ApiLog("月台-导入验证")
	@PostMapping("import-valid")
	@ApiOperation(value = "导入验证")
	public R<List<DataVerify>> importValid(MultipartFile file) {
		return R.data(platformInfoService.validExcel(ExcelUtil.read(file, PlatformInfoExcel.class)));
	}

	/**
	 * 导入验证通过的数据
	 */
	@ApiLog("月台-导入数据")
	@PostMapping("import-data")
	@ApiOperation(value = "导入数据")
	public R<Boolean> importData(@RequestBody List<DataVerify> dataVerifyList) {
		return R.data(platformInfoService.importData(dataVerifyList));
	}
}
