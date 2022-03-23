package org.nodes.wms.core.outstock.inventory.service.impl;

import org.nodes.wms.core.outstock.inventory.entity.SoInventory;
import org.nodes.wms.core.outstock.inventory.vo.SoInventoryVO;
import org.nodes.wms.core.outstock.inventory.mapper.SoInventoryMapper;
import org.nodes.wms.core.outstock.inventory.service.ISoInventoryService;
import org.nodes.wms.core.outstock.so.entity.SoPick;
import org.nodes.wms.core.outstock.so.service.ISoPickService;
import org.nodes.wms.core.outstock.so.wrapper.SoPickWrapper;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springblade.core.tool.utils.Func;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.nodes.wms.core.outstock.inventory.wrapper.SoInventoryWrapper;

import java.util.List;

/**
 * 发货清单主表
 服务实现类
 *
 * @author NodeX
 * @since 2021-06-09
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED,isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SoInventoryServiceImpl<M extends SoInventoryMapper, T extends SoInventory>
    extends BaseServiceImpl<SoInventoryMapper, SoInventory>
    implements ISoInventoryService {

	@Autowired
	ISoPickService soPickService;

	@Override
	public SoInventoryVO getDetail(Long id) {
		//查询收货单详情
		SoInventory soInventory = super.getById(id);
		if (Func.isEmpty(soInventory)) {
			throw new ServiceException("订单已删除或不存在");
		}
		SoInventoryVO soInventoryVO = SoInventoryWrapper.build().entityVO(soInventory);
		//查询收货单明细分页列表
		List<SoPick> soPickList = soPickService.list(
			Condition.getQueryWrapper(new SoPick()).lambda()
				.eq(SoPick::getInventoryId, id)
				.orderByAsc(SoPick::getCreateTime));
		soInventoryVO.setDetailList(SoPickWrapper.build().listVO(soPickList));
		return soInventoryVO;
	}
}
