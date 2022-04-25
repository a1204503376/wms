package org.nodes.wms.biz.basics.suppliers.modular;

import org.nodes.wms.dao.basics.suppliers.dto.input.SuppliersRequest;
import org.nodes.wms.dao.basics.suppliers.entities.Suppliers;
import org.springframework.stereotype.Service;

/**
 * 供应商 工厂
 * 用于创建供应商实体
 * @author 彭永程
 * @date 2022-04-20 13:47
 **/
@Service
public class SuppliersFactory {

	public Suppliers createSuppliers(SuppliersRequest suppliersRequest){
		Suppliers suppliers = new Suppliers();
		suppliers.setCode(suppliersRequest.getCode());
		suppliers.setName(suppliersRequest.getName());
		suppliers.setSimpleName(suppliersRequest.getSimpleName());
		suppliers.setWoId(suppliersRequest.getWoId());
		suppliers.setRemark(suppliersRequest.getRemark());
		suppliers.setTenantId(suppliers.getTenantId());
		suppliers.setStatus(suppliersRequest.getStatus());
		return suppliers;
	}
}
