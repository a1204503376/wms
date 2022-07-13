package org.nodes.wms.dao.stock.dto.output;

import lombok.Data;
import org.nodes.wms.dao.application.dto.output.ElementUiSelectResponse;

/**
 * 库存状态下拉组件返回前端dto
 */
@Data
public class StockStatusResponse extends ElementUiSelectResponse<Integer> {
}
