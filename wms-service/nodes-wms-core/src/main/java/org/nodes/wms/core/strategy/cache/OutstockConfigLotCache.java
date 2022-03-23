package org.nodes.wms.core.strategy.cache;

import org.nodes.wms.core.strategy.entity.OutstockConfigLot;
import org.nodes.wms.core.strategy.service.IOutstockConfigLotService;
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
 * @description 分配策略批属性设定缓存类
 * @since 2020-12-08
 */
public class OutstockConfigLotCache {

	/*static Map<Long, OutstockConfigLot> cache = new HashMap<>();

	static {
		IOutstockConfigLotService outstockConfigLotService = SpringUtil.getBean(IOutstockConfigLotService.class);

		outstockConfigLotService.list().forEach(outstockConfigLot -> {
			cache.put(outstockConfigLot.getSsoclId(), outstockConfigLot);
		});
	}

	public static List<OutstockConfigLot> listBySsodId(Long ssodId) {
		return cache.values().stream().filter(u -> {
			return u.getSsodId().equals(ssodId);
		}).collect(Collectors.toList());
	}

	public static OutstockConfigLot getById(Long ssoclId) {
		return cache.getOrDefault(ssoclId, null);
	}

	public static void saveOrUpdate(OutstockConfigLot outstockConfigLot) {
		OutstockConfigLot findObj = getById(outstockConfigLot.getSsoclId());
		if (Func.isEmpty(findObj)) {
			cache.put(outstockConfigLot.getSsoclId(), outstockConfigLot);
		} else {
			cache.replace(outstockConfigLot.getSsoclId(), outstockConfigLot);
		}
	}

	public static void remove(Serializable id) {
		cache.remove(id);
	}

	public static void remove(Collection<? extends Serializable> idList) {
		idList.forEach(id->{
			remove(id);
		});
	}

	public static void removeBySsodId(Long ssodId) {
		cache.values().removeIf(new Predicate<OutstockConfigLot>() {
			@Override
			public boolean test(OutstockConfigLot outstockConfigLot) {
				return outstockConfigLot.getSsodId().equals(ssodId);
			}
		});
	}*/
}
