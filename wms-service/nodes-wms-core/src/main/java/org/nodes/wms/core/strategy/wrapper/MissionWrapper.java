package org.nodes.wms.core.strategy.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.utils.StringPool;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.nodes.wms.core.strategy.entity.Mission;
import org.nodes.wms.core.strategy.vo.MissionVO;


import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;


/**
 * 包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2021-05-24
 */
public class MissionWrapper extends BaseEntityWrapper<Mission, MissionVO> {

	public static MissionWrapper build() {
		return new MissionWrapper();
	}

	@Override
	public MissionVO entityVO(Mission mission) {
		MissionVO missionVO = BeanUtil.copy(mission, MissionVO.class);
		//任务类型
		missionVO.setMissionTypeName(DictCache.getValue(DictConstant.TASK_TYPE, mission.getMissionType()));
		//推送方式
		missionVO.setPushWayName(DictCache.getValue(DictConstant.TASK_PUSH_WAY, mission.getPushWay()));
		//计算方式
		missionVO.setCountWayName(DictCache.getValue(DictConstant.TASK_COUNT_WAY, mission.getCountWay()));
		//超过数量是否分割
		if (Func.isNotEmpty(mission.getIsExceedcountSegment())) {
			missionVO.setIsExceedcountSegmentName(missionVO.getIsExceedcountSegment() != 0 ? StringPool.CHS_YES : StringPool.CHS_NO);

		}

		return missionVO;
	}
}
