package org.nodes.wms.core.crontab.cache;

import org.nodes.wms.dao.crontab.entity.Scheme;
import org.nodes.wms.core.crontab.service.ISchemeService;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.*;

/**
 * @program: WmsCore
 * @description: 定时计划缓存类
 * @author: pengwei
 * @create: 2021-01-24 10:07
 **/
public class SchemeCache {

	public static final String SCHEME_CACHE = "wms:scheme";

	static final String SCHEME_LIST = "scheme:list:";

	static ISchemeService schemeService;


	static {
		schemeService = SpringUtil.getBean(ISchemeService.class);
	}

	public static List<Scheme> list(){
		return CacheUtil.get(SCHEME_CACHE, SCHEME_LIST, "all", ()-> schemeService.list());
	}
}
