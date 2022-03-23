package org.nodes.wms.core.instock.inventory.dto;

import org.nodes.wms.core.instock.inventory.entity.AsnInventory;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 收货清单头表数据传输对象实体类
 *
 * @author NodeX
 * @since 2021-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AsnInventoryDTO extends AsnInventory {
	private static final long serialVersionUID = 1L;

}
