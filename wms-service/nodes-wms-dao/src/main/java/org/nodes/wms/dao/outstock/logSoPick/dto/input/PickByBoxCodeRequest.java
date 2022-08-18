package org.nodes.wms.dao.outstock.logSoPick.dto.input;

import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;

import java.io.Serializable;

/**
 * PDA按箱拣货请求参数
 *
 * @author nodesc
 **/
@Data
public class PickByBoxCodeRequest extends BaseSkuLot implements Serializable {
	private static final long serialVersionUID = -6976318740136962398L;

	/**
	 * 箱码
	 */
	private String boxCode;
}
