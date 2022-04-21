package org.nodes.wms.biz.basics.customers.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.customers.CustomersBiz;
import org.nodes.wms.biz.basics.customers.modular.CustomersFactory;
import org.nodes.wms.dao.basics.customers.CustomersDao;
import org.nodes.wms.dao.basics.customers.dto.input.CustomersPageQuery;
import org.nodes.wms.dao.basics.customers.dto.input.CustomersRequest;
import org.nodes.wms.dao.basics.customers.dto.input.DeleteRequest;
import org.nodes.wms.dao.basics.customers.dto.output.CustomersResponse;
import org.nodes.wms.dao.basics.customers.entities.BasicsCustomers;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.stereotype.Service;

/**
 * 客户管理 业务类
 */
@Service
@RequiredArgsConstructor
public class CustomersBizImpl implements CustomersBiz {
	private  final CustomersDao customersDao;
	private  final CustomersFactory customersFactory;

	@Override
	public Page<CustomersResponse> page(IPage<CustomersResponse> page, CustomersPageQuery customersPageQuery) {

		return customersDao.getCustomersPage(page,customersPageQuery);
	}

	@Override
	public int save(CustomersRequest customersRequest) {
		BasicsCustomers basicsCustomers = customersFactory.createCustomers(customersRequest);
		String code =  customersDao.findByCode(basicsCustomers.getCode());
		if(Func.isNotEmpty(code)){
			throw new ServiceException("客户编码重复");
		}

		return  customersDao.save(basicsCustomers);
	}

	@Override
	public int update(CustomersRequest customersRequest) {
		BasicsCustomers basicsCustomers = customersFactory.createCustomers(customersRequest);
		if(Func.isEmpty(basicsCustomers.getId())){
			throw new ServiceException("ID不能为空");
		}
		String code =  customersDao.findByCode(basicsCustomers.getCode());
		if(Func.isNotEmpty(code)){
			throw new ServiceException("客户编码重复");
		}

		return customersDao.update(basicsCustomers);
	}

	@Override
	public int delete(DeleteRequest deleteRequest) {
		return customersDao.delete(deleteRequest);
	}
}
