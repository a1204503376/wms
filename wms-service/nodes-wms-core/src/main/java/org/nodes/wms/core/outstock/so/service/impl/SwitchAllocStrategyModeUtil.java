package org.nodes.wms.core.outstock.so.service.impl;


import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.base.entity.Param;
import org.nodes.core.base.enums.ParamEnum;
import org.nodes.core.base.service.IParamService;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

/**
 * 根据分配模式执行分配逻辑
 *
 * @author wuhongjie
 * @date 2022/2/9 13:36
 */
public class SwitchAllocStrategyModeUtil {
	private static ISwitchAllocStrategyMode strategy;

	public static <T, S> T executeStrategy(ISwitchAllocStrategyMode.AllocTypeEnum allocTypeEnum, S s) {
		int allocMode = Func.toInt(ParamCache.getValue(ParamEnum.STRATEGY_ALLOC_MODE.getKey()));
		ISwitchAllocStrategyMode.AllocModeEnum allocModeByValue = ISwitchAllocStrategyMode.AllocModeEnum.getAllocModeByValue(allocMode);
		switch (allocModeByValue) {
			case AUTO:
				strategy = new AutoAllocStrategyMode<>();
				break;
			case MANUAL:
				strategy = new ManualAllocStrategyMode<>();
				break;
		}
		return (T) strategy.switchStrategy(allocTypeEnum, s);
	}
}
