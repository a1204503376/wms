package org.nodes.wms.biz.basics.suppliers.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.core.enums.StatusEnum;
import org.nodes.wms.biz.basics.owner.OwnerBiz;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.suppliers.SupplierDao;
import org.nodes.wms.dao.basics.suppliers.dto.input.AddOrEditSupplierRequest;
import org.nodes.wms.dao.basics.suppliers.dto.input.SupplierImportRequest;
import org.nodes.wms.dao.basics.suppliers.entities.Supplier;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierFactory {

	private final OwnerBiz ownerBiz;

	private final SupplierDao supplierDao;

	public Supplier createSupplier(AddOrEditSupplierRequest addOrEditSupplierRequest) {
		Supplier supplier = new Supplier();
		Func.copy(addOrEditSupplierRequest, supplier);
		return supplier;
	}

	public List<Supplier> createSupplierListForImport(List<SupplierImportRequest> importDataList) {
		List<Supplier> supplierList = new ArrayList<>();
		for (SupplierImportRequest data : importDataList) {
			Supplier supplier = new Supplier();
			if (Func.isEmpty(data.getCode())) {
				throw new ServiceException("导入失败，供应商编码不能为空");
			}
			if (Func.isEmpty(data.getName())) {
				throw new ServiceException("导入失败，供应商名称不能为空");
			}
			// 根据供应商编码查询供应商信息
			if (Func.isNotEmpty(data.getOwnerCode())) {
				Owner owner = ownerBiz.findByCode(data.getOwnerCode());
				if (Func.isEmpty(owner)) {
					throw new ServiceException("导入失败，不存在货主编码：" + data.getOwnerCode());
				} else {
					supplier.setWoId(owner.getWoId());
				}
			}
			boolean isExist = supplierDao.isExistSupplierCode(data.getCode());
			if (isExist) {
				throw new ServiceException("导入失败，供应商编码[" + data.getCode() + "]已存在");
			}
			supplier.setCode(data.getCode());
			supplier.setName(data.getName());
			supplier.setSimpleName(data.getSimpleName());
			supplier.setRemark(data.getRemark());

			if (Func.isEmpty(data.getStatus())) {
				throw new ServiceException("导入失败，启用状态不能为空");
			}
			if (!data.getStatus().equals(StatusEnum.ON.getIndex())
				&& !data.getStatus().equals(StatusEnum.OFF.getIndex())) {
				throw new ServiceException("导入失败，启用状态只能为1(启用)或者-1(禁用)");
			}
			supplier.setStatus(data.getStatus());
			supplierList.add(supplier);
		}
		return supplierList;
	}
}
