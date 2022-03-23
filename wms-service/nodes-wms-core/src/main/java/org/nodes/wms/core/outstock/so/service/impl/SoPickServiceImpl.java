package org.nodes.wms.core.outstock.so.service.impl;

import org.nodes.wms.core.outstock.so.dto.PickPlanDTO;
import org.nodes.wms.core.outstock.so.entity.PickPlan;
import org.nodes.wms.core.outstock.so.entity.SoPick;
import org.nodes.wms.core.outstock.so.mapper.SoPickMapper;
import org.nodes.wms.core.outstock.so.service.IPickPlanService;
import org.nodes.wms.core.outstock.so.service.ISoPickService;
import org.nodes.wms.core.outstock.so.vo.PickLogVO;
import org.nodes.wms.core.outstock.so.wrapper.SoPickWrapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 拣货记录日志 服务实现类
 *
 * @author zx
 * @since 2020-03-04
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SoPickServiceImpl<M extends SoPickMapper, T extends SoPick>
	extends BaseServiceImpl<SoPickMapper, SoPick>
	implements ISoPickService {

	/**
	 * 获得拣货日志
	 * @param list
	 * @return
	 */
	@Override
	public List<PickLogVO> getPickLog(List<SoPick> list){
		List<PickLogVO> pickLogVOs = new ArrayList<>();
		for (SoPick soPick : list) {
			PickPlanDTO pickPlanDTO = new PickPlanDTO();
			PickPlan pickPlan = SpringUtil.getBean(IPickPlanService.class).getById(soPick.getPickPlanId());
			pickPlanDTO.setPickPlanQty(pickPlan.getPickPlanQty());

			PickLogVO pickLogVO = SoPickWrapper.soPickList2PickLogVO(soPick,pickPlanDTO);
			pickLogVOs.add(pickLogVO);
		}
		return pickLogVOs;
	}

}
