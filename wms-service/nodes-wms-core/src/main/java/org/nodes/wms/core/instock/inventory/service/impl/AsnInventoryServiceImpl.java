package org.nodes.wms.core.instock.inventory.service.impl;

import org.nodes.wms.core.instock.asn.entity.ContainerLog;
import org.nodes.wms.core.instock.asn.service.IContainerLogService;
import org.nodes.wms.core.instock.asn.wrapper.ContainerLogWrapper;
import org.nodes.wms.core.instock.inventory.entity.AsnInventory;
import org.nodes.wms.core.instock.inventory.vo.AsnInventoryVO;
import org.nodes.wms.core.instock.inventory.mapper.AsnInventoryMapper;
import org.nodes.wms.core.instock.inventory.service.IAsnInventoryService;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.nodes.wms.core.instock.inventory.wrapper.AsnInventoryWrapper;

import java.util.List;

/**
 * 收货清单头表 服务实现类
 *
 * @author NodeX
 * @since 2021-06-09
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED,isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class AsnInventoryServiceImpl<M extends AsnInventoryMapper, T extends AsnInventory>
    extends BaseServiceImpl<AsnInventoryMapper, AsnInventory>
    implements IAsnInventoryService {

	@Autowired
	IContainerLogService containerLogService;

	@Override
	public AsnInventoryVO getDetail(Long id) {
		//查询收货单详情
		AsnInventory asnInventory = super.getById(id);
		if (Func.isEmpty(asnInventory)) {
			throw new ServiceException("订单已删除或不存在");
		}
		AsnInventoryVO asnInventoryVO = AsnInventoryWrapper.build().entityVO(asnInventory);
		//查询收货单明细分页列表
		List<ContainerLog> containerLogList = containerLogService.list(
			Condition.getQueryWrapper(new ContainerLog()).lambda()
				.eq(ContainerLog::getInventoryId, id)
				.orderByAsc(ContainerLog::getCreateTime));
		asnInventoryVO.setDetailList(ContainerLogWrapper.build().listVO(containerLogList));
		return asnInventoryVO;
	}
}
