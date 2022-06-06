package org.nodes.wms.biz.basics.carriers.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.core.enums.StatusEnum;
import org.nodes.wms.biz.basics.owner.OwnerBiz;
import org.nodes.wms.dao.basics.carrier.CarriersDao;
import org.nodes.wms.dao.basics.carrier.dto.input.CarrierExcelRequest;
import org.nodes.wms.dao.basics.carrier.dto.input.NewCarrierRequest;
import org.nodes.wms.dao.basics.carrier.dto.input.UpdateStatusRequest;
import org.nodes.wms.dao.basics.carrier.entites.BasicsCarriers;
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

	public BasicsCarriers createCarriers(NewCarrierRequest newCarrierRequest) {
		BasicsCarriers basicsCarriers = new BasicsCarriers();
		basicsCarriers.setCode(newCarrierRequest.getCode());
		basicsCarriers.setName(newCarrierRequest.getName());
		basicsCarriers.setSimpleName(newCarrierRequest.getSimpleName());
		basicsCarriers.setStatus(newCarrierRequest.getStatus());
		basicsCarriers.setWoId(newCarrierRequest.getWoId());
		basicsCarriers.setRemark(newCarrierRequest.getRemark());
		return  basicsCarriers;
	}
	public BasicsCarriers createCarriers(UpdateStatusRequest updateStatusRequest) {
		BasicsCarriers basicsCarriers = new BasicsCarriers();
		basicsCarriers.setStatus(updateStatusRequest.getStatus());
        basicsCarriers.setId(updateStatusRequest.getId());
		return  basicsCarriers;
	}

    public List<BasicsCarriers> createCarrierListForImport(List<CarrierExcelRequest> importDataList) {
		List<BasicsCarriers> carrierList = new ArrayList<>();
		for (CarrierExcelRequest data: importDataList) {
			BasicsCarriers carrier = new BasicsCarriers();
			if(Func.isEmpty(data.getCode())){
				throw new ServiceException("导入失败，承运商编码不能为空");
			}
			if(Func.isEmpty(data.getName())){
				throw new ServiceException("导入失败，承运商名称不能为空");
			}
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
				throw new ServiceException("导入失败，承运商编码["+data.getCode()+"]已存在");
			}
			carrier.setCode(data.getCode());
			carrier.setName(data.getName());
			carrier.setSimpleName(data.getSimpleName());
			carrier.setRemark(data.getRemark());

			if(Func.isEmpty(data.getStatus())){
				throw new ServiceException("导入失败，启用状态不能为空");
			}
			if (!data.getStatus().equals(StatusEnum.ON.getIndex())
				&& !data.getStatus().equals(StatusEnum.OFF.getIndex())){
				throw new ServiceException("导入失败，启用状态只能为1(启用)或者-1(禁用)");
			}
			carrier.setStatus(data.getStatus());
			carrierList.add(carrier);
		}
		return carrierList;
    }
}
