package org.nodes.wms.core.outstock.so.service.impl;

/**
 * 切换分配策略模式
 * @author wuhongjie
 * @date 2022/2/9 13:32
 */
public interface ISwitchAllocStrategyMode<T,S> {
	//分配模式
	enum AllocModeEnum{
		//自动
		AUTO,
		//手动
		MANUAL;
		public static AllocModeEnum getAllocModeByValue(int value){
			for(AllocModeEnum allocModeEnum:values()){
				if(value == allocModeEnum.ordinal()){
					return allocModeEnum;
				}
			}
			return null;
		}
	}
	//分配类型
	enum AllocTypeEnum{
		//按单分配
		ORDER,
		//波次分配
		WELLEN,
		//明细分配
		DETAIL
	}

	T switchStrategy(ISwitchAllocStrategyMode.AllocTypeEnum allocTypeEnum,S s);
}
