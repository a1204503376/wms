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
import org.springblade.core.tool.utils.Func;
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
	public Page<SuppliersPageResponse> getPageSuppliers(IPage<?> page, SuppliersPageQuery suppliersPageQuery) {
		return suppliersDao.queryPageSuppliers(page, suppliersPageQuery);
	}

	/**
	 * 新增供应商信息
	 * <p>
	 * * @param suppliersRequest:
	 * * @return java.lang.Boolean
	 */

	@Override
	public Boolean saveSuppliers(SuppliersRequest suppliersRequest) {
		if (Func.isEmpty(suppliersRequest.getCode())) {
			throw new SerialException("供应商编码为空");
		}
		Integer codeExistCount = suppliersDao.findSupplierCodeExist(suppliersRequest.getCode());
		if (codeExistCount > 0) {
			throw new SerialException("供应商编码["+suppliersRequest.getCode()+"]已存在");
		}
		if (Func.isEmpty(suppliersRequest.getName())){
			throw new SerialException("供应商名称为空");
		}
		Suppliers suppliers = suppliersFactory.createSuppliers(suppliersRequest);
		Integer count = suppliersDao.addSuppliers(suppliers);
		return count > 0;
	}

	@Override
	public Boolean updateSuppliersById(SuppliersRequest suppliersRequest) {
		Suppliers suppliers = suppliersFactory.createSuppliers(suppliersRequest);
		Integer count = suppliersDao.updateSuppliers(suppliers);
		return count > 0;
	}

	@Override
	public Boolean removeSuppliersByIds(DeleteSuppliersRequest deleteSuppliersRequest) {
		Integer count = suppliersDao.deleteSuppliersById(deleteSuppliersRequest.getIds());
		return count > 0;
	}
}
