package org.nodes.wms.core.stock.core.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.stock.core.entity.StockPack;

/**
 * 尾箱打包数据传输对象实体类
 *
 * @author pengwei
 * @since 2020-07-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StockPackDTO extends StockPack {
	/**
	 * 系统日志ID
	 */
	private Long systemProcId;

	/**
	 * 操作类型
	 */
	private Integer procType;

}
