package org.nodes.wms.core.outstock.so.service.impl;

import org.nodes.wms.core.outstock.so.dto.CreatePickPlanDTO;
import org.nodes.wms.core.outstock.so.service.IPickPlanService;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 手动分配模式
 * @author wuhongjie
 * @date 2022/2/9 13:35
 */
@Primary
public class ManualAllocStrategyMode<T,S> implements ISwitchAllocStrategyMode<T,S>{
	IPickPlanService pickPlanService;
	public ManualAllocStrategyMode(){
		pickPlanService = SpringUtil.getBean(IPickPlanService.class);
	}
	@Override
	public T switchStrategy(ISwitchAllocStrategyMode.AllocTypeEnum allocTypeEnum,S s) {
		T t = null;
		switch (allocTypeEnum){
			case ORDER:
				t = (T) pickPlanService.createPickPlan((CreatePickPlanDTO) s);
				break;
			case DETAIL:
				break;
			case WELLEN:
				break;
		}
		return t;
	}
}
