package org.nodes.wms.biz.basics.suppliers.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.core.enums.StatusEnum;
import org.nodes.wms.biz.basics.owner.OwnerBiz;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.suppliers.SupplierDao;
import org.nodes.wms.dao.basics.suppliers.dto.input.AddSupplierRequest;
import org.nodes.wms.dao.basics.suppliers.entities.Supplier;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 供应商 工厂
 * 用于创建供应商实体
 * @author 彭永程
 * @date 2022-04-20 13:47
 **/
@Service
@RequiredArgsConstructor
public class SupplierFactory {

	private final OwnerBiz ownerBiz;

	private final SupplierDao supplierDao;

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

    public List<Supplier> createSupplierListForUpload(List<AddSupplierRequest> addSupplierList) {
		List<Supplier> supplierList = new ArrayList<>();
		for (AddSupplierRequest addSupplier: addSupplierList) {
			Supplier supplier = new Supplier();
			// 根据供应商编码查询供应商信息
			if (Func.isNotEmpty(addSupplier.getOwnerCode())){
				Owner owner = ownerBiz.findByCode(addSupplier.getOwnerCode());
				if(Func.isEmpty(owner)){
					throw new ServiceException("导入失败，不存在货主编码："+addSupplier.getOwnerCode());
				} else {
					supplier.setWoId(owner.getWoId());
				}
			}
			boolean isExist = supplierDao.isExistSupplierCode(addSupplier.getCode());
			if (isExist) {
				throw new ServiceException("导入失败，供应商编码["+ addSupplier.getCode()+"]已存在");
			}
			supplier.setCode(addSupplier.getCode());
			supplier.setName(addSupplier.getName());
			supplier.setSimpleName(addSupplier.getSimpleName());
			supplier.setRemark(addSupplier.getRemark());

			if (!addSupplier.getStatus().equals(StatusEnum.ON.getIndex())
				&& !addSupplier.getStatus().equals(StatusEnum.OFF.getIndex())){
				throw new ServiceException("导入失败，启用状态只能为1(启用)或者0(禁用)");
			}
			supplier.setStatus(addSupplier.getStatus());
			supplierList.add(supplier);
		}
		return supplierList;
    }
}
