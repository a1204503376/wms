package org.nodes.wms.core.crontab.service;

import org.nodes.wms.core.crontab.entity.Scheme;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 任务计划表 服务类
 *
 * @author NodeX
 * @since 2021-01-22
 */
public interface ISchemeService extends BaseService<Scheme> {
	List<Scheme> getSchemeList();
}
