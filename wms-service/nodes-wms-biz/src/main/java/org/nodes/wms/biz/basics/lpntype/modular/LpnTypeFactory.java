package org.nodes.wms.biz.basics.lpntype.modular;

import org.nodes.wms.dao.basics.lpntype.dto.input.NewLpnTypeRequest;
import org.nodes.wms.dao.basics.lpntype.dto.input.UpdateLpnTypeRequest;
import org.nodes.wms.dao.basics.lpntype.dto.output.LpnTypeByIdResponse;
import org.nodes.wms.dao.basics.lpntype.entities.LpnType;
import org.nodes.wms.dao.basics.lpntype.enums.LpnTypeEnum;
import org.springframework.stereotype.Service;

/**
 * 容器管理工厂
 */
@Service
public class LpnTypeFactory {
	public LpnType createLpnType(NewLpnTypeRequest lpnTypeRequest){
		LpnType lpnType=new LpnType();
		LpnTypeEnum[] values = LpnTypeEnum.values();
		for(LpnTypeEnum lpnTypeEnum:values){
			if(lpnTypeEnum.key().intValue()== lpnTypeRequest.getLpnType().intValue())
			{
				lpnType.setType(lpnTypeEnum);
			}
		}
		lpnType.setLpnNoRule(lpnTypeRequest.getLpnNoRule());
        lpnType.setCode(lpnTypeRequest.getCode());
		lpnType.setWeight(lpnTypeRequest.getWeight());
		return lpnType;
	}
	public LpnTypeByIdResponse createLpnTypeByIdResponse(LpnType lpnType){
		LpnTypeByIdResponse lpnTypeByIdResponse=new LpnTypeByIdResponse();
		lpnTypeByIdResponse.setLpnType(lpnType.getType().getCode());
		lpnTypeByIdResponse.setId(lpnType.getId());
		lpnTypeByIdResponse.setCode(lpnType.getCode());
		lpnTypeByIdResponse.setLpnNoRule(lpnType.getLpnNoRule());
		lpnTypeByIdResponse.setWeight(lpnType.getWeight());
		return lpnTypeByIdResponse;
	}

	public LpnType createLpnType(UpdateLpnTypeRequest updateLpnTypeRequest){
		LpnType lpnType=new LpnType();
		LpnTypeEnum[] values = LpnTypeEnum.values();
		for(LpnTypeEnum lpnTypeEnum:values){
			if(lpnTypeEnum.key().intValue()== updateLpnTypeRequest.getLpnType().intValue())
			{
				lpnType.setType(lpnTypeEnum);
			}
		}
		lpnType.setId(updateLpnTypeRequest.getId());
		lpnType.setLpnNoRule(updateLpnTypeRequest.getLpnNoRule());
		lpnType.setWeight(updateLpnTypeRequest.getWeight());
		lpnType.setCode(updateLpnTypeRequest.getCode());
		return lpnType;
	}
}
