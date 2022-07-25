package org.nodes.wms.dao.outstock.so.dto.output;

import lombok.Data;
import org.nodes.wms.dao.stock.dto.output.PickByPcStockDto;

import java.util.List;

/**
 * pc拣货返回前端dto
 */
@Data
public class SoDetailAndStockResponse {
	/**
	 * 出库明细
	 */
	private PickByPcSoDetailResponse pickByPcSoDetailResponse;
	/**
	 * 可出库库存集合
	 */
	List<PickByPcStockDto> stockResponseList;

}
