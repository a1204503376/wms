package org.nodes.wms.biz.basics.suppliers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.suppliers.dto.input.DeleteSuppliersRequest;
import org.nodes.wms.dao.basics.suppliers.dto.input.SuppliersPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.input.SuppliersRequest;
import org.nodes.wms.dao.basics.suppliers.dto.output.SuppliersPageResponse;

/**
 * 供应商 业务类
 */
public interface SuppliersBiz {
	/**
	 * 获取供应商集合
	 *
	 * @param page
	 * @param suppliersPageQuery : 分页请求参数
	 * @return Page<SuppliersPageResponse>
	 */
	Page<SuppliersPageResponse> getPageSuppliers(IPage<?> page, SuppliersPageQuery suppliersPageQuery);

	/**
	 *  新增供应商信息
	 *
	 ** @param suppliersRequest:
	 ** @return java.lang.Integer
	 */
	Boolean saveSuppliers(SuppliersRequest suppliersRequest);

	/**
	 * 修改供应商信息
	 *
	 ** @param suppliersRequest:
	 ** @return java.lang.Boolean
	 */

	Boolean updateSuppliersById(SuppliersRequest suppliersRequest);

	/**
	 * 删除供应商信息
	 * <p>
	 * * @param deleteSuppliersRequest :
	 * * @return java.lang.Integer
	 */
	Boolean removeSuppliersByIds(DeleteSuppliersRequest deleteSuppliersRequest);
}
