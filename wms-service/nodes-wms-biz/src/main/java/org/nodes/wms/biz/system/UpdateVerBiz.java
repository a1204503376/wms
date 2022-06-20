package org.nodes.wms.biz.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.system.updateVer.dto.input.UpdateVerEditRequest;
import org.nodes.wms.dao.system.updateVer.dto.input.UpdateVerPageQuery;
import org.nodes.wms.dao.system.updateVer.dto.output.UpdateVerEditResponse;
import org.nodes.wms.dao.system.updateVer.dto.output.UpdateVerPageResponse;
import org.nodes.wms.dao.system.updateVer.entities.UpdateVer;

import javax.servlet.http.HttpServletResponse;

/**
 * 系统版本业务接口
 **/
public interface UpdateVerBiz {

	/**
	 * 分页查询
	 *
	 * @param page: 分页对象
	 * @param query: 查询条件dto对象
	 * @return Page
	 */
	Page<UpdateVerPageResponse> page(IPage<?> page, UpdateVerPageQuery query);

	/**
	 *  编辑查询
	 *
	 * @param suvId: 系统版本id
	 * @return UpdateVerEditResponse
	 */
    UpdateVerEditResponse detailByEdit(Long suvId);

	/**
	 * 编辑
	 *
	 * @param updateVerEditRequest: 编辑请求dto对象
	 * @return UpdateVer
	 */
	UpdateVer edit(UpdateVerEditRequest updateVerEditRequest);

	/**
	 * 导出
	 *
	 * @param updateVerPageQuery: 导出时条件
	 * @param response: 响应对象
	 */

	void export(UpdateVerPageQuery updateVerPageQuery, HttpServletResponse response);
}
