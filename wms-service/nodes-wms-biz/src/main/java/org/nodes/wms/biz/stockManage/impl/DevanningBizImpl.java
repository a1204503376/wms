package org.nodes.wms.biz.stockManage.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.ExceptionUtil;
import org.nodes.core.udf.UdfEntity;
import org.nodes.wms.biz.basics.lpntype.LpnTypeBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.biz.outstock.plan.SoPickPlanBiz;
import org.nodes.wms.biz.outstock.so.SoBillBiz;
import org.nodes.wms.biz.stock.SerialBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.biz.stockManage.DevanningBiz;
import org.nodes.wms.biz.stockManage.module.DevanningAction;
import org.nodes.wms.biz.stockManage.module.DevanningActionFactory;
import org.nodes.wms.biz.task.WmsTaskBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.lpntype.enums.LpnTypeCodeEnum;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.stock.StockDao;
import org.nodes.wms.dao.stock.dto.input.DevanningSubmitRequest;
import org.nodes.wms.dao.stock.dto.input.FindAllSerialNumberManageRequest;
import org.nodes.wms.dao.stock.dto.output.DevanningStockResponse;
import org.nodes.wms.dao.stock.dto.output.FindAllSerialNumberManageResponse;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskProcTypeEnum;
import org.nodes.wms.dao.task.enums.WmsTaskTypeEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 拆箱相关业务
 *
 * @author admin
 */
@Service
@RequiredArgsConstructor
public class DevanningBizImpl implements DevanningBiz {
	private final StockQueryBiz stockQueryBiz;
	private final StockBiz stockBiz;
	private final SerialBiz serialBiz;
	private final LpnTypeBiz lpnTypeBiz;
	private final LocationBiz locationBiz;
	private final WmsTaskBiz wmsTaskBiz;
	private final SoPickPlanBiz soPickPlanBiz;
	private final SoBillBiz soBillBiz;
	private final ZoneBiz zoneBiz;
	private final StockDao stockDao;
	private final DevanningActionFactory devanningActionFactory;

	@Override
	public FindAllSerialNumberManageResponse getAllSerialNumberManage(FindAllSerialNumberManageRequest request) {
		FindAllSerialNumberManageResponse response = new FindAllSerialNumberManageResponse();
		// 根据箱码查询库存
		List<Stock> stockList = stockQueryBiz.findEnableStockByBoxCode(request.getBoxCode());
		if (Func.isNull(stockList) || stockList.size() == 0) {
			throw new ServiceException("拆箱失败，拆箱根据箱码查询库存失败");
		}
		// 返回是否是序列号管理----根据序列号列表是否为空进行判断，如果不是则需要查询出所有跟当前托盘有关的库存进行返回
		List<String> serialNumberList = new ArrayList<>();
		for (Stock stock : stockList) {
			List<String> serialBizSerialNoByStockId = serialBiz.findSerialNoByStockId(stock.getStockId());
			if (Func.isNotEmpty(serialBizSerialNoByStockId)) {
				serialNumberList.addAll(serialBizSerialNoByStockId);
			}
		}

		if (Func.isNotEmpty(serialNumberList)) {
			// 序列号不为空赋值响应对象-序列号集合赋值、并且将序列号集合判断变为true、将库存响应对象置空
			response.setIsSn(true);
			response.setSerialNumberList(serialNumberList);
			response.setStockList(null);
		} else {
			// 序列号为空赋值响应对象-序列号集合置空、并且将序列号集合判断变为false、库存响应对象集合
			response.setIsSn(false);
			response.setSerialNumberList(null);
			List<DevanningStockResponse> devanningStockList = BeanUtil.copy(stockList, DevanningStockResponse.class);
			response.setStockList(devanningStockList);
		}
		return response;
	}

	private String generateNewBoxCode(DevanningSubmitRequest request) {
		LpnTypeCodeEnum lpnTypeCodeEnum = lpnTypeBiz.tryParseBoxCode(request.getBoxCode());
		if (LpnTypeCodeEnum.UNKNOWN.equals(lpnTypeCodeEnum)) {
			throw new ServiceException(String.format("拆箱时生成新箱码错误,箱码[%s]没有对应的箱型", request.getBoxCode()));
		}

		String skuName = null;
		String spec = null;
		if (request.getIsSn()) {
			if (request.getSerialNumberList().size() <= 0) {
				throw new ServiceException(String.format(
					"拆箱时生成新箱码错误,箱码[%s]对应的库存有序列号但没有采集到需拆箱的序列号", request.getBoxCode()));
			}
			// 天宜：带序列号的库存箱子中只对应一个物品
			Serial serial = serialBiz.findSerialSerialNo(request.getSerialNumberList().get(0));
			Stock stock = stockQueryBiz.findStockById(serial.getStockId());
			skuName = stock.getSkuName();
			spec = stock.getSkuLot2();
		} else {
			if (request.getStockList().size() <= 0) {
				throw new ServiceException(String.format("拆箱时生成新箱码错误,箱码[%s]请求参数为空", request.getBoxCode()));
			}
			// 根据skuCode获取sku，获取skuName
			List<Stock> stockList = new ArrayList<>();
			for (DevanningStockResponse stockRequestItem : request.getStockList()) {
				Stock stock = stockQueryBiz.findStockById(stockRequestItem.getStockId());
				if (Func.isNull(stock)) {
					throw new ServiceException(String.format("拆箱时生成新箱码错误,箱码[%s]stockID[%d]查询不到库存",
						request.getBoxCode(), stockRequestItem.getStockId()));
				}
				stockList.add(stock);
			}
			skuName = StringUtil.join(stockList.stream().map(Stock::getSkuName).distinct().collect(Collectors.toList()), ",");
			spec = stockList.get(0).getSkuLot2();
		}

		return lpnTypeBiz.generateLpnCode(lpnTypeCodeEnum.getCode(), skuName, spec);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void devanning(DevanningSubmitRequest request) {
		// 校验目标库位是否正常；如果是序列号的需要校验参数是否争取
		Location targetLoc = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
		if (!targetLoc.enableStock()) {
			throw ExceptionUtil.mpe("拆箱失败,目标库位[{}]不能上架库存,请检查目标库位状态", request.getLocCode());
		}
		if (locationBiz.isAgvLocation(targetLoc)) {
			throw ExceptionUtil.mpe("拆箱失败,目标库位[{}]不能是自动区库位", request.getLocCode());
		}

		// 将旧库存拆成新库存
		List<DevanningAction> devanningActions = devanningActionFactory.create(request);
		String newBoxCode = null;
		if (request.getNewBoxCode()) {
			newBoxCode = generateNewBoxCode(request);
		}
		String newLpnCode = newBoxCode;
		UdfEntity udfEntity = new UdfEntity();
		udfEntity.setUdf2(request.getBoxCode());

		Map<Stock, Stock> oldStock2NewStock = new HashMap<>();
		for (DevanningAction item : devanningActions) {
			Location sourceLoc = locationBiz.findByLocId(item.getStock().getLocId());
			if (locationBiz.isAgvLocation(sourceLoc)) {
				throw ExceptionUtil.mpe("拆箱失败,原库存库位[{}]不能是自动区库位", sourceLoc.getLocCode());
			}
			Stock newStock = stockBiz.moveStock(item.getStock(), item.getSerialNoList(), item.getQty(), newBoxCode, newLpnCode,
				targetLoc, StockLogTypeEnum.STOCK_DEVANNING_BY_PDA, null, null, null, udfEntity);
			oldStock2NewStock.put(item.getStock(), newStock);
		}

		// 判断就库存是否有关联拣货计划，如果有需要重新分配
		List<SoPickPlan> soPickPlanList = soPickPlanBiz.findSoPickPlanByBoxCode(request.getBoxCode());
		if (Func.isNotEmpty(soPickPlanList)) {
			updateSoPickPlanAndTaskOnDevanning(soPickPlanList, oldStock2NewStock);
		}
	}

	private void updateSoPickPlanAndTaskOnDevanning(List<SoPickPlan> soPickPlanList,
													Map<Stock, Stock> oldStock2NewStock) {
		for (SoPickPlan soPickPlanItem : soPickPlanList) {
			SoDetail soDetail = soBillBiz.getSoDetailById(soPickPlanItem.getSoDetailId());
			Stock oldStock = oldStock2NewStock.keySet().stream()
				.filter(item -> item.getStockId().equals(soPickPlanItem.getStockId()))
				.findFirst().orElse(null);
			if (Func.isNull(oldStock)) {
				continue;
			}
			Stock newStock = oldStock2NewStock.get(oldStock);

			if (BigDecimalUtil.le(newStock.getStockBalance(), soDetail.getSurplusQty())
				&& BigDecimalUtil.gt(oldStock.getStockBalance(), soDetail.getSurplusQty())) {
				soPickPlanBiz.updatePlanOfStock(soPickPlanItem, newStock, oldStock);
			}
		}

		SoHeader soHeader = soBillBiz.getSoHeaderById(soPickPlanList.get(0).getSoBillId());
		WmsTask wmsTask = wmsTaskBiz.create(WmsTaskTypeEnum.PICKING, WmsTaskProcTypeEnum.BY_BOX, soPickPlanList, soHeader);
		soPickPlanBiz.setTaskId(soPickPlanList, wmsTask.getTaskId());
	}


}
