package org.nodes.wms.core.instock.inventory.service;

import org.nodes.wms.core.instock.inventory.entity.AsnInventory;
import org.nodes.wms.core.instock.inventory.vo.AsnInventoryVO;
import org.springblade.core.mp.base.BaseService;

/**
 * 收货清单头表 服务类
 *
 * @author NodeX
 * @since 2021-06-09
 */
public interface IAsnInventoryService extends BaseService<AsnInventory> {

	AsnInventoryVO getDetail(Long id);
}
