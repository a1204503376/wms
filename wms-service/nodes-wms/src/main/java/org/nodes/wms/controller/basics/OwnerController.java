package org.nodes.wms.controller.basics;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.biz.basics.owner.OwnerBiz;
import org.nodes.wms.core.basedata.dto.OwnerDTO;
import org.nodes.wms.core.basedata.entity.Owner;
import org.nodes.wms.core.basedata.excel.OwnerExcel;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.basedata.vo.OwnerVO;
import org.nodes.wms.core.basedata.wrapper.OwnerWrapper;
import org.nodes.wms.dao.basics.owner.dto.OwnerSelectResponse;
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

/**
 * @author zhugx
 * @program 货主管理
 * @description 货主管理控制器
 * @create 20191205
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "owner")
@Api(value = "货主管理", tags = "货主管理")
public class OwnerController extends BladeController {

	private final IOwnerService ownerService;

	private final OwnerBiz ownerBiz;

	@GetMapping("/ownerSelect")
	public R<List<OwnerSelectResponse>> getOwnerSelectResponseList(){
		List<OwnerSelectResponse> ownerSelectResponseList = ownerBiz.getOwnerSelectResponseList();
		return R.data(ownerSelectResponseList);
	}

	/**
	 * @program 货主ID
	 * @description 查询货主详细信息
	 * @author zhugx
	 * @create 20191205
	 */
	@ApiLog("货主管理-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入owner的主键ID")
	public R<OwnerVO> detail(Long woId) {
		OwnerVO detail = OwnerWrapper.build().entityVO(ownerService.getById(woId));
		return R.data(detail);
	}

	/**
	 * 列表
	 */
	@ApiLog("货主管理-列表")
	@GetMapping("/list")
	@ApiOperation(value = "列表", notes = "传入owner")
	public R<List<OwnerVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		return R.data(OwnerWrapper.build().listVO(ownerService.list(Condition.getQueryWrapper(params, Owner.class))));
	}
	/**
	 * 下拉数据源
	 */
	@ApiLog("货主管理-下拉数据源")
	@GetMapping("/select")
	@ApiOperation(value = "下拉数据源", notes = "传入woId")
	public R<List<Owner>> select(Owner owner) {
		QueryWrapper<Owner> queryWrapper = Condition.getQueryWrapper(owner);
		List<Owner> list = ownerService.list(queryWrapper);
		return R.data(list);
	}
	/**
	 * 分页
	 */
	@ApiLog("货主管理-分页")
	@GetMapping("/page")
	@ApiOperation(value = "分页", notes = "传入owner")
	public R<IPage<OwnerVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		return R.data(OwnerWrapper.build().pageVO(ownerService.page(Condition.getPage(query), Condition.getQueryWrapper(params, Owner.class))));
	}

	/**
	 * 新增或修改
	 */
	@ApiLog("货主管理-新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入owner")
	public R submit(@Valid @RequestBody OwnerDTO ownerDTO) {
		return R.data(ownerService.saveOrUpdate(ownerDTO));
	}


	/**
	 * 删除
	 */
	@ApiLog("货主管理-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(ownerService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 导出
	 */
	@ApiLog("货主管理-导出")
	@GetMapping("export")
	@ApiOperation(value = "导出", notes = "查询条件")
	public void export(@ApiIgnore @RequestParam HashMap<String, Object> params, HttpServletResponse response) {
		ownerService.exportExcel(params, response);
	}

	/**
	 * 导出模板
	 */
	@ApiLog("货主管理-导出模板")
	@GetMapping("export-template")
	@ApiOperation(value = "导出模板")
	public void exportTemplate(HttpServletResponse response) {
		List<OwnerExcel> ownerExportList = new ArrayList<>();
		ExcelUtil.export(response, "货主", "货主数据表", ownerExportList, OwnerExcel.class);
	}

	/**
	 * 导入验证
	 */
	@ApiLog("货主管理-导入验证")
	@PostMapping("import-valid")
	@ApiOperation(value = "导入验证")
	public R<List<DataVerify>> importValid(MultipartFile file) {
		return R.data(ownerService.validExcel(ExcelUtil.read(file, OwnerExcel.class)));
	}

	/**
	 * 导入验证通过的数据
	 */
	@ApiLog("货主管理-导入数据")
	@PostMapping("import-data")
	@ApiOperation(value = "导入数据")
	public R<Boolean> importData(@RequestBody List<DataVerify> dataVerifyList) {
		return R.data(ownerService.importData(dataVerifyList));
	}
}
