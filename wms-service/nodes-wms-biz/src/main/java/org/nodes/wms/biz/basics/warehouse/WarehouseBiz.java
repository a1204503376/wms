package org.nodes.wms.biz.basics.warehouse;

import org.nodes.wms.dao.basics.warehouse.dto.output.WarehousePdaResponse;
import org.nodes.wms.dao.basics.warehouse.dto.output.WarehouseResponse;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.secure.BladeUser;

import java.util.List;

/**
 * 库房管理业务层接口
 */
public interface WarehouseBiz {
	/**
	 * 获取仓库下拉组件列表
	 *
	 * @return 仓库下拉列表集合
	 */
	List<WarehouseResponse> getWarehouseSelectResponseList();

	/**
	 * 根据仓库Id获取仓库实体
	 *
	 * @param warehouseId 仓库Id
	 * @return Warehouse 仓库实体
	 */
	Warehouse findById(Long warehouseId);

	/**
	 * 获取所有的库房信息
	 *
	 * @return
	 */
	List<Warehouse> findAll();

	/**
	 * 根据仓库编码获取仓库实体
	 *
	 * @param whCode 仓库实体
	 * @return Warehouse 仓库实体
	 */
	Warehouse findByCode(String whCode);

	/**
	 * 根据用户获取有权限查看的库房
	 *
	 * @param user
	 * @return
	 */
	List<Warehouse> getWarehouseByUser(BladeUser user);

	List<WarehousePdaResponse> getWarehouseResponseByUser(BladeUser user);

	/**
	 * 新增库房之后初始化库区、库位,编码格式已库房编码-开头
	 *
	 * @param warehouse: 库房对象
	 */
	void initZoneAndLocAfterNewWarehouse(Warehouse warehouse);

	/**
	 * 授权
	 *
	 * @param authorizeString: 原始json字符
	 * @return String 加密之后的字符
	 */
	String authorized(String authorizeString);

	/**
	 * 验证仓库的授权，如果验证失败，则抛出异常
	 */
	void valiAuthorization();
}
