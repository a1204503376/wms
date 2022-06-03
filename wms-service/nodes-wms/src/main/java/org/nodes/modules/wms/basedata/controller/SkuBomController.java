package org.nodes.modules.wms.basedata.controller;

import com.sun.istack.NotNull;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

import org.nodes.wms.core.basedata.service.ISkuBomService;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.dao.basics.bom.entites.SkuBom;
import org.nodes.wms.core.basedata.vo.SkuBomVO;
import org.nodes.wms.core.basedata.wrapper.SkuBomWrapper;
import org.nodes.wms.core.basedata.dto.SkuBomDTO;
import org.springblade.core.boot.ctrl.BladeController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 物料清单 控制器
 *
 * @author NodeX
 * @since 2021-05-19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/basedata/skubom")
@Api(value = "物料清单", tags = "物料清单接口")
public class SkuBomController extends BladeController {

	private ISkuBomService skuBomService;

	/**
	 * 物料清单详情
	 */
	@GetMapping("/detail")
	@ApiOperation(value = "物料清单详情", notes = "传入skuBom")
	public R<SkuBomVO> detail(@NotNull SkuBomDTO skuBom) {
		SkuBom detail = (SkuBom) skuBomService.getOne(Condition.getQueryWrapper(skuBom));
		return R.data(SkuBomWrapper.build().entityVO(detail));
	}

    /**
     * 物料清单列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "物料清单列表", notes = "传入skuBom")
    public R<List<SkuBomVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<SkuBom> list = skuBomService.list(Condition.getQueryWrapper(params, SkuBom.class));
		return R.data(SkuBomWrapper.build().listVO(list));
    }

	/**
	 * 物料清单分页
	 */
	@GetMapping("/page")
	@ApiOperation(value = "物料清单分页", notes = "传入skuBom")
	public R<IPage<SkuBomVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<SkuBom> pages = skuBomService.page(Condition.getPage(query), Condition.getQueryWrapper(params, SkuBom.class));
		return R.data(SkuBomWrapper.build().pageVO(pages));
	}


	/**
	 * 物料清单新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperation(value = "物料清单新增或修改", notes = "传入skuBom")
	public R submit(@Valid @RequestBody SkuBom skuBom) {
		if (skuBom.getSkuId().equals(skuBom.getJoinSkuId())) {
			return R.fail("组合物品不能相同");
		}
		return R.data(skuBomService.saveOUpdate(skuBom));
	}


	/**
	 * 物料清单删除
	 */
	@PostMapping("/remove")
	@ApiOperation(value = "物料清单删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(skuBomService.removeByIds(Func.toLongList(ids)));
	}


}
