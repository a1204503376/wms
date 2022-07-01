package org.nodes.wms.dao.instock.receive.dto.input;

import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;

import java.util.List;

/**
 * 多箱收货接收前端参数request
 */
@Data
public class ReceiveDetailLpnPdaMultiRequest extends BaseSkuLot {
	/**
	 *  lpn集合
	 */
	private  List<ReceiveDetailLpnPdaRequest> receiveDetailLpnPdaRequestList;
	/**
	 *  lpn
	 */
	private String lpnCode;
	/**
	 * 库位编码
	 */
	private String locCode;
	/**
	 * 库房id
	 */
	private Long whId;
}
