package org.nodes.wms.core.stock.core.cache;

import org.nodes.wms.core.stock.core.entity.Lot;
import org.nodes.wms.core.stock.core.service.ILotService;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.nodes.core.base.cache.CacheNames.NODES_FLASH;

/**
 * @program: WmsCore
 * @description: 批次号缓存类
 * @author: pengwei
 * @create: 2021-01-11 10:34
 **/
public class LotCache {

	static final Map<Long, Lot> cache = new HashMap<>();

	public static final String LAST_LOT_CACHE_ID = "lastlot:";

	private static final String LAST_LOT_CACHE_KEY = "new";

	private static final String LOT_PREFIX = "LOT";

	private static final int MIN_VALUE = 1;

	private static final int MAX_VALUE = 999999;

	private static DecimalFormat decimalFormat = null;

	private static ILotService lotService;

	public static String FinalLot = null;

	static {
		lotService = SpringUtil.getBean(ILotService.class);
		lotService.list().forEach(lot->{
			cache.put(lot.getLotId(), lot);
		});
		decimalFormat = new DecimalFormat("000000");
	}

	/**
	 * 创建批次号（调用时注意在外部方法上添加 synchronized 关键字）
	 *
	 * @return 批次号
	 */
	public static String create() {
		if (!Func.isEmpty(FinalLot)) {
			return FinalLot;
		}
		// 从缓存取出最近一次生成的批次号
		Lot lot = CacheUtil.get(NODES_FLASH, LAST_LOT_CACHE_ID, LAST_LOT_CACHE_KEY, () -> {
			return lotService.getOne(Condition.getQueryWrapper(new Lot())
				.lambda()
				.orderByDesc(Lot::getCreateTime)
				.last("limit 1"));
		});
		// 获取当前的时间
		LocalDateTime now = LocalDateTime.now();
		// 日期YYMMDD
		String date = String.format(
			"%s%s%s",
			now.getYear(),
			new DecimalFormat("00").format(now.getMonthValue()),
			new DecimalFormat("00").format(now.getDayOfMonth()));

		if (Func.isEmpty(lot)) {
			lot = new Lot();
			// 如果还没创建过批次号，则从最小值开始
			lot.setLotNumber(String.format("%s%s%s", LOT_PREFIX, date, decimalFormat.format(MIN_VALUE)));
		} else {
			// 拆分批次号
			String lastDate = lot.getLotNumber().substring(3, 11);        // 日期
			String lastSerial = lot.getLotNumber().substring(11, 17);    // 流水号
			// 判断是否在同一个日期
			if (date.equals(lastDate)) {
				// 日期相同，判断流水号是否达到最大值
				int serial = Integer.parseInt(lastSerial);
				if (serial >= MAX_VALUE) {
					throw new ServiceException("今天流水号已达到最大值！");
				}
				lot.setLotNumber(String.format("%s%s%s", LOT_PREFIX, date, decimalFormat.format(serial + 1)));
			} else {
				// 日期不同，流水号从最小值开始
				lot.setLotNumber(String.format("%s%s%s", LOT_PREFIX, date, decimalFormat.format(MIN_VALUE)));
			}
		}
		CacheUtil.put(NODES_FLASH, LAST_LOT_CACHE_ID, LAST_LOT_CACHE_KEY, lot);
		return lot.getLotNumber();
	}

	/*public static List<Lot> list(){
		return new ArrayList(cache.values());
	}

	public static Lot getById(Long id) {
		return cache.getOrDefault(id, null);
	}

	public static Lot getByLotNumber(String lotNumber) {
		return cache.values().stream().filter(u->{
			return u.getLotNumber().equals(lotNumber);
		}).findFirst().orElse(null);
	}

	public static void saveOrUpdate(Lot lot) {
		Lot findObj = getById(lot.getLotId());
		if (Func.isEmpty(findObj)) {
			cache.put(lot.getLotId(), lot);
		} else {
			cache.replace(lot.getLotId(), lot);
		}
	}*/
}
