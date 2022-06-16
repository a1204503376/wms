package org.nodes.wms.controller.basics;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.CommonConstant;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.biz.basics.skuType.SkuTypeBiz;
import org.nodes.wms.core.basedata.dto.SkuTypeDTO;
import org.nodes.wms.dao.basics.skuType.dto.input.SkuTypeAddOrEditRequest;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;
import org.nodes.wms.core.basedata.excel.SkuTypeExcel;
import org.nodes.wms.core.basedata.service.ISkuTypeService;
import org.nodes.wms.core.basedata.vo.SkuTypeVO;
import org.nodes.wms.core.basedata.wrapper.SkuTypeWrapper;
import org.springblade.core.boot.ctrl.BladeController;
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
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author wanglei
 * @program 物品分类管理
 * @description 物品分类管理控制器
 * @create 20191128
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/wms/basedata/skutype")
public class SkuTypeController extends BladeController {

	/**
	 * 物品分类服务接口
	 */
	private final ISkuTypeService skuTypeService;

	private final SkuTypeBiz skuTypeBiz;

	/**
	 * 详情
	 */
	@ApiLog("物品分类-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入skuType")
	public R<SkuTypeVO> detail(SkuType skuType) {
		SkuType detail = skuTypeService.getOne(Condition.getQueryWrapper(skuType));

		return R.data(SkuTypeWrapper.build().entityVO(detail));
	}

	/**
	 * 列表
	 */
	@ApiLog("物品分类-列表")
	@GetMapping("/list")
	@ApiOperation(value = "树形列表", notes = "树形列表")
	public R<List<SkuTypeVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<SkuType> list = skuTypeService.list(Condition.getQueryWrapper(params, SkuType.class));
		List<SkuTypeVO> skuTypeList = SkuTypeWrapper.build().listVO(list);
		if (Func.isNotEmpty(skuTypeList)) {
			List<SkuType> childrenList = skuTypeService.list(Condition.getQueryWrapper(new SkuType())
				.lambda()
				.in(SkuType::getTypePreId, NodesUtil.toList(skuTypeList, SkuType::getSkuTypeId)));
			skuTypeList.forEach(item->{
				boolean hasChildren = childrenList.stream().filter(u->{
					return u.getTypePreId().equals(item.getSkuTypeId());
				}).count() > CommonConstant.TOP_PARENT_ID;
				item.setHasChildren(hasChildren);
			});
		}
		return R.data(skuTypeList);
	}

	/**
	 * @program 物品分类清单查询
	 * @description 物品分类清单查询返回树形结构
	 * @author wanglei
	 * @create 20191128
	 */
	@ApiLog("物品分类-分页")
	@GetMapping("/page")
	public R<IPage<SkuTypeVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<SkuType> page = skuTypeService.page(Condition.getPage(query), Condition.getQueryWrapper(params, SkuType.class)
			.lambda()
			.eq(SkuType::getTypePreId, CommonConstant.TOP_PARENT_ID));
		IPage<SkuTypeVO> skuTypePage = SkuTypeWrapper.build().pageVO(page);
		if (Func.isNotEmpty(skuTypePage) && Func.isNotEmpty(skuTypePage.getRecords())) {
			List<SkuType> childrenList = skuTypeService.list(Condition.getQueryWrapper(new SkuType())
				.lambda()
				.in(SkuType::getTypePreId, NodesUtil.toList(skuTypePage.getRecords(), SkuType::getSkuTypeId)));
			skuTypePage.getRecords().forEach(item->{
				boolean hasChildren = childrenList.stream().filter(u->{
					return u.getTypePreId().equals(item.getSkuTypeId());
				}).count() > CommonConstant.TOP_PARENT_ID;
				item.setHasChildren(hasChildren);
			});
		}
		return R.data(skuTypePage);
	}

	/**
	 * 树形列表
	 */
	@ApiLog("物品分类-树形列表")
	@GetMapping("/tree")
	@ApiOperation(value = "树形列表", notes = "树形列表")
	public R<List<SkuTypeVO>> tree(SkuType skuType) {
		List<SkuTypeVO> tree = skuTypeService.tree(skuType);
		return R.data(tree);
	}

	/**
	 * @program 物品分类新增或更新
	 * @description 物品分类新增或更新
	 * @author wanglei
	 * @create 20191128
	 */
	@ApiLog("物品分类-物品分类新增或更新")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改物品分类", notes = "传入物品分类实体(SkuType)")
	public R submit(@Validated @RequestBody SkuTypeDTO skuTypeDTO) {
		return R.status(skuTypeService.saveOrUpdate(skuTypeDTO));
	}

	/**
	 * 删除
	 */
	@ApiLog("物品分类-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(skuTypeService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 导出
	 */
	@ApiLog("物品分类管理-导出")
	@GetMapping("export")
	@ApiOperation(value = "导出", notes = "查询条件")
	public void export(@ApiIgnore @RequestParam HashMap<String, Object> params, HttpServletResponse response) {
		skuTypeService.exportExcel(params, response);
	}

	/**
	 * 导出模板
	 */
	@ApiLog("物品分类管理-导出模板")
	@GetMapping("export-template")
	@ApiOperation(value = "导出模板")
	public void exportTemplate(HttpServletResponse response) {
		List<SkuTypeExcel> skuTypeExcels = new ArrayList<>();
		ExcelUtil.export(response, "物品分类", "物品分类表", skuTypeExcels, SkuTypeExcel.class);
	}

	/**
	 * 导入验证
	 */
	@ApiLog("物品分类管理-导入验证")
	@PostMapping("import-valid")
	@ApiOperation(value = "导入验证")
	public R<List<DataVerify>> importValid(MultipartFile file) {
		return R.data(skuTypeService.validExcel(ExcelUtil.read(file, SkuTypeExcel.class)));
	}

	/**
	 * 导入验证通过的数据
	 */
	@ApiLog("物品分类管理-导入数据")
	@PostMapping("import-data")
	@ApiOperation(value = "导入数据")
	public R<Boolean> importData(@RequestBody List<DataVerify> dataVerifyList) {
		return R.data(skuTypeService.importData(dataVerifyList));
	}

	@ApiLog("物品分类管理-新增或修改")
	@PostMapping("/save")
	public R<String> save(@Valid @RequestBody SkuTypeAddOrEditRequest skuTypeAddOrEditRequest){
		SkuType skuType = skuTypeBiz.save(skuTypeAddOrEditRequest);
		return R.success(String.format("%s成功，分类编码：%s",
			Func.isEmpty(skuTypeAddOrEditRequest.getSkuTypeId()) ? "新增" : "修改",
			skuType.getTypeCode()));
	}
}
