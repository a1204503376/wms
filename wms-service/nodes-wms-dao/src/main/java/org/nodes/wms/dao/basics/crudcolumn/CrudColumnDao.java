package org.nodes.wms.dao.basics.crudcolumn;

import org.nodes.wms.dao.basics.crudcolumn.entities.NodesCurdColumn;

import java.util.Collection;
import java.util.List;

/**
 * 列显隐 DAO接口
 */
public interface CrudColumnDao {

	/**
	 * 列显隐保存
	 *
	 * @param nodesCurdColumnCollection 列显隐实体集合
	 * @return true:入库成功，false：入库失败
	 */
	boolean insert(Collection<NodesCurdColumn> nodesCurdColumnCollection);

	/**
	 * 根据用户ID和菜单ID删除列显隐配置数据
	 * @param userId 用户ID
	 * @param menuId 菜单ID
	 * @return true:删除成功，false：删除失败
	 */
	boolean delete(Long userId, Long menuId);

	/**
	 * 根据用户ID和菜单ID获取列显隐配置数据
	 *
	 * @param userId 用户ID
	 * @param menuId 菜单ID
	 * @return List<NodesCurdColumn>
	 */
	List<NodesCurdColumn> listByUserIdMenuId(Long userId, Long menuId);
}
