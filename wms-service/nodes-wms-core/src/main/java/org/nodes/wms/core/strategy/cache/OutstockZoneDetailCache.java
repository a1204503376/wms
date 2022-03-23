package org.nodes.wms.core.strategy.cache;

import org.nodes.wms.core.strategy.entity.OutstockZoneDetail;
import org.nodes.wms.core.strategy.service.IOutstockZoneDetailService;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author pengwei
 * @program WmsCore
 * @description 分配策略货源库区缓存类
 * @since 2020-12-08
 */
public class OutstockZoneDetailCache {

	/*static Map<Long, OutstockZoneDetail> cache = new HashMap<>();

	static {
		IOutstockZoneDetailService outstockZoneDetailService = SpringUtil.getBean(IOutstockZoneDetailService.class);

		outstockZoneDetailService.list().forEach(outstockZoneDetail -> {
			cache.put(outstockZoneDetail.getStozdId(), outstockZoneDetail);
		});
	}

	public static List<OutstockZoneDetail> listBySsodId(Long ssodId) {
		return cache.values().stream().filter(u -> {
			return u.getSsodId().equals(ssodId);
		}).collect(Collectors.toList());
	}

	public static OutstockZoneDetail getById(Long stozdId) {
		return cache.getOrDefault(stozdId, null);
	}

	public static void saveOrUpdate(OutstockZoneDetail outstockZoneDetail) {
		OutstockZoneDetail findObj = getById(outstockZoneDetail.getStozdId());
		if (Func.isEmpty(findObj)) {
			cache.put(outstockZoneDetail.getStozdId(), outstockZoneDetail);
		} else {
			cache.replace(outstockZoneDetail.getStozdId(), outstockZoneDetail);
		}
	}

	public static void remove(Serializable id) {
		cache.remove(id);
	}

	public static void remove(Collection<? extends Serializable> idList) {
		idList.forEach(id -> {
			remove(id);
		});
	}

	public static void removeBySsodId(Long ssodId) {
		cache.values().removeIf(new Predicate<OutstockZoneDetail>() {
			@Override
			public boolean test(OutstockZoneDetail outstockZoneDetail) {
				return outstockZoneDetail.getSsodId().equals(ssodId);
			}
		});
	}*/
}
