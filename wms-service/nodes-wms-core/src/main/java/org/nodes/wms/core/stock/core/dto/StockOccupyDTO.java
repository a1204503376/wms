
package org.nodes.wms.core.stock.core.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.stock.core.entity.StockOccupy;

/**
 * 库存占用表数据传输对象实体类
 *
 * @author pengwei
 * @since 2020-02-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StockOccupyDTO extends StockOccupy {
	private static final long serialVersionUID = 1L;

}
