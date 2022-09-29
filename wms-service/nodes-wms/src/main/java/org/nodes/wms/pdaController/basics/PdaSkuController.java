package org.nodes.wms.pdaController.basics;

import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.sku.dto.input.FindSkuByCodeRequest;
import org.nodes.wms.dao.basics.sku.dto.input.FindSkuIsSnBySkuCodeRequest;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.dao.stock.dto.input.FindIsSnByStockIdRequest;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 收货管理API
 *
 * @author nodesc
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/sku")
public class PdaSkuController {
	private final SkuBiz skuBiz;
	private final LocationBiz locationBiz;
	private final StockQueryBiz stockQueryBiz;

	/**
	 * PDA查询型号下拉框组件
	 *
	 * @return 型号集合
	 */
	@GetMapping("/findSkuDropDownBox")
	public R<List<String>> findSkuDropDownBox() {
		List<String> dropDownBox = skuBiz.getSkuDropDownBox();
		return R.data(dropDownBox);
	}

	/**
	 * PDA根据物料编码查询型号下拉框组件
	 *
	 * @param request 包含物料编码
	 * @return 物品型号集合
	 */
	@PostMapping("/findSkuByCode")
	public R<List<String>> findSkuByCode(@RequestBody FindSkuByCodeRequest request) {
		List<Sku> skus = skuBiz.selectSkuListByNo(request.getNo());
		List<String> skuList = skus.stream()
			.map(Sku::getSkuSpec)
			.filter(Func::isNotEmpty)
			.distinct()
			.collect(Collectors.toList());
		return R.data(skuList);
	}

	/**
	 * PDA查询当前物品是否序列号管理
	 *
	 * @return 当前物品是否序列号
	 */
	@GetMapping("/findSkuIsSnBySkuCode")
	public R<Boolean> findSkuIsSnBySkuCode(FindSkuIsSnBySkuCodeRequest request) {
		//根据skuCode获取SKU对象
		Sku sku = skuBiz.findByCode(request.getSkuCode());
		//根据locCode获取Location对象
		Location sourceLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
		List<Long> locationIdList = new ArrayList<>();
		locationIdList.add(sourceLocation.getLocId());
		SkuLotBaseEntity skuLot = new SkuLotBaseEntity();
		skuLot.setSkuLot1(request.getSkuLot1());
		//根据库房ID SKU_ID 库位 和批属性1查询对应库存
		List<Stock> stockList = stockQueryBiz.findEnableStockByLocationAndSkuLot(request.getWhId(), sku.getSkuId(), null, locationIdList, skuLot);
		Stock stock = stockList.stream()
			.filter(stocks -> request.getSkuLot1().equals(stocks.getSkuLot1()))
			.findFirst()
			.orElse(null);
		AssertUtil.notNull(stock, "查询失败，找不到对应库存,请确认数据正确后重试");
		assert stock != null;
		return R.data(stock.isSerial());
	}

	/**
	 * PDA查询当前物品是否序列号管理
	 *
	 * @return 当前物品是否序列号
	 */
	@GetMapping("/findIsSnByStockId")
	public R<Boolean> findIsSnByStockId(FindIsSnByStockIdRequest request) {
		Stock stock;
		if (Func.isEmpty(request.getStockId())) {
			List<Stock> stockList = stockQueryBiz.findEnableStockByBoxCode(request.getBoxCode());
			stock = stockList.stream()
				.filter(stocks -> request.getSkuLot1().equals(stocks.getSkuLot1())
					&& request.getLocCode().equals(stocks.getLocCode())
					&& request.getSkuCode().equals(stocks.getSkuCode())
					&& BigDecimalUtil.gt(stocks.getStockBalance(), request.getQty()))
				.findFirst()
				.orElse(null);

		} else {
			stock = stockQueryBiz.findStockById(request.getStockId());
		}
		AssertUtil.notNull(stock, "根据您输入的数据匹配不到对应库存，请检查您输入的数据后重试");
		assert stock != null;
		return R.data(stock.isSerial());
	}


}
