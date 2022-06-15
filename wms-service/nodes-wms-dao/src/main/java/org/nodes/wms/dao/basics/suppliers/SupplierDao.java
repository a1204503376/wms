package org.nodes.wms.dao.basics.suppliers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.suppliers.dto.input.SupplierPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierExportResponse;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierPageResponse;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierSelectResponse;
import org.nodes.wms.dao.basics.suppliers.entities.Supplier;

import java.util.List;

/**
 * 供应商 Dao 接口
 */

public interface SupplierDao {
	/**
	 * 分页查询供应商信息
	 *
	 * @param page              : 分页对象
	 * @param supplierPageQuery : 分页请求参数
	 * @return Page<SupplierPageResponse>
	 */
	Page<SupplierPageResponse> selectPage(IPage<?> page, SupplierPageQuery supplierPageQuery);

	/**
	 * 新增供应商信息
	 *
	 * @param supplier: 供应商对象
	 * @return true: 新增成功，false: 新增失败
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
	 * <p>
	 * * @param code:供应商编码
	 * * @return boolean
	 */
	boolean isExistSupplierCode(String code);

	/**
	 * 根据参数条件查询供应商信息
	 *
	 * @param supplierPageQuery: 条件参数
	 * @return List<SupplierExportResponse>
	 */
	List<SupplierExportResponse> listBySupplierPageQuery(SupplierPageQuery supplierPageQuery);

	/**
	 * 根据供应商编码或者供应商名称查询前10个供应商信息
	 *
	 * @param code: 供应商编码
	 * @param name: 供应商名称
	 * @return List<SupplierSelectResponse>
	 */
	List<SupplierSelectResponse> listTop10ByCodeName(String code, String name);

	/**
	 * 根据id查找供应商信息
	 *
	 * @param id: 供应商id
	 * @return Supplier
	 */
	Supplier getById(Long id);

	/**
	 * Excel 导入
	 *
	 * @param supplierList : 新增供应商对象集合
	 * @return true: 导入成功， false: 导入失败
	 */
	boolean importExcel(List<Supplier> supplierList);

	/**
	 * 根据供应商编码获取供应商实体
	 *
	 * @param code 供应商编码
	 * @return Supplier
	 */
	Supplier getByCode(String code);

	/**
	 * 新增或修改供应商
	 *
	 * @param supplier: 供应商对象
	 */
	void saveSupplier(Supplier supplier);
}
