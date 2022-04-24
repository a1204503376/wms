package org.nodes.wms.dao.basics.carriers.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.carriers.CarriersDao;
import org.nodes.wms.dao.basics.carriers.dto.input.DeleteCarriersRequest;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersPageQuery;
import org.nodes.wms.dao.basics.carriers.dto.output.CarriersResponse;
import org.nodes.wms.dao.basics.carriers.entites.BasicsCarriers;
import org.nodes.wms.dao.basics.carriers.mapper.CarriersMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 *  承运商 DAO实现类
 */
@Service
@RequiredArgsConstructor
public class CarriersDaoImpl extends BaseServiceImpl<CarriersMapper,BasicsCarriers> implements CarriersDao {
	private final CarriersMapper carriersMapper;
	@Override
	public Page<CarriersResponse> selectPage(IPage<?> page, CarriersPageQuery carriersPageQuery) {

		return carriersMapper.getPage(page, carriersPageQuery);
	}

	@Override
	public boolean insert(BasicsCarriers basicscarriers) {
		return super.save(basicscarriers);
	}


	@Override
	public boolean findByCode(String code) {
		LambdaQueryWrapper<BasicsCarriers> lambdaQueryWrapper = new LambdaQueryWrapper();
		lambdaQueryWrapper.eq(BasicsCarriers::getCode,code);
		int count = super.count(lambdaQueryWrapper);
		return count>0;
	}

	@Override
	public boolean delete(DeleteCarriersRequest deleteRequest) {
		return super.deleteLogic(deleteRequest.getList());
	}
}







