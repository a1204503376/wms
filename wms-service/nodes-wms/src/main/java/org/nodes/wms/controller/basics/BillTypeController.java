package org.nodes.wms.controller.basics;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.wms.biz.basics.billType.BillTypeBiz;
import org.nodes.wms.core.basedata.entity.BillType;
import org.nodes.wms.core.basedata.service.IBillTypeService;
import org.nodes.wms.core.basedata.vo.BillTypeVo;
import org.nodes.wms.core.basedata.wrapper.BillTypeWrapper;
import org.nodes.wms.dao.basics.billType.dto.BillTypeSelectQuery;
import org.nodes.wms.dao.basics.billType.dto.BillTypeSelectResponse;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

import static org.nodes.wms.core.basedata.cache.BillTypeCache.BILL_TYPE_CACHE;

/**
 * 单据类型 控制器
 *
 * @author zx
 * @since 2019-12-24
 */
@RestController
@AllArgsConstructor
@ApiIgnore
@RequestMapping("/wms/basedata/billtype")
@Api(value = "单据类型", tags = "单据类型接口")
public class BillTypeController extends BladeController {

	private IBillTypeService billTypeService;

	private BillTypeBiz billTypeBiz;

	/**
	 * 详情
	 */
	@ApiLog("单据类型接口-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入billType")
	public R<BillTypeVo> detail(BillType billType) {
		BillType detail = billTypeService.getOne(Condition.getQueryWrapper(billType));
		return R.data(BillTypeWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 单据类型
	 */
	@ApiLog("单据类型接口-分页")
	@GetMapping("/list")
	@ApiOperation(value = "分页", notes = "传入billType")
	public R<List<BillType>> list(BillType billType) {
		return R.data(billTypeService.list(Condition.getQueryWrapper(billType)));
	}

	/**
	 * 自定义分页 单据类型
	 */
	@ApiLog("单据类型接口-自定义分页")
	@GetMapping("/page")
	@ApiOperation(value = "分页", notes = "传入billType")
	public R<IPage<BillTypeVo>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<BillType> pages = billTypeService.page(Condition.getPage(query), Condition.getQueryWrapper(params,BillType.class));
		return R.data(BillTypeWrapper.build().pageVO(pages));
	}

	/**
	 * 新增或修改 单据类型
	 */
	@ApiLog("单据类型接口-新增或修改")
	@PostMapping("/submit")
	@ApiIgnore
	@ApiOperation(value = "新增或修改", notes = "传入billType")
	public R submit(@Valid @RequestBody BillType billType) {
		CacheUtil.clear(BILL_TYPE_CACHE);
		return R.status(billTypeService.saveOrUpdate(billType));
	}


	/**
	 * 删除 单据类型
	 */
	@ApiLog("单据类型接口-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		CacheUtil.clear(BILL_TYPE_CACHE);
		return R.status(billTypeService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 单据类型选择下拉框
	 * 展示最近10个物品
	 */
	@PostMapping("/select")
	public R<List<BillTypeSelectResponse>> getBillTypeSelectResponseTop10List(@RequestBody BillTypeSelectQuery billTypeSelectQuery){
		List<BillTypeSelectResponse> selectResponseList = billTypeBiz.getBillTypeSelectResponseTop10List(billTypeSelectQuery);
		return R.data(selectResponseList);
	}
}


