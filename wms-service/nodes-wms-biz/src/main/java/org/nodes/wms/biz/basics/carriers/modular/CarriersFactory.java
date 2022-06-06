package org.nodes.wms.biz.basics.carriers.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.core.enums.StatusEnum;
import org.nodes.wms.biz.basics.owner.OwnerBiz;
import org.nodes.wms.dao.basics.carrier.CarriersDao;
import org.nodes.wms.dao.basics.carrier.dto.input.CarrierExcelRequest;
import org.nodes.wms.dao.basics.carrier.dto.input.NewCarrierRequest;
import org.nodes.wms.dao.basics.carrier.dto.input.UpdateStatusRequest;
import org.nodes.wms.dao.basics.carrier.entites.BasicsCarrier;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 承运商管理工厂类
 */
@Service
@RequiredArgsConstructor
public class CarriersFactory {

	private final OwnerBiz ownerBiz;

	private final CarriersDao carrierDao;

	public BasicsCarrier createCarriers(NewCarrierRequest newCarrierRequest) {
		BasicsCarrier basicsCarrier = new BasicsCarrier();
		basicsCarrier.setCode(newCarrierRequest.getCode());
		basicsCarrier.setName(newCarrierRequest.getName());
		basicsCarrier.setSimpleName(newCarrierRequest.getSimpleName());
		basicsCarrier.setStatus(newCarrierRequest.getStatus());
		basicsCarrier.setWoId(newCarrierRequest.getWoId());
		basicsCarrier.setRemark(newCarrierRequest.getRemark());
		return basicsCarrier;
	}
	public BasicsCarrier createCarriers(UpdateStatusRequest updateStatusRequest) {
		BasicsCarrier basicsCarrier = new BasicsCarrier();
		basicsCarrier.setStatus(updateStatusRequest.getStatus());
        basicsCarrier.setId(updateStatusRequest.getId());
		return basicsCarrier;
	}


    public List<BasicsCarrier> createCarrierListForImport(List<CarrierExcelRequest> importDataList) {
		List<BasicsCarrier> carrierList = new ArrayList<>();
		for (CarrierExcelRequest data: importDataList) {
			BasicsCarrier carrier = new BasicsCarrier();
			// 根据承运商编码查询承运商信息
			if (Func.isNotEmpty(data.getOwnerCode())){
				Owner owner = ownerBiz.findByCode(data.getOwnerCode());
				if(Func.isEmpty(owner)){
					throw new ServiceException("导入失败，不存在货主编码："+data.getOwnerCode());
				} else {
					carrier.setWoId(owner.getWoId());
				}
			}
			boolean isExist = carrierDao.isExistCarrierCode(data.getCode());
			if (isExist) {
				throw new ServiceException("导入失败，承运商编码["+ data.getCode()+"]已存在");
			}
			carrier.setCode(data.getCode());
			carrier.setName(data.getName());
			carrier.setSimpleName(data.getSimpleName());
			carrier.setRemark(data.getRemark());

			if (!data.getStatus().equals(StatusEnum.ON.getIndex())
				&& !data.getStatus().equals(StatusEnum.OFF.getIndex())){
				throw new ServiceException("导入失败，启用状态只能为1(启用)或者0(禁用)");
			}
			carrier.setStatus(data.getStatus());
			carrierList.add(carrier);
		}
		return carrierList;
    }
}
