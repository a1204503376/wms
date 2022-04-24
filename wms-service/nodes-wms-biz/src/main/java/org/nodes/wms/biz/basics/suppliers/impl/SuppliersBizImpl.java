package org.nodes.wms.biz.basics.suppliers.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.jsonwebtoken.io.SerialException;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.suppliers.SuppliersBiz;
import org.nodes.wms.biz.basics.suppliers.modular.SuppliersFactory;
import org.nodes.wms.dao.basics.suppliers.SuppliersDao;
import org.nodes.wms.dao.basics.suppliers.dto.input.DeleteSuppliersRequest;
import org.nodes.wms.dao.basics.suppliers.dto.input.SuppliersPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.input.SuppliersRequest;
import org.nodes.wms.dao.basics.suppliers.dto.output.SuppliersPageResponse;
import org.nodes.wms.dao.basics.suppliers.entities.Suppliers;
import org.springframework.context.annotation.Primary;
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
	public Page<SuppliersPageResponse> getPage(IPage<?> page, SuppliersPageQuery suppliersPageQuery) {
		return suppliersDao.selectPage(page, suppliersPageQuery);
	}

	/**
	 * 新增供应商信息
	 * <p>
	 * * @param suppliersRequest:
	 * * @return java.lang.Boolean
	 */

	@Override
	public Boolean newSuppliers(SuppliersRequest suppliersRequest) {
		Integer codeCount = suppliersDao.selectCountSupplierCode(suppliersRequest.getCode());
		if (codeCount > 0) {
			throw new SerialException("供应商编码["+suppliersRequest.getCode()+"]已存在");
		}
		Suppliers suppliers = suppliersFactory.createSuppliers(suppliersRequest);
		return suppliersDao.addSuppliers(suppliers);
	}

	@Override
	public Boolean removeByIds(DeleteSuppliersRequest deleteSuppliersRequest) {
		return suppliersDao.deleteSuppliersByIds(deleteSuppliersRequest.getIds());
	}
}
