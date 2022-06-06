package org.nodes.wms.biz.basics.customers.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.core.enums.StatusEnum;
import org.nodes.wms.biz.basics.owner.OwnerBiz;
import org.nodes.wms.dao.basics.customer.CustomerDao;
import org.nodes.wms.dao.basics.customer.dto.input.CustomerImportRequest;
import org.nodes.wms.dao.basics.customer.dto.input.NewCustomerRequest;
import org.nodes.wms.dao.basics.customer.entities.BasicsCustomer;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户管理工厂类
 */
@Service
@RequiredArgsConstructor

public class CustomersFactory {
	private final OwnerBiz ownerBiz;

	private final CustomerDao customerDao;

	public BasicsCustomer createCustomers(NewCustomerRequest newCustomerRequest) {
		BasicsCustomer basicsCustomer = new BasicsCustomer();
		basicsCustomer.setCode(newCustomerRequest.getCode());
		basicsCustomer.setName(newCustomerRequest.getName());
		basicsCustomer.setSimpleName(newCustomerRequest.getSimpleName());
		basicsCustomer.setStatus(newCustomerRequest.getStatus());
		basicsCustomer.setWoId(newCustomerRequest.getWoId());
		basicsCustomer.setZipCode(newCustomerRequest.getZipCode());
		basicsCustomer.setRemark(newCustomerRequest.getRemark());
		return basicsCustomer;
	}

    public List<BasicsCustomer> createCustomerListForImport(List<CustomerImportRequest> importDataList) {
		List<BasicsCustomer> customerList = new ArrayList<>();
		for (CustomerImportRequest data: importDataList) {
			BasicsCustomer customer = new BasicsCustomer();
			// 根据客户编码查询客户信息
			if (Func.isNotEmpty(data.getOwnerCode())){
				Owner owner = ownerBiz.findByCode(data.getOwnerCode());
				if(Func.isEmpty(owner)){
					throw new ServiceException("导入失败，不存在货主编码："+data.getOwnerCode());
				} else {
					customer.setWoId(owner.getWoId());
				}
			}
			boolean isExist = customerDao.isExistCustomerCode(data.getCode());
			if (isExist) {
				throw new ServiceException("导入失败，客户编码["+ data.getCode()+"]已存在");
			}
			customer.setCode(data.getCode());
			customer.setName(data.getName());
			customer.setSimpleName(data.getSimpleName());
			customer.setRemark(data.getRemark());

			if (!data.getStatus().equals(StatusEnum.ON.getIndex())
				&& !data.getStatus().equals(StatusEnum.OFF.getIndex())){
				throw new ServiceException("导入失败，启用状态只能为1(启用)或者0(禁用)");
			}
			customer.setStatus(data.getStatus());
			customerList.add(customer);
		}
		return customerList;
    }
}
