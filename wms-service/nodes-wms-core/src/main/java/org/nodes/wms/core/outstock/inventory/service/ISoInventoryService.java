package org.nodes.wms.core.outstock.inventory.service;

import org.nodes.wms.core.outstock.inventory.entity.SoInventory;
import org.nodes.wms.core.outstock.inventory.vo.SoInventoryVO;
import org.springblade.core.mp.base.BaseService;

/**
 * 发货清单主表
 服务类
 *
 * @author NodeX
 * @since 2021-06-09
 */
public interface ISoInventoryService extends BaseService<SoInventory> {

	SoInventoryVO getDetail(Long id);
}
