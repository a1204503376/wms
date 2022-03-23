package org.nodes.wms.core.warehouse.cache;

import org.nodes.wms.core.warehouse.entity.WorkArea;
import org.nodes.wms.core.warehouse.service.IWorkAreaService;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.tool.utils.SpringUtil;

/**
 * 工作区缓存
 *
 * @Author zx
 * @Date 2019/12/24
 **/
public class WorkAreaCache {
	//工作区id
	private static final String WORKAREA_CACHE_ID = "workarea:id";

	public static final String WORK_AREA_CAHCE = "wms.workarea";

	private static IWorkAreaService workAreaService;

	static {
		workAreaService = SpringUtil.getBean(IWorkAreaService.class);
	}

	/**
	 * 获得工作区信息
	 * @param id
	 * @return
	 */
	public static WorkArea getById(Long id) {
		return CacheUtil.get(WORK_AREA_CAHCE, WORKAREA_CACHE_ID, id, () -> workAreaService.getById(id));
	}

}
