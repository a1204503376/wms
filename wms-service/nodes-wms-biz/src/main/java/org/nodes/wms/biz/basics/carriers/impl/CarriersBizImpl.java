package org.nodes.wms.biz.basics.carriers.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.carriers.CarriersBiz;
import org.nodes.wms.biz.basics.carriers.modular.CarriersFactory;
import org.nodes.wms.dao.basics.carriers.CarriersDao;
import org.nodes.wms.dao.basics.carriers.dto.input.DeleteCarriersRequest;
import org.nodes.wms.dao.basics.carriers.dto.input.CarrierPageQuery;
import org.nodes.wms.dao.basics.carriers.dto.input.newCarrierRequest;
import org.nodes.wms.dao.basics.carriers.dto.output.CarrierResponse;
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
	public Page<CarrierResponse> getPage(Query query, CarrierPageQuery carrierPageQuery) {
		IPage<CustomersResponse> page = Condition.getPage(query);
		return carriersDao.selectPage(page, carrierPageQuery);
	}

	@Override
	public boolean newCarrier(newCarrierRequest newCarrierRequest) {
		boolean isExist = carriersDao.isExistCarrierCode(newCarrierRequest.getCode());
		if(isExist){
			throw new ServiceException("新增承运商失败，编码重复");
		}

		BasicsCarriers basicscarriers = carriersFactory.createCarriers(newCarrierRequest);
		return  carriersDao.insert(basicscarriers);
	}
	@Override
	public boolean remove(DeleteCarriersRequest deleteRequest) {
		return carriersDao.delete(deleteRequest);
	}
}
