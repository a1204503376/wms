package org.nodes.wms.biz.instock;

import org.nodes.wms.dao.instock.receive.dto.input.PdaByPieceReceiveRequest;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailLpnPdaMultiRequest;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailLpnPdaRequest;
import org.nodes.wms.dao.instock.receive.dto.output.PdaByPcsReceiveResponse;

import java.util.List;

/**
 * 收货相关业务
 */
public interface InStockBiz {

	/**
	 * 按箱收货,支持无单收货
	 *
	 * @param request
	 */
	void receiveByBoxCode(ReceiveDetailLpnPdaRequest request);

	/**
	 * 按件收货
	 *
	 * @param request
	 */
	PdaByPcsReceiveResponse receiptByPcs(PdaByPieceReceiveRequest request);

	/**
	 * 多箱收货
	 *
	 * @param receiveDetailLpnPdaMultiRequest 前端传入参数
	 */
	void receiveByMultiBoxCode(ReceiveDetailLpnPdaMultiRequest receiveDetailLpnPdaMultiRequest);

	/**
	 * 撤销收货
	 *
	 * @param receiveIdList 清点记录id
	 */
	void cancelReceive(List<Long> receiveIdList);
}
