package org.nodes.wms.dao.instock.receive.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveDetailLpnItemDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 按箱收货接收前端参数Request
 */
@Data
public class ReceiveDetailLpnPdaRequest extends BaseSkuLot {
	/**
	 * 收货单头表id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long receiveHeaderId;

	private List<ReceiveDetailLpnItemDto> receiveDetailLpnItemDtoList;

	/**
	 * 箱码
	 */
	private String  boxCode;
	/**
	 *  lpn
	 */
	private String lpnCode;
	/**
	 * 总数
	 */
	private BigDecimal num;
	/**
	 * 库位编码
	 */
	private String locCode;
	/**
	 * 库房id
	 */
	private Long whId;
}
