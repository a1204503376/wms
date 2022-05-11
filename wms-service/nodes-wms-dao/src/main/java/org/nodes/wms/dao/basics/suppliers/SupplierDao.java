package org.nodes.wms.dao.basics.suppliers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.suppliers.dto.input.SupplierPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierExportResponse;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierPageResponse;
import org.nodes.wms.dao.basics.suppliers.entities.Supplier;

import java.util.List;

/**
 * 供应商 Dao 接口
 */

public interface SupplierDao {
	/**
	 * 分页查询供应商信息
	 *
	 * @param page : 分页对象
	 * @param supplierPageQuery : 分页请求参数
	 * @return Page<SupplierPageResponse>
	 */
	Page<SupplierPageResponse> selectPage(IPage<?> page, SupplierPageQuery supplierPageQuery);

	/**
	 * 新增一条供应商信息
	 *
	 * @param supplier:供应商对象
	 * @return java.lang.Integer
	 */
	boolean insert(Supplier supplier);

	/**
	 * 根据id逻辑删除供应商信息
	 *
	 * @param ids:供应商id集合
	 * @return true:删除成功，false：删除失败
	 */
	boolean delete(List<Long> ids);

	/**
	 * 查询供应商code是否存在
	 *
	 ** @param code:供应商编码
	 ** @return boolean
	 */
	boolean isExistSupplierCode(String code);

	/**
	 * 根据参数条件查询供应商信息
	 *
	 * @param supplierPageQuery: 条件参数
	 * @return List<SupplierExportResponse>
	 */
	List<SupplierExportResponse> listBySupplierPageQuery(SupplierPageQuery supplierPageQuery);
}
