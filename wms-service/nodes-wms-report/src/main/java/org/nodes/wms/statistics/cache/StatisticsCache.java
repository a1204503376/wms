package org.nodes.wms.statistics.cache;

import org.nodes.wms.statistics.service.IStatisticsService;
import org.nodes.wms.statistics.vo.StatisicsAll;
import org.springblade.core.tool.utils.SpringUtil;


/**
 * @author pengwei
 * @program WmsCore
 * @description 查询统计缓存类
 * @since 2020-12-09
 */
public class StatisticsCache {

	public static StatisicsAll cache = new StatisicsAll();

	static IStatisticsService statisticsService;

	static {
		statisticsService = SpringUtil.getBean(IStatisticsService.class);

		refreshAll();
	}

	public static void refreshAll() {
		synchronized (cache) {
 			cache = statisticsService.all();
		}
	}
}
