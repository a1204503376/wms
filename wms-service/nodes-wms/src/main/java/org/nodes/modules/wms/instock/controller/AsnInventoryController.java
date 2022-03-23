package org.nodes.modules.wms.instock.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.instock.inventory.entity.AsnInventory;
import org.nodes.wms.core.instock.inventory.vo.AsnInventoryVO;
import org.nodes.wms.core.instock.inventory.wrapper.AsnInventoryWrapper;
import org.nodes.wms.core.instock.inventory.dto.AsnInventoryDTO;
import org.nodes.wms.core.instock.inventory.service.IAsnInventoryService;
import org.springblade.core.boot.ctrl.BladeController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;

/**
 * 收货清单头表 控制器
 *
 * @author NodeX
 * @since 2021-06-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/instock/asn-inventory")
@Api(value = "收货清单头表", tags = "收货清单头表接口")
public class AsnInventoryController extends BladeController {

	private IAsnInventoryService asnInventoryService;

	/**
	 * 收货清单头表详情
	 */
	@GetMapping("/detail")
	@ApiOperation(value = "收货清单头表详情", notes = "传入asnInventory")
	public R<AsnInventoryVO> detail(AsnInventoryDTO asnInventory) {
		return R.data(asnInventoryService.getDetail(asnInventory.getId()));
	}

	/**
	 * 收货清单头表分页
	 */
	@GetMapping("/page")
	@ApiOperation(value = "收货清单头表分页", notes = "传入asnInventory")
	public R<IPage<AsnInventoryVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<AsnInventory> pages = asnInventoryService.page(Condition.getPage(query), Condition.getQueryWrapper(params, AsnInventory.class));
		return R.data(AsnInventoryWrapper.build().pageVO(pages));
	}
}
