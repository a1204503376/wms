package org.nodes.wms.core.strategy.dto;

import org.nodes.wms.core.stock.core.entity.StockSnapshoot;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 库存快照数据传输对象实体类
 *
 * @author NodeX
 * @since 2021-05-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StockSnapshootDTO extends StockSnapshoot {
	private static final long serialVersionUID = 1L;

}
