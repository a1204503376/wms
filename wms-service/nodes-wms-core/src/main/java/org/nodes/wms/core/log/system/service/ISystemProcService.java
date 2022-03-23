package org.nodes.wms.core.log.system.service;

import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.entity.SystemProc;
import org.springblade.core.mp.base.BaseService;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * 系统日志 服务类
 *
 * @author NodeX
 * @since 2020-02-12
 */
public interface ISystemProcService extends BaseService<SystemProc> {
	/**
	 * 创建系统日志
	 * @param param 日志参数SystemProcParamDTO
	 * @return 创建的系统日志
	 */
	SystemProc create(@Validated @NotNull SystemProcDTO param);
}
