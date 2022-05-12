package org.nodes.wms.biz.basics.lpntype.modular;

import org.nodes.wms.dao.basics.lpntype.dto.input.NewLpnTypeRequest;
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
		lpnType.setType(lpnTypeRequest.getType());
        lpnType.setLpnNoRule(lpnTypeRequest.getLpnNoRule());
        lpnType.setCode(lpnTypeRequest.getCode());
		lpnType.setWeight(lpnTypeRequest.getWeight());
		return lpnType;
	}
}
