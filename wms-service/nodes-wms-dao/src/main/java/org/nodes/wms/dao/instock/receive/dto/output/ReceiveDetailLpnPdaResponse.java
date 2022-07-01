package org.nodes.wms.dao.instock.receive.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 按箱收货返回前端response对象
 */
@Data
public class ReceiveDetailLpnPdaResponse {
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
	 * 生产批次
	 */
	private String SkuLot1;
	/**
	 * 总数
	 */
	private BigDecimal num;
	/**
	 * 型号
	 */
	private String skuLot2;
	/**
	 * 库位编码
	 */
	private String locCode;
}
