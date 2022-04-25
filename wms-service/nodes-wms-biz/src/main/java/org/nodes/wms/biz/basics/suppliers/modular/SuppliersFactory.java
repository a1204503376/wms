package org.nodes.wms.biz.basics.suppliers.modular;

import org.nodes.wms.dao.basics.suppliers.dto.input.AddSupplierRequest;
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

	public Suppliers createSuppliers(AddSupplierRequest addSupplierRequest){
		Suppliers suppliers = new Suppliers();
		suppliers.setCode(addSupplierRequest.getCode());
		suppliers.setName(addSupplierRequest.getName());
		suppliers.setSimpleName(addSupplierRequest.getSimpleName());
		suppliers.setWoId(addSupplierRequest.getWoId());
		suppliers.setRemark(addSupplierRequest.getRemark());
		suppliers.setTenantId(suppliers.getTenantId());
		suppliers.setStatus(addSupplierRequest.getStatus());
		return suppliers;
	}
}
