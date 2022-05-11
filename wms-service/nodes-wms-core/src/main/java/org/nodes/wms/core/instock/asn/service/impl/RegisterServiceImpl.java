package org.nodes.wms.core.instock.asn.service.impl;

import org.nodes.wms.core.instock.asn.cache.AsnCache;
import org.nodes.wms.core.instock.asn.entity.AsnHeader;
import org.nodes.wms.core.instock.asn.entity.Register;
import org.nodes.wms.core.instock.asn.mapper.RegisterMapper;
import org.nodes.wms.core.instock.asn.service.IAsnDetailService;
import org.nodes.wms.core.instock.asn.service.IAsnHeaderService;
import org.nodes.wms.core.instock.asn.service.IRegisterService;
import org.nodes.wms.core.instock.inventory.entity.AsnInventory;
import org.nodes.wms.core.instock.inventory.service.IAsnInventoryService;
import org.nodes.wms.core.system.dto.TaskDTO;
import org.nodes.wms.core.system.entity.Task;
import org.nodes.wms.core.system.enums.TaskTypeEnum;
import org.nodes.wms.core.system.service.ITaskService;
import org.nodes.wms.dao.instock.asn.enums.AsnBillStateEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 到货登记 服务实现类
 *
 * @author zhonglianshuai
 * @since 2020-04-07
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class RegisterServiceImpl<M extends RegisterMapper, T extends Register>
	extends BaseServiceImpl<RegisterMapper, Register>
	implements IRegisterService {

	@Autowired
	IAsnHeaderService asnHeaderService;
	@Autowired
	IAsnDetailService asnDetailService;
	@Autowired
	IAsnInventoryService asnInventoryService;
	@Autowired
	ITaskService taskService;

	@Override
	public boolean save(Register register) {
		AsnHeader asnHeader = asnHeaderService.getById(register.getAsnBillId());
		if (Func.isEmpty(asnHeader)) {
			throw new ServiceException("指定入库单不存在（ID：" + register.getAsnBillId() + "）！");
		} else if (asnHeader.getAsnBillState() >= AsnBillStateEnum.COMPLETED.getCode()) {
			throw new ServiceException("该单据不可登记！");
		}
		// 修改订单状态
		asnHeaderService.updateAsnBillState(asnHeader.getAsnBillId(), AsnBillStateEnum.PART);
		// 创建收货清单
		AsnInventory asnInventory = BeanUtil.copy(asnHeader, AsnInventory.class);
		asnInventory.setAsnBillNo(AsnCache.getInventoryNo());
		asnInventory.setOrderNo(asnHeader.getAsnBillNo());
		asnInventory.setBillKey(asnHeader.getAsnBillNo());
		asnInventoryService.save(asnInventory);
		// 创建收货任务
		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setTaskType(TaskTypeEnum.Check.getIndex());
		taskDTO.setWhId(asnHeader.getWhId());
		taskDTO.setBillTypeCd(asnHeader.getBillTypeCd());
		taskDTO.setBillId(asnHeader.getAsnBillId());
		taskDTO.setBillNo(asnHeader.getAsnBillNo());
		taskDTO.setTaskQty(asnDetailService.list(asnHeader.getAsnBillId()).size());
		taskDTO.setRemark(asnInventory.getAsnBillNo());
		Task task = taskService.create(taskDTO);

		return super.save(register);
	}

}
