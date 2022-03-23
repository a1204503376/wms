package org.nodes.wms.core.strategy.cache;

import org.nodes.wms.core.strategy.entity.InstockDetail;
import org.nodes.wms.core.strategy.service.IOutstockService;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.nodes.wms.core.strategy.entity.Outstock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.nodes.core.base.cache.CacheNames.NODES_FLASH;

/**
 * 分配策略缓存
 *
 * @Author zx
 * @Date 2019/12/24
 **/
public class OutstockCache {

	/*private static IOutstockService outstockService;

	static final Map<Serializable, Outstock> cache = new HashMap<>();

	static {
		outstockService = SpringUtil.getBean(IOutstockService.class);
		outstockService.list().forEach(item->cache.put(item.getSsoId(), item));
	}
	*//**
	 * 修改或更新分配策略到缓存
	 *
	 * @param Outstock 上架策略明细
	 * @return 是否成功
	 *//*
	public static void saveOrUpdate(Outstock outstock) {
		Outstock findObj = getById(outstock.getSsoId());
		if (Func.isEmpty(findObj)) {
			cache.put(outstock.getSsoId(), outstock);
		} else {
			cache.replace(outstock.getSsoId(), outstock);
		}
	}

	*//**
	 * 获得分配策略信息
	 *
	 * @param id
	 * @return
	 *//*
	public static Outstock getById(Serializable id) {
		return cache.getOrDefault(id, null);
	}

	public static void removeById(Serializable id) {
		cache.remove(id);
	}

	public static List<Outstock> list(){
		return new ArrayList(cache.values());
	}*/
}
