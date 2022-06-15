package org.nodes.wms.dao.basics.customer.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.customer.CustomerDao;
import org.nodes.wms.dao.basics.customer.dto.input.CustomerPageQuery;
import org.nodes.wms.dao.basics.customer.dto.input.DeleteCustomerRequest;
import org.nodes.wms.dao.basics.customer.dto.output.CustomerResponse;
import org.nodes.wms.dao.basics.customer.dto.output.CustomerSelectResponse;
import org.nodes.wms.dao.basics.customer.entities.BasicsCustomer;
import org.nodes.wms.dao.basics.customer.mapper.CustomerMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户表 DAO 实现类
 */
@Service
@RequiredArgsConstructor
public class CustomerDaoImpl extends BaseServiceImpl<CustomerMapper, BasicsCustomer> implements CustomerDao {
	 private final CustomerMapper customerMapper;

	@Override
	public boolean insert(BasicsCustomer basicsCustomer) {
		return super.save(basicsCustomer);
	}


	@Override
	public boolean isExistCustomerCode(String code) {
		LambdaQueryWrapper<BasicsCustomer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(BasicsCustomer::getCode,code);
		int count = super.count(lambdaQueryWrapper);
		return count>0;
	}

	@Override
	public boolean delete(DeleteCustomerRequest deleteRequest) {
		return super.deleteLogic(deleteRequest.getIds());
	}

	@Override
	public List<CustomerResponse> getCustomerResponseByQuery(CustomerPageQuery customerPageQuery) {
		return customerMapper.getCustomerResponseByQuery(customerPageQuery);
	}

	@Override
	public List<CustomerSelectResponse> listTop10ByCodeName(String code, String name) {
		return super.baseMapper.listTop10ByCodName(code, name);
	}

    @Override
	public Page<CustomerResponse> selectPage(IPage<CustomerResponse> page, CustomerPageQuery customerPageQuery) {
		return  customerMapper.getPage(page, customerPageQuery);
	}

	@Override
    public boolean importExcel(List<BasicsCustomer> customerList) {
        return super.saveBatch(customerList);
    }

	@Override
	public BasicsCustomer getCustomerById(Long id) {
		return super.getById(id);
	}

	@Override
	public BasicsCustomer getCustomerByCode(String code) {
		return super.getOne(new LambdaQueryWrapper<BasicsCustomer>().eq(BasicsCustomer::getCode,code));
	}

    @Override
    public void update(BasicsCustomer basicsCustomer) {
            super.update(new LambdaUpdateWrapper<BasicsCustomer>()
			.eq(BasicsCustomer::getCode,basicsCustomer.getCode())
			.set(BasicsCustomer::getName,basicsCustomer.getName()));
    }
}
