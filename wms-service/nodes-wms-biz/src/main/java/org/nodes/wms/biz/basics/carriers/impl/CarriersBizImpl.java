package org.nodes.wms.biz.basics.carriers.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.carriers.CarriersBiz;
import org.nodes.wms.biz.basics.carriers.modular.CarriersFactory;
import org.nodes.wms.dao.basics.carriers.CarriersDao;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersDeleteRequest;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersPageQuery;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersRequest;
import org.nodes.wms.dao.basics.carriers.dto.output.CarriersResponse;
import org.nodes.wms.dao.basics.carriers.entites.BasicsCarriers;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
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
	public Page<CarriersResponse> page(IPage<CarriersResponse> page, CarriersPageQuery carriersPageQuery) {

		return carriersDao.getcarriersPage(page,carriersPageQuery);
	}

	@Override
	public int save(CarriersRequest carriersRequest) {
		BasicsCarriers basicscarriers = carriersFactory.createCarriers(carriersRequest);
		String code =  carriersDao.findByCode(basicscarriers.getCode());
		if(Func.isNotEmpty(code)){
			throw new ServiceException("承运商编码重复");
		}

		return  carriersDao.save(basicscarriers);
	}

	@Override
	public int update(CarriersRequest carriersRequest) {
		BasicsCarriers basicscarriers = carriersFactory.createCarriers(carriersRequest);
		if(Func.isEmpty(basicscarriers.getId())){
			throw new ServiceException("ID不能为空");
		}
		String code =  carriersDao.findByCode(basicscarriers.getCode());
		if(Func.isNotEmpty(code)){
			throw new ServiceException("承运商编码重复");
		}

		return carriersDao.update(basicscarriers);
	}

	@Override
	public int delete(CarriersDeleteRequest deleteRequest) {
		return carriersDao.delete(deleteRequest);
	}
}
