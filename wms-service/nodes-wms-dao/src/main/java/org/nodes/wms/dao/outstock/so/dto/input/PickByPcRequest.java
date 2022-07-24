package org.nodes.wms.dao.outstock.so.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.stock.dto.output.PickByPcStockDto;

import java.util.List;

/**
 * pc拣货接收前端参数Request
 */
@Data
public class PickByPcRequest {
	/**
	 * 发货单ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;
	/**
	 * 收货列表集合
	 */
	private List<PickByPcStockDto> pickByPcStockDtoList;
}
