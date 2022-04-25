package org.nodes.wms.biz.basics.suppliers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.suppliers.dto.input.AddSupplierRequest;
import org.nodes.wms.dao.basics.suppliers.dto.input.RemoveRequest;
import org.nodes.wms.dao.basics.suppliers.dto.input.SupplierPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierPageResponse;

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
	 * @return java.lang.Integer
	 */
	Boolean newSupplier(AddSupplierRequest addSupplierRequest);

	/**
	 * 根据id批量删除供应商信息
	 *
	 * @param removeRequest : 供应商id集合对象
	 * @return java.lang.Integer
	 */
	Boolean removeByIds(RemoveRequest removeRequest);
}
