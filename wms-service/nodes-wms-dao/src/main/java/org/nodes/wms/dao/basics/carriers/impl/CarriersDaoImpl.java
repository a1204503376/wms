package org.nodes.wms.dao.basics.carriers.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.carriers.CarriersDao;
import org.nodes.wms.dao.basics.carriers.dto.input.DeleteCarriersRequest;
import org.nodes.wms.dao.basics.carriers.dto.input.CarrierPageQuery;
import org.nodes.wms.dao.basics.carriers.dto.output.CarrierExcelResponse;
import org.nodes.wms.dao.basics.carriers.dto.output.CarrierResponse;
import org.nodes.wms.dao.basics.carriers.entites.BasicsCarriers;
import org.nodes.wms.dao.basics.carriers.mapper.CarriersMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * 承运商 DAO实现类
 */
@Service
@RequiredArgsConstructor
public class CarriersDaoImpl extends BaseServiceImpl<CarriersMapper, BasicsCarriers> implements CarriersDao {
	private final CarriersMapper carriersMapper;

	@Override
	public Page<CarrierResponse> selectPage(IPage<?> page, CarrierPageQuery carrierPageQuery) {

		return carriersMapper.getPage(page, carrierPageQuery);
	}

	@Override
	public boolean insert(BasicsCarriers basicscarriers) {
		return super.save(basicscarriers);
	}


	@Override
	public boolean isExistCarrierCode(String code) {
		LambdaQueryWrapper<BasicsCarriers> lambdaQueryWrapper = new LambdaQueryWrapper();
		lambdaQueryWrapper.eq(BasicsCarriers::getCode, code);
		int count = super.count(lambdaQueryWrapper);
		return count > 0;
	}

	@Override
	public boolean delete(DeleteCarriersRequest deleteRequest) {
		return super.deleteLogic(deleteRequest.getList());
	}

	/**
	 * 导出Excel
	 *
	 * @param params   查询条件
	 */
	@Override
	public List<CarrierExcelResponse> exportExcel(HashMap<String, Object> params) {
		return carriersMapper.getExcel(params);
	}

	/**
	 * 根据ID修改状态
	 * @param basicsCarriers 内含id与状态
	 * @return 是否成功
	 */
	@Override
	public Boolean updateStatus(BasicsCarriers basicsCarriers) {
		return super.updateById(basicsCarriers);
	}
}







