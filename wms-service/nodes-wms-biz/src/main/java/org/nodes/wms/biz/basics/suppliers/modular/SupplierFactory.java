package org.nodes.wms.biz.basics.suppliers.modular;

import org.nodes.wms.dao.basics.suppliers.dto.input.AddSupplierRequest;
import org.nodes.wms.dao.basics.suppliers.entities.Supplier;
import org.springframework.stereotype.Service;

/**
 * 供应商 工厂
 * 用于创建供应商实体
 * @author 彭永程
 * @date 2022-04-20 13:47
 **/
@Service
public class SupplierFactory {

	public Supplier createSupplier(AddSupplierRequest addSupplierRequest){
		Supplier supplier = new Supplier();
		supplier.setCode(addSupplierRequest.getCode());
		supplier.setName(addSupplierRequest.getName());
		supplier.setSimpleName(addSupplierRequest.getSimpleName());
		supplier.setWoId(addSupplierRequest.getWoId());
		supplier.setRemark(addSupplierRequest.getRemark());
		supplier.setStatus(addSupplierRequest.getStatus());
		return supplier;
	}
}
