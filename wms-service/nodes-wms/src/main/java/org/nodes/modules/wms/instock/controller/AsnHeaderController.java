package org.nodes.modules.wms.instock.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.core.instock.asn.cache.AsnCache;
import org.nodes.wms.core.instock.asn.dto.AsnHeaderDTO;
import org.nodes.wms.core.instock.asn.entity.AsnHeader;
import org.nodes.wms.core.instock.asn.excel.AsnHeaderExcel;
import org.nodes.wms.core.instock.asn.excel.SnExcel;
import org.nodes.wms.core.instock.asn.service.IAsnHeaderService;
import org.nodes.wms.core.instock.asn.vo.AsnHeaderVO;
import org.nodes.wms.core.instock.asn.wrapper.AsnHeaderWrapper;
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
 * 收货单头表 控制器
 *
 * @author zx
 * @since 2019-12-12
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/instock/asnHeader")
@Api(value = "收货单头表", tags = "收货单头表接口")
public class AsnHeaderController extends BladeController {

	//收货单头表服务层接口
	private IAsnHeaderService asnHeaderService;
	/**
	 * 批量完成收货单
	 *
	 * @param ids
	 * @return
	 */
	@ApiLog("收货单头表接口-批量完成收货单")
	@PostMapping(value = "/finishAsnBill")
	@ApiOperation(value = "结束收货单", notes = "传入入库单ID")
	public R finishAsnBill(@Valid @ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(asnHeaderService.finishAsnBill(ids));
	}
	/**
	 * 详情 收货单头表
	 */
	@ApiLog("收货单头表接口-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "收货单头表详情", notes = "传入入库单ID")
	public R<AsnHeaderVO> detail(@RequestParam Long asnBillId) {
		return R.data(asnHeaderService.getDetail(asnBillId));
	}

	/**
	 * 分页查询 收货单头表
	 */
	@ApiLog("收货单头表接口-分页")
	@GetMapping("/page")
	@ApiOperation(value = "分页", notes = "传入入库单查询条件,分页信息")
	public R<IPage<AsnHeaderVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<AsnHeader> page = asnHeaderService.page(Condition.getPage(query), Condition.getQueryWrapper(params, AsnHeader.class)
			.lambda());
//			.orderByDesc(AsnHeader::getPreCreateDate));
		return R.data(AsnHeaderWrapper.build().pageVO(page));
	}
	/**
	 * 批量取消订单
	 *
	 * @param ids 入库单ID，多个用英文逗号分隔
	 * @return
	 */
	@ApiLog("收货单头表接口-批量取消订单")
	@PostMapping("/cancel")
	@ApiOperation(value = "批量取消订单", notes = "入库单ID，多个用英文逗号分隔")
	public R cancel(@Valid @ApiParam(value = "入库单ID，多个用英文逗号分隔") @RequestParam String ids) {
		return R.data(asnHeaderService.cancel(Func.toLongList(ids)));
	}
	/**
	 * 查询收货单头表列表
	 */
	@ApiLog("收货单头表接口-收货单头表列表")
	@GetMapping("/list")
	@ApiOperation(value = "收货单头表列表", notes = "传入入库单查询条件")
	public R<List<AsnHeaderVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		return R.data(AsnHeaderWrapper.build().listVO(asnHeaderService.list(Condition.getQueryWrapper(params, AsnHeader.class))));
	}

	/**
	 * 新增或修改 收货单头表
	 */
	@ApiLog("收货单头表接口-收货单头表新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "收货单头表新增或修改", notes = "传入入库单对象")
	public R submit(@Valid @RequestBody AsnHeaderDTO asnHeader) {
		return R.status(asnHeaderService.saveOrUpdate(asnHeader));
	}

	/**
	 * 删除 收货单头表
	 */
	@ApiLog("收货单头表接口-收货单头表删除")
	@PostMapping("/remove")
	@ApiOperation(value = "收货单头表删除", notes = "传入IDS")
	public R remove(@Valid @ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(asnHeaderService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 获得收货单编号
	 *
	 * @return
	 */
	@ApiLog("收货单头表接口-获得收货单编号")
	@GetMapping("/getAsnBillNo")
	@ApiOperation(value = "获得收货单编号", notes = "")
	public R<String> getAsnBillNo() {
		return R.data(AsnCache.getAsnBillNo());
	}

	/**
	 * 获取订单是否允许编辑
	 *
	 * @param asnHeaderId
	 * @return
	 */
	@ApiLog("收货单头表接口-获取订单是否允许编辑")
	@GetMapping("/canEdit")
	@ApiOperation(value = "获取订单是否允许编辑", notes = "传入入库单ID")
	public R canEdit(@Valid @ApiParam(value = "入库单ID", required = true) @RequestParam Long asnHeaderId) {
		return R.data(asnHeaderService.canEdit(asnHeaderId));
	}

	/**
	 * 导出序列号模板
	 */
	@ApiLog("收货单头表接口-导出序列号模板")
	@GetMapping("sn/export-template")
	@ApiOperation(value = "导出序列号模板")
	public void exportSnTemplate(HttpServletResponse response) {
		List<SnExcel> snExcelList = new ArrayList<>();
		ExcelUtil.export(response, "订单序列号", "订单序列号数据表", snExcelList, SnExcel.class);
	}

	/**
	 * 序列号数据校验
	 *
	 * @param file
	 * @return
	 */
	@ApiLog("收货单头表接口-序列号数据校验")
	@PostMapping("sn/import-valid")
	@ApiOperation(value = "序列号数据校验", notes = "")
	public R<List<DataVerify>> serialDataVerify(MultipartFile file) {
		return R.data(asnHeaderService.validSnExcel(ExcelUtil.read(file, SnExcel.class)));
	}

	/**
	 * 序列号导入 保存上传的数据
	 *
	 * @param dataVerifyList 验证合格的数据
	 * @return
	 */
	@ApiLog("收货单头表接口-序列号导入保存上传的数据")
	@PostMapping("sn/import-data")
	@ApiOperation(value = "序列号导入 保存上传的数据", notes = "")
	public R<Boolean> upload(@RequestBody List<DataVerify> dataVerifyList) {
		return R.data(asnHeaderService.importSnData(dataVerifyList));
	}

	/**
	 * 导出
	 */
	@ApiLog("入库单-导出")
	@GetMapping("export")
	@ApiOperation(value = "导出", notes = "查询条件")
	public void exportAsn(@ApiIgnore @RequestParam HashMap<String, Object> params, HttpServletResponse response) {
		asnHeaderService.exportAsnExcel(params, response);
	}

	/**
	 * 导出模板
	 */
	@ApiLog("入库单-导出模板")
	@GetMapping("export-template")
	@ApiOperation(value = "导出模板")
	public void exportAsnTemplate(HttpServletResponse response) {
		List<AsnHeaderExcel> asnExportList = new ArrayList<>();
		ExcelUtil.export(response, "入库单", "入库单数据表", asnExportList, AsnHeaderExcel.class);
	}

	/**
	 * 导入验证
	 */
	@ApiLog("入库单-导入验证")
	@PostMapping("import-valid")
	@ApiOperation(value = "导入验证")
	public R<List<DataVerify>> importAsnValid(MultipartFile file) {
		if(!"入库单".equals(file.getOriginalFilename())){
			return R.fail("请上传入库单excel文件！");
		}
		return R.data(asnHeaderService.validAsnExcel(ExcelUtil.read(file, AsnHeaderExcel.class)));
	}

	/**
	 * 导入验证通过的数据
	 */
	@ApiLog("入库单-导入数据")
	@PostMapping("import-data")
	@ApiOperation(value = "导入数据")
	public R<Boolean> importAsnData(@RequestBody List<DataVerify> dataVerifyList) {
		return R.data(asnHeaderService.importAsnData(dataVerifyList));
	}
}
