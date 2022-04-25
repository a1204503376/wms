package org.nodes.wms.biz.basics.carriers.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.carriers.CarriersBiz;
import org.nodes.wms.biz.basics.carriers.modular.CarriersFactory;
import org.nodes.wms.dao.basics.carriers.CarriersDao;
import org.nodes.wms.dao.basics.carriers.dto.input.DeleteCarriersRequest;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersPageQuery;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersRequest;
import org.nodes.wms.dao.basics.carriers.dto.output.CarriersResponse;
import org.nodes.wms.dao.basics.carriers.entites.BasicsCarriers;
import org.nodes.wms.dao.basics.customers.dto.output.CustomersResponse;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springframework.stereotype.Service;

/**
 * 承运商管理业务类
 */
@Service
@RequiredArgsConstructor
public class CarriersBizImpl implements CarriersBiz {
	private  final CarriersDao carriersDao;
	private  final CarriersFactory carriersFactory;
	@Override
	public Page<CarriersResponse> getPage(Query query, CarriersPageQuery carriersPageQuery) {
		IPage<CustomersResponse> page = Condition.getPage(query);
		return carriersDao.selectPage(page,carriersPageQuery);
	}

	@Override
	public boolean saveCarriers(CarriersRequest carriersRequest) {
		BasicsCarriers basicscarriers = carriersFactory.createCarriers(carriersRequest);
		boolean code =  carriersDao.findByCode(basicscarriers.getCode());
		boolean hasCode = carriersDao.findByCode(basicscarriers.getCode());
		if(hasCode){
			throw new ServiceException("客户编码重复");
		}

		return  carriersDao.insert(basicscarriers);
	}
	@Override
	public boolean remove(DeleteCarriersRequest deleteRequest) {
		return carriersDao.delete(deleteRequest);
	}
}
