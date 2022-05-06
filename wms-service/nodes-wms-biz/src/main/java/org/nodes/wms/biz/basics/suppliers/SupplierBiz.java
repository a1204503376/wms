package org.nodes.wms.biz.basics.suppliers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.suppliers.dto.input.AddSupplierRequest;
import org.nodes.wms.dao.basics.suppliers.dto.input.RemoveRequest;
import org.nodes.wms.dao.basics.suppliers.dto.input.SupplierPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierPageResponse;

import javax.servlet.http.HttpServletResponse;

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
	 * @param addSupplierRequest:
	 * @return  true:新增成功 ,false:新增失败
	 */
	boolean newSupplier(AddSupplierRequest addSupplierRequest);

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
}
