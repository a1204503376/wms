package org.nodes.wms.biz.basics.suppliers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.suppliers.dto.input.*;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierPageResponse;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierSelectResponse;
import org.nodes.wms.dao.basics.suppliers.entities.Supplier;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 供应商 业务类
 */
public interface SupplierBiz {
	/**
	 * 获取供应商集合
	 *
	 * @param page : 分页对象
	 * @param supplierPageQuery : 分页请求参数
	 * @return Page<SupplierPageResponse>
	 */
	Page<SupplierPageResponse> getPage(IPage<?> page, SupplierPageQuery supplierPageQuery);

	/**
	 *  新增供应商信息
	 *
	 * @param addSupplierRequest: 新增供应商dto对象
	 * @return Supplier: 供应商实体
	 */
	Supplier newSupplier(AddSupplierRequest addSupplierRequest);

	/**
	 * 根据id批量删除供应商信息
	 *
	 * @param removeRequest : 供应商id集合对象
	 * @return true:删除成功 ,false:删除失败
	 */
	boolean removeByIds(RemoveRequest removeRequest);

	/**
	 * Excel 导出(导出当前查询条件)
	 *
	 * @param supplierPageQuery : 条件参数
	 * @param response:
	 */
	void exportSupplier(SupplierPageQuery supplierPageQuery, HttpServletResponse response);

	/**
	 * 供应商选择下拉框
	 * 展示最近10个供应商
	 *
	 * @param supplierSelectQuery: 下拉组件搜索条件
	 * @return List<SupplierSelectResponse>
	 */
    List<SupplierSelectResponse> getSupplierSelectResponseTop10List(SupplierSelectQuery supplierSelectQuery);

	/**
	 * 根据id查找供应商信息
	 *
	 * @param id: 供应商id
	 * @return Supplier
	 */
	public Supplier findById(Long id);

	/**
	 * Excel 导入
	 *
	 * @param importDataList: 导入excel数据集合
	 * @return true: 导入成功， false: 导入失败
	 */
    boolean importExcel(List<SupplierImportRequest> importDataList);
}
