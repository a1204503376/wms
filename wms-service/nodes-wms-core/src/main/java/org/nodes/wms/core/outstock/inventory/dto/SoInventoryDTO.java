package org.nodes.wms.core.outstock.inventory.dto;

import org.nodes.wms.core.outstock.inventory.entity.SoInventory;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 发货清单主表
数据传输对象实体类
 *
 * @author NodeX
 * @since 2021-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SoInventoryDTO extends SoInventory {
	private static final long serialVersionUID = 1L;

}
