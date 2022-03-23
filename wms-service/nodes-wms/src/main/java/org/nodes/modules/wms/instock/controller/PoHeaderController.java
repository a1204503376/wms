package org.nodes.modules.wms.instock.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

import org.nodes.wms.core.instock.asn.cache.AsnCache;
import org.nodes.wms.core.instock.purchase.dto.PoHeaderQueryDTO;
import org.nodes.wms.core.instock.purchase.entity.PoHeader;
import org.nodes.wms.core.instock.purchase.wrapper.PoHeaderWrapper;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.instock.purchase.vo.PoHeaderVO;
import org.nodes.wms.core.instock.purchase.dto.PoHeaderDTO;
import org.nodes.wms.core.instock.purchase.service.IPoHeaderService;
import org.springblade.core.boot.ctrl.BladeController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 采购单头表 控制器
 *
 * @author NodeX
 * @since 2021-05-31
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/instock/po-header")
@Api(value = "采购单头表", tags = "采购单头表接口")
public class PoHeaderController extends BladeController {

	private IPoHeaderService poHeaderService;

	/**
	 * 采购单头表详情
	 */
	@ApiLog("采购单-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "采购单头表详情", notes = "传入poHeader")
	public R<PoHeaderVO> detail(PoHeaderDTO poHeader) {
		return R.data(poHeaderService.getDetail(poHeader.getPoBillId()));
	}

    /**
     * 采购单头表列表
     */
	@ApiLog("采购单-列表")
    @GetMapping("/list")
    @ApiOperation(value = "采购单头表列表", notes = "传入poHeader")
    public R<List<PoHeaderVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<PoHeader> list = poHeaderService.list(Condition.getQueryWrapper(params, PoHeader.class));
		return R.data(PoHeaderWrapper.build().listVO(list));
    }

	/**
	 * 采购单头表分页
	 */
	@ApiLog("采购单-分页")
	@GetMapping("/page")
	@ApiOperation(value = "采购单头表分页", notes = "传入poHeader")
	public R<IPage<PoHeaderVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<PoHeader> page = poHeaderService.page(Condition.getPage(query), Condition.getQueryWrapper(params, PoHeader.class));
		return R.data(PoHeaderWrapper.build().pageVO(page));
	}


	/**
	 * 采购单头表新增或修改
	 */
	@ApiLog("采购单-提交")
	@PostMapping("/submit")
	@ApiOperation(value = "采购单头表新增或修改", notes = "传入poHeader")
	public R submit(@Valid @RequestBody PoHeaderDTO poHeader) {
		return R.status(poHeaderService.saveOrUpdate(poHeader));
	}


	/**
	 * 采购单头表删除
	 */
	@ApiLog("采购单-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "采购单头表删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(poHeaderService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 获得采购单编号
	 *
	 * @return
	 */
	@ApiLog("采购单-编号")
	@GetMapping("/getPoBillNo")
	@ApiOperation(value = "获得采购单编号", notes = "")
	public R<String> getPoBillNo() {
		return R.data(AsnCache.getPoBillNo());
	}

	/**
	 * 创建入库单
	 * @param poHeader 采购单
	 * @return 是否成功
	 */
	@ApiLog("采购单-创建入库单")
	@PostMapping("/create-asn")
	@ApiOperation(value = "根据采购订单生成入库单", notes = "传入poHeader")
	public R createAsn(@Valid @RequestBody PoHeaderDTO poHeader) {
		return R.status(poHeaderService.createAsn(poHeader));
	}
}
