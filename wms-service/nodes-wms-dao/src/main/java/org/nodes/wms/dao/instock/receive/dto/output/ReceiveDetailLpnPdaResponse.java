package org.nodes.wms.dao.instock.receive.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.basics.sku.dto.output.SkuSelectResponse;
import org.nodes.wms.dao.basics.sku.entities.Sku;

import java.math.BigDecimal;
import java.util.List;

/**
 * 按箱收货返回
 */
@Data
public class ReceiveDetailLpnPdaResponse {
	private List<ReceiveDetailLpnItemDto> receiveDetailLpnItemDtoList;
	/**
	 * 主键id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

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
}
