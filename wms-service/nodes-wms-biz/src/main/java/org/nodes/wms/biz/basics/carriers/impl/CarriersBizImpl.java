package org.nodes.wms.biz.basics.carriers.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.carriers.CarriersBiz;
import org.nodes.wms.biz.basics.carriers.modular.CarriersFactory;
import org.nodes.wms.dao.basics.carrier.CarriersDao;
import org.nodes.wms.dao.basics.carrier.dto.input.DeleteCarriersRequest;
import org.nodes.wms.dao.basics.carrier.dto.input.CarrierPageQuery;
import org.nodes.wms.dao.basics.carrier.dto.input.NewCarrierRequest;
import org.nodes.wms.dao.basics.carrier.dto.input.UpdateStatusRequest;
import org.nodes.wms.dao.basics.carrier.dto.output.CarrierExcelResponse;
import org.nodes.wms.dao.basics.carrier.dto.output.CarrierResponse;
import org.nodes.wms.dao.basics.carrier.entites.BasicsCarriers;
import org.nodes.wms.dao.basics.customer.dto.output.CustomerResponse;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

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
		IPage<CustomerResponse> page = Condition.getPage(query);
		return carriersDao.selectPage(page, carrierPageQuery);
	}

	@Override
	public boolean newCarrier(NewCarrierRequest newCarrierRequest) {
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

	@Override
	public void exportExcel(HashMap<String, Object> params, HttpServletResponse response) {
		List<CarrierExcelResponse> basicsCarriersList = carriersDao.getCarriers(params);
		ExcelUtil.export(response, "承运商", "承运商位数据表", basicsCarriersList, CarrierExcelResponse.class);
	}

	/**
	 * 根据id修改启用状态
	 * @param updateStatusRequest 内含id与状态
	 * @return 是否成功
	 */
	@Override
	public Boolean updateStatusById(UpdateStatusRequest updateStatusRequest) {
		BasicsCarriers carriers = carriersFactory.createCarriers(updateStatusRequest);
		return carriersDao.updateStatus(carriers);
	}
}
