package org.nodes.wms.dao.basics.suppliers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.suppliers.dto.input.SuppliersPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.output.SuppliersPageResponse;
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
	 * @param suppliersPageQuery : 分页请求参数
	 * @return Page
	 */
	Page<SuppliersPageResponse> selectPage(IPage<?> page, SuppliersPageQuery suppliersPageQuery);

	/**
	 * 新增一条供应商信息
	 *
	 * @param suppliers:
	 * @return java.lang.Integer
	 */
	Boolean addSuppliers(Suppliers suppliers);

	/**
	 * 根据Id逻辑删除供应商信息
	 *
	 * @param ids
	 * @return java.lang.Integer
	 */
	Boolean deleteSuppliersByIds(List<Long> ids);

	/**
	 * 查询供应商code是否存在
	 *
	 ** @param code:
	 ** @return java.util.List<org.nodes.wms.dao.basics.suppliers.entities.Suppliers>
	 */

	Integer selectCountSupplierCode(String code);
}
