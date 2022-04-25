package org.nodes.wms.biz.basics.suppliers.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.suppliers.SuppliersBiz;
import org.nodes.wms.biz.basics.suppliers.modular.SuppliersFactory;
import org.nodes.wms.dao.basics.suppliers.SuppliersDao;
import org.nodes.wms.dao.basics.suppliers.dto.input.DeleteSuppliersRequest;
import org.nodes.wms.dao.basics.suppliers.dto.input.SupplierPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.input.AddSupplierRequest;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierPageResponse;
import org.nodes.wms.dao.basics.suppliers.entities.Suppliers;
import org.springblade.core.log.exception.ServiceException;
import org.springframework.stereotype.Service;

/**
 * 供应商业务
 *
 * @author 彭永程
 * @date 2022-04-20 10:57
 **/
@Service
@RequiredArgsConstructor
public class SuppliersBizImpl implements SuppliersBiz {

	private final SuppliersDao suppliersDao;

	private final SuppliersFactory suppliersFactory;

	@Override
	public Page<SupplierPageResponse> getPage(IPage<?> page, SupplierPageQuery supplierPageQuery) {
		return suppliersDao.selectPage(page, supplierPageQuery);
	}

	/**
	 * 新增供应商信息
	 * <p>
	 * * @param suppliersRequest:
	 * * @return java.lang.Boolean
	 */

	@Override
	public Boolean newSupplier(AddSupplierRequest addSupplierRequest) {
		boolean isExist = suppliersDao.isExistSupplierCode(addSupplierRequest.getCode());
		if (isExist) {
			throw new ServiceException("新增供应商失败,供应商编码["+ addSupplierRequest.getCode()+"]已存在");
		}
		Suppliers suppliers = suppliersFactory.createSuppliers(addSupplierRequest);
		return suppliersDao.insert(suppliers);
	}

	@Override
	public Boolean removeByIds(DeleteSuppliersRequest deleteSuppliersRequest) {
		return suppliersDao.delete(deleteSuppliersRequest.getIds());
	}
}
