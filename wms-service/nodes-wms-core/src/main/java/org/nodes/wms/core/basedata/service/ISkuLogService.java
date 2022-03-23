package org.nodes.wms.core.basedata.service;

import org.nodes.wms.core.basedata.dto.SkuLogDTO;
import org.nodes.wms.core.basedata.entity.SkuLog;
import org.springblade.core.mp.base.BaseService;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * 物品操作记录表 服务类
 *
 * @author pengwei
 * @since 2020-06-29
 */
public interface ISkuLogService extends BaseService<SkuLog> {

	/**
	 * 保存或更新物品日志
	 *
	 * @param skuLogDTO 物品日志参数
	 * @return 是否成功
	 */
	boolean saveOrUpdate(@NotNull @Validated SkuLogDTO skuLogDTO);
}
