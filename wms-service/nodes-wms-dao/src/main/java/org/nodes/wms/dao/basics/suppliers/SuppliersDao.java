package org.nodes.wms.dao.basics.suppliers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.suppliers.dto.input.SupplierPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierPageResponse;
import org.nodes.wms.dao.basics.suppliers.entities.Suppliers;

import java.util.List;

/**
 * 供应商 Dao 接口
 */

public interface SuppliersDao {
	/**
	 * 分页查询供应商信息
	 *
	 * @param page
	 * @param supplierPageQuery : 分页请求参数
	 * @return Page
	 */
	Page<SupplierPageResponse> selectPage(IPage<?> page, SupplierPageQuery supplierPageQuery);

	/**
	 * 新增一条供应商信息
	 *
	 * @param suppliers:
	 * @return java.lang.Integer
	 */
	Boolean insert(Suppliers suppliers);

	/**
	 * 根据Id逻辑删除供应商信息
	 *
	 * @param ids
	 * @return java.lang.Integer
	 */
	Boolean delete(List<Long> ids);

	/**
	 * 查询供应商code是否存在
	 *
	 ** @param code:
	 ** @return java.util.List<org.nodes.wms.dao.basics.suppliers.entities.Suppliers>
	 */
	boolean isExistSupplierCode(String code);
}
