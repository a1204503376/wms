package org.nodes.wms.dao.basics.carrier.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.carrier.CarriersDao;
import org.nodes.wms.dao.basics.carrier.dto.input.CarrierDropDownRequest;
import org.nodes.wms.dao.basics.carrier.dto.input.CarrierPageQuery;
import org.nodes.wms.dao.basics.carrier.dto.input.DeleteCarriersRequest;
import org.nodes.wms.dao.basics.carrier.dto.output.CarrierDropDownResponse;
import org.nodes.wms.dao.basics.carrier.dto.output.CarrierExcelResponse;
import org.nodes.wms.dao.basics.carrier.dto.output.CarrierResponse;
import org.nodes.wms.dao.basics.carrier.entites.BasicsCarrier;
import org.nodes.wms.dao.basics.carrier.mapper.CarriersMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * 承运商 DAO实现类
 */
@Service
@RequiredArgsConstructor
public class CarriersDaoImpl extends BaseServiceImpl<CarriersMapper, BasicsCarrier> implements CarriersDao {
	private final CarriersMapper carriersMapper;

	@Override
	public Page<CarrierResponse> selectPage(IPage<?> page, CarrierPageQuery carrierPageQuery) {

		return carriersMapper.getPage(page, carrierPageQuery);
	}

	@Override
	public boolean insert(BasicsCarrier basicscarriers) {
		return super.save(basicscarriers);
	}


	@Override
	public boolean isExistCarrierCode(String code) {
		LambdaQueryWrapper<BasicsCarrier> lambdaQueryWrapper = new LambdaQueryWrapper();
		lambdaQueryWrapper.eq(BasicsCarrier::getCode, code);
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
	public List<CarrierExcelResponse> getCarriers(HashMap<String, Object> params) {
		return carriersMapper.getCarriers(params);
	}

	/**
	 * 根据ID修改状态
	 * @param basicsCarrier 内含id与状态
	 * @return 是否成功
	 */
	@Override
	public Boolean updateStatus(BasicsCarrier basicsCarrier) {
		return super.updateById(basicsCarrier);
	}

	/**
	 *  无条件的查询
	 * @return 承运商集合
	 */
	@Override
	public List<CarrierDropDownResponse> getCarrierUnconditional(CarrierDropDownRequest carrierDropDownRequest) {
		return carriersMapper.getCarrierUnconditional(carrierDropDownRequest);
	}

    @Override
    public boolean importExcel(List<BasicsCarrier> importDataList) {
        return super.saveBatch(importDataList);
	}

	@Override
	public BasicsCarrier getCarrierById(Long id) {
		return super.getById(id);
	}

	@Override
	public BasicsCarrier getCarrierByCode(String code) {
		return super.getOne(new LambdaQueryWrapper<BasicsCarrier>().eq(BasicsCarrier::getCode,code));
	}
}







