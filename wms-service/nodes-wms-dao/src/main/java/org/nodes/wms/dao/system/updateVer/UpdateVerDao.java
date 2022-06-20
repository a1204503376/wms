package org.nodes.wms.dao.system.updateVer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.system.updateVer.dto.input.UpdateVerPageQuery;
import org.nodes.wms.dao.system.updateVer.dto.output.UpdateVerExportResponse;
import org.nodes.wms.dao.system.updateVer.dto.output.UpdateVerPageResponse;
import org.nodes.wms.dao.system.updateVer.entities.UpdateVer;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统版本DAO接口
 **/
@Repository
public interface UpdateVerDao {

	/**
	 * 分页查询
	 *
	 * @param page: 分页对象
	 * @param query: 查询条件dto对象
	 * @return Page
	 */
	Page<UpdateVerPageResponse> page(IPage<?> page, UpdateVerPageQuery query);

	/**
	 *  根据id获取系统版本对象
	 *
	 * @param suvId: 系统版本id
	 * @return UpdateVer
	 */
	UpdateVer getById(Long suvId);

	/**
	 * 根据id修改系统版本
	 *
	 * @param updateVer: 系统版本对象
	 */
	void updateUpdateVerById(UpdateVer updateVer);

	/**
	 * 导出
	 *
	 * @param query: 导出时条件
	 * @return List<UpdateVerExportResponse>
	 */
	List<UpdateVerExportResponse> listByQuery(UpdateVerPageQuery query);
}
