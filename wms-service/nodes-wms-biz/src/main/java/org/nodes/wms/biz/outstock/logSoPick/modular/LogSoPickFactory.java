package org.nodes.wms.biz.outstock.logSoPick.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.stock.dto.output.PickByPcStockDto;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springframework.stereotype.Component;

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
	 * @param stock
	 * @return 拣货记录实体
	 */
	public LogSoPick createLogSoPick(PickByPcStockDto pickByPcStockDto, SoHeader soHeader, SoDetail soDetail, Stock stock) {
		LogSoPick logSoPick = new LogSoPick();
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
		logSoPick.setStockStatus(stock.getStockStatus());
		SkuLotUtil.setAllSkuLot(stock, logSoPick);
		return logSoPick;
	}
}
