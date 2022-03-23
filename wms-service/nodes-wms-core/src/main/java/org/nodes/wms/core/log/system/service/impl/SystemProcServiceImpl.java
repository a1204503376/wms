package org.nodes.wms.core.log.system.service.impl;

import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.entity.SystemProc;
import org.nodes.wms.core.log.system.enums.PlatformEnum;
import org.nodes.wms.core.log.system.mapper.SystemProcMapper;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringPool;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 系统日志 服务实现类
 *
 * @author NodeX
 * @since 2020-02-12
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SystemProcServiceImpl<M extends SystemProcMapper, T extends SystemProc>
	extends BaseServiceImpl<SystemProcMapper, SystemProc>
	implements ISystemProcService {

	/**
	 * 创建系统日志
	 *
	 * @param param 日志参数SystemProcParamDTO
	 * @return 创建的系统日志
	 */
	@Override
	public SystemProc create(@Validated @NotNull SystemProcDTO param) {
		// 暂时将系统区分设置为PC，后期再处理
		param.setPlatform(PlatformEnum.PC);
		// 拼接日志内容
		StringBuilder content = new StringBuilder();
		content.append("[").append(param.getPlatform().getName());
		if (Func.isNotEmpty(param.getAction())) {
			content.append("-").append(param.getAction().getName());
		}
		content.append("]");
		if (Func.isNotEmpty(param.getDataType()) && Func.isNotEmpty(param.getBillNo())) {
			content.append("[")
				.append(param.getDataType())
				.append(":")
				.append(param.getBillNo())
				.append("]");
		}
		content.append("[库位编码：").append(param.getLocCode()).append("]");
		content.append("[容器:").append(param.getLpnCode()).append("]");
		content.append("[箱号:").append(param.getBoxCode()).append("]");
		content.append("[").append(param.getSkuCode()).append(":").append(param.getSkuName()).append("]");
		content.append("[").append(param.getProcType().getName()).append("]");

		if (Func.isNotEmpty(param.getOperationQty()) && Func.isNotEmpty(param.getOperationUnit())) {
			content.append("[")
				.append(param.getOperationQty())
				.append(StringPool.SPACE)
				.append(param.getOperationUnit())
				.append("]");
		}
		if (Func.isNotEmpty(param.getRemark())) {
			content.append("[PS:").append(param.getRemark()).append("]");
		}

		// 封装日志
		SystemProc systemProc = new SystemProc();
		systemProc.setProcTime(new Date());
		systemProc.setProcType(param.getProcType().getName());
		systemProc.setSystemProcLog(content.toString());
		systemProc.setUserId(AuthUtil.getUserId());
		systemProc.setUserName(AuthUtil.getNickName());
		if (Func.isNotEmpty(param.getBillNo())) {
			systemProc.setDataId(param.getBillNo());
		}
		if (Func.isNotEmpty(param.getDataType())) {
			systemProc.setDataItem(param.getDataType().getIndex());
		}
		systemProc.setWhId(param.getWhId());

		super.save(systemProc);

		return systemProc;
	}
}
