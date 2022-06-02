package org.nodes.wms.biz.basics.customers.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.core.enums.StatusEnum;
import org.nodes.wms.biz.basics.owner.OwnerBiz;
import org.nodes.wms.dao.basics.customer.CustomerDao;
import org.nodes.wms.dao.basics.customer.dto.input.CustomerImportRequest;
import org.nodes.wms.dao.basics.customer.dto.input.newCustomerRequest;
import org.nodes.wms.dao.basics.customer.entities.BasicsCustomers;
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

	public BasicsCustomers createCustomers(newCustomerRequest newCustomerRequest) {
		BasicsCustomers basicsCustomers = new BasicsCustomers();
		basicsCustomers.setCode(newCustomerRequest.getCode());
		basicsCustomers.setName(newCustomerRequest.getName());
		basicsCustomers.setSimpleName(newCustomerRequest.getSimpleName());
		basicsCustomers.setStatus(newCustomerRequest.getStatus());
		basicsCustomers.setWoId(newCustomerRequest.getWoId());
		basicsCustomers.setZipCode(newCustomerRequest.getZipCode());
		basicsCustomers.setRemark(newCustomerRequest.getRemark());
		return  basicsCustomers;
	}

    public List<BasicsCustomers> createCustomerListForImport(List<CustomerImportRequest> importDataList) {
		List<BasicsCustomers> customerList = new ArrayList<>();
		for (CustomerImportRequest data: importDataList) {
			BasicsCustomers customer = new BasicsCustomers();
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
