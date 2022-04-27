package org.nodes.wms.biz.basics.crudcolumn;

import org.nodes.wms.dao.basics.crudcolumn.dto.CrudColumnRequest;
import org.nodes.wms.dao.basics.crudcolumn.dto.CrudColumnResponse;

import java.util.Collection;
import java.util.List;

/**
 * 列显隐 业务接口
 */
public interface CrudColumnBiz {

	/**
	 * 列显隐保存
	 * 保存前先删除旧的配置数据
	 *
	 * @param crudColumnRequestCollection 列显隐请求对象集合
	 * @return true:成功，false：失败
	 */
	boolean deleteBeforeSaving(Collection<CrudColumnRequest> crudColumnRequestCollection);

	/**
	 * 获取当前登录用户的列显隐配置数据
	 *
	 * @param menuId 菜单ID
	 * @return List<CrudColumnResponse>
	 */
	List<CrudColumnResponse> getCrudColumnResponseList(Long menuId);
}
