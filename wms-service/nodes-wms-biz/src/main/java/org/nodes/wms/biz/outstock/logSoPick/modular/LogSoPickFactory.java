package org.nodes.wms.biz.outstock.logSoPick.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.PickByPcsRequest;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.stock.dto.output.PickByPcStockDto;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 拣货记录工厂
 *
 * @author nodesc
 */
@Component
@RequiredArgsConstructor
public class LogSoPickFactory {
	private final StockQueryBiz stockQueryBiz;
	private final SkuBiz skuBiz;

	/**
	 * PC拣货根据前端传入的参数生成拣货记录
	 *
	 * @param pickByPcStockDto 前端传入参数
	 * @param soHeader         收货单头表实体
	 * @param soDetail         收货单明细实体
	 * @param stock            stock
	 * @return 拣货记录实体
	 */
	public LogSoPick createLogSoPick(PickByPcStockDto pickByPcStockDto, SoHeader soHeader, SoDetail soDetail, Stock stock) {
		LogSoPick logSoPick = new LogSoPick();
		logSoPick.setLocId(pickByPcStockDto.getLocId());
		logSoPick.setLocCode(pickByPcStockDto.getLocCode());
		logSoPick.setSkuId(stock.getSkuId());
		logSoPick.setSkuCode(stock.getSkuCode());
		logSoPick.setSkuName(stock.getSkuName());
		logSoPick.setLotNumber(stock.getSkuLot1());
		logSoPick.setPickRealQty(pickByPcStockDto.getOutStockQty());
		logSoPick.setWspId(stock.getWspId());
		logSoPick.setWspName(stock.getWspName());
		logSoPick.setSkuLevel(stock.getSkuLevel());
		logSoPick.setWsuCode(stock.getWsuCode());
		logSoPick.setConvertQty(1);
		logSoPick.setSkuLevel(stock.getSkuLevel());
		logSoPick.setLpnCode(stock.getLpnCode());
		logSoPick.setBoxCode(stock.getBoxCode());
		logSoPick.setSoBillId(soHeader.getSoBillId());
		logSoPick.setSoBillNo(soHeader.getSoBillNo());
		logSoPick.setSoDetailId(soDetail.getSoDetailId());
		logSoPick.setSoLineNo(soDetail.getSoLineNo());
		logSoPick.setWhId(stock.getWhId());
		StringBuilder serailNumber = new StringBuilder();
		if (pickByPcStockDto.getSerailList().size() > 0) {
			for (String serailNum : pickByPcStockDto.getSerailList()) {
				serailNumber.append(serailNum);
				serailNumber.append(",");
			}
			serailNumber.deleteCharAt(serailNumber.length() - 1);
		}
		logSoPick.setSnCode(serailNumber.toString());
		logSoPick.setStockStatus(stock.getStockStatus());
		logSoPick.setWoId(stock.getWoId());
		SkuLotUtil.setAllSkuLot(stock, logSoPick);
		return logSoPick;
	}

	/**
	 * PC拣货根据前端传入的参数生成拣货记录
	 *
	 * @param request  前端传入参数
	 * @param soHeader 收货单头表实体
	 * @param soDetail 收货单明细实体
	 * @param stock    库存
	 * @param location 库位
	 * @return 拣货记录实体
	 */
	public LogSoPick createLogSoPick(PickByPcsRequest request, SoHeader soHeader, SoDetail soDetail, Stock stock, Location location) {
		LogSoPick logSoPick = new LogSoPick();
		logSoPick.setLocId(location.getLocId());
		logSoPick.setLocCode(location.getLocCode());
		logSoPick.setSkuId(stock.getSkuId());
		logSoPick.setSkuCode(stock.getSkuCode());
		logSoPick.setSkuName(stock.getSkuName());
		logSoPick.setLotNumber(stock.getSkuLot1());
		logSoPick.setPickRealQty(request.getQty());
		logSoPick.setWspId(stock.getWspId());
		logSoPick.setWspName(stock.getWspName());
		logSoPick.setSkuLevel(stock.getSkuLevel());
		logSoPick.setWsuCode(stock.getWsuCode());
		logSoPick.setConvertQty(1);
		logSoPick.setSkuLevel(stock.getSkuLevel());
		logSoPick.setLpnCode(stock.getLpnCode());
		logSoPick.setBoxCode(stock.getBoxCode());
		logSoPick.setSoBillId(soHeader.getSoBillId());
		logSoPick.setSoBillNo(soHeader.getSoBillNo());
		logSoPick.setSoDetailId(soDetail.getSoDetailId());
		logSoPick.setSoLineNo(soDetail.getSoLineNo());
		logSoPick.setWhId(stock.getWhId());
		StringBuilder serailNumber = new StringBuilder();
		if (request.getSerailList().size() > 0) {
			for (String serailNum : request.getSerailList()) {
				serailNumber.append(serailNum);
				serailNumber.append(",");
			}
			serailNumber.deleteCharAt(serailNumber.length() - 1);
		}
		logSoPick.setSnCode(serailNumber.toString());
		logSoPick.setStockStatus(stock.getStockStatus());
		logSoPick.setWoId(stock.getWoId());
		SkuLotUtil.setAllSkuLot(stock, logSoPick);
		return logSoPick;
	}

	public LogSoPick create(SoDetail soDetail, SoPickPlan pickPlan, BigDecimal pickQty, List<String> serialNoList, Stock stock) {
		LogSoPick logSoPick = new LogSoPick();
		logSoPick.setLocId(stock.getLocId());
		logSoPick.setLocCode(stock.getLocCode());
		logSoPick.setSkuId(stock.getSkuId());
		logSoPick.setSkuCode(stock.getSkuCode());
		logSoPick.setSkuName(stock.getSkuName());
		logSoPick.setLotNumber(stock.getSkuLot1());
		logSoPick.setPickRealQty(pickQty);
		logSoPick.setWspId(stock.getWspId());
		logSoPick.setWspName(stock.getWspName());
		logSoPick.setSkuLevel(stock.getSkuLevel());
		logSoPick.setWsuCode(stock.getWsuCode());
		logSoPick.setConvertQty(1);
		logSoPick.setSkuLevel(stock.getSkuLevel());
		logSoPick.setLpnCode(stock.getLpnCode());
		logSoPick.setBoxCode(stock.getBoxCode());
		logSoPick.setSoBillId(pickPlan.getSoBillId());
		logSoPick.setSoBillNo(pickPlan.getSoBillNo());
		logSoPick.setSoDetailId(pickPlan.getSoDetailId());
		logSoPick.setSoLineNo(soDetail.getSoLineNo());
		logSoPick.setWhId(stock.getWhId());
		StringBuilder serialNumber = new StringBuilder();
		if (serialNoList.size() > 0) {
			for (String serailNum : serialNoList) {
				serialNumber.append(serailNum);
				serialNumber.append(",");
			}
			serialNumber.deleteCharAt(serialNumber.length() - 1);
		}
		logSoPick.setSnCode(serialNumber.toString());
		logSoPick.setStockStatus(stock.getStockStatus());
		logSoPick.setWoId(stock.getWoId());
		SkuLotUtil.setAllSkuLot(stock, logSoPick);
		return logSoPick;
	}
}
