package org.nodes.wms.biz.instock;

import org.nodes.wms.dao.instock.receive.dto.input.PdaByPieceReceiveRequest;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveByPcRequest;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailLpnPdaMultiRequest;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailLpnPdaRequest;
import org.nodes.wms.dao.instock.receive.dto.output.PdaByPcsReceiveResponse;
import org.nodes.wms.dao.stock.entities.Stock;

import java.util.List;

/**
 * 收货相关业务
 *
 * @author nodesc
 */
public interface InStockBiz {

	/**
	 * 按箱收货,支持无单收货
	 *
	 * @param request 请求参数
	 * @param logType StockLogTypeEnum的描述
	 */
	void receiveByBoxCode(ReceiveDetailLpnPdaRequest request, String logType);

	/**
	 * 按件收货
	 *
	 * @param request 请求参数
	 */
	PdaByPcsReceiveResponse receiptByPcs(PdaByPieceReceiveRequest request);

	/**
	 * 多箱收货
	 *
	 * @param receiveDetailLpnPdaMultiRequest 前端传入参数
	 */
	void receiveByMultiBoxCode(ReceiveDetailLpnPdaMultiRequest receiveDetailLpnPdaMultiRequest);

	/**
	 * 按箱收货,支持无单收货(多箱收货调用)
	 *
	 * @param request 请求参数
	 * @param logType StockLogTypeEnum的描述
	 */
	List<Stock> receiveByDuoBoxCode(ReceiveDetailLpnPdaRequest request, String logType);

	/**
	 * 撤销收货
	 *
	 * @param receiveIdList 清点记录id
	 */
	void cancelReceive(List<Long> receiveIdList);

	/**
	 * PC收货
	 *
	 * @param receiveByPcRequest 前端传入参数
	 * @return 单号
	 */
	String receiveByPc(ReceiveByPcRequest receiveByPcRequest);
}
