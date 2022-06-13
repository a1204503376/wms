package org.nodes.wms.core.crontab.cache;

import org.nodes.wms.dao.crontab.entity.CrontabTask;
import org.nodes.wms.biz.crontab.ICrontabTaskService;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.*;

/**
 * @program: WmsCore
 * @description: 定时任务缓存
 * @author: pengwei
 * @create: 2021-01-24 09:44
 **/
public class CrontabTaskCache {

	public static final String CRONTAB_TASK_CACHE = "crontab_task_cache";

	static final String CRONTAB_TASK_ID = "crontab_task:id:";

	static final String CRONTAB_TASK_LIST = "crontab_task:list:";

	static ICrontabTaskService crontabTaskService;

	static {
		crontabTaskService = SpringUtil.getBean(ICrontabTaskService.class);
	}

	public static CrontabTask getById(Long id){
		return CacheUtil.get(CRONTAB_TASK_CACHE, CRONTAB_TASK_ID, id, () -> crontabTaskService.getById(id));
	}

	public static List<CrontabTask> list() {
		return CacheUtil.get(CRONTAB_TASK_CACHE, CRONTAB_TASK_LIST, "data", () -> crontabTaskService.list());
	}
}
