package org.nodes.wms.dao.basics.carriers.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.carriers.CarriersDao;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersDeleteRequest;
import org.nodes.wms.dao.basics.carriers.dto.input.CarriersPageQuery;
import org.nodes.wms.dao.basics.carriers.dto.output.CarriersResponse;
import org.nodes.wms.dao.basics.carriers.entites.BasicsCarriers;
import org.nodes.wms.dao.basics.carriers.mapper.CarriersMapper;
import org.springframework.stereotype.Service;

/**
 *  承运商 DAO实现类
 */
@Service
@RequiredArgsConstructor
public class CarriersDaoImpl implements CarriersDao {
	private final CarriersMapper carriersMapper;
	@Override
	public Page<CarriersResponse> getcarriersPage(IPage<?> page, CarriersPageQuery carriersPageQuery) {

		return carriersMapper.getCarriersPage(page, carriersPageQuery);
	}

	@Override
	public int save(BasicsCarriers basicscarriers) {
		return carriersMapper.insert(basicscarriers);
	}

	@Override
	public int update(BasicsCarriers basicscarriers) {
		return carriersMapper.updateById(basicscarriers);
	}

	@Override
	public String findByCode(String code) {
		return carriersMapper.selectByCode(code);
	}

	@Override
	public int delete(CarriersDeleteRequest deleteRequest) {
		return carriersMapper.updateByIds(deleteRequest);
	}
}
