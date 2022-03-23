package org.nodes.core.tool.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 序列号缓存类
 *
 * @Author pengwei
 * @Date 2020-01-19
 **/
@Component
public class SerialNoCache {


	private static RedisTemplate<String, Object> redisTemplate;

	@Resource
	public void setRedisTemplate(RedisTemplate redisTemplate) {
		SerialNoCache.redisTemplate = redisTemplate;
	}

	private static Map<String, Long> MaxCount = new HashMap<String, Long>();

	private static Map<String, Long> SerialCount = new HashMap<String, Long>();

	private final static Long SerialStep = 300L;
	private final static String SerialKeyStart = "WMS:SQNO_";
	//箱号redis缓存key
	final static String BOX_CODE_KEY = "BoxCode";


	/**
	 * 获取序列号
	 *
	 * @param serialKey 键值
	 * @return 序列号
	 */
	public static synchronized Long getSerialNo(String serialKey) {
		return redisTemplate.opsForValue().increment(
			SerialKeyStart + serialKey, 1L);
	}


	/**
	 * 获取序列号
	 *
	 * @param serialKey 键值
	 * @param length    补零长度
	 * @return 序列号
	 */
	public static synchronized String getSerialNo(String serialKey, int length) {
		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		numberFormat.setMinimumIntegerDigits(length);
		numberFormat.setGroupingUsed(false);

		return numberFormat.format(getSerialNo(serialKey));
	}

	/**
	 * 获取带日期的序列号
	 *
	 * @param serialKey 键值
	 * @param length    补零长度
	 * @return 带日期的序列号
	 */
	public static String getSerialDateNo(String serialKey, int length) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String dateStr = LocalDateTime.now().format(formatter);

		String key = String.format("%s_%s", serialKey, dateStr);
		Long serialNo = getSerialNo(key);

		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		numberFormat.setMinimumIntegerDigits(length);
		numberFormat.setGroupingUsed(false);

		return dateStr + numberFormat.format(serialNo);
	}

	/**
	 * 获取入库单单号
	 *
	 * @return 入库单单号
	 */
	public static String getPoNo() {
		return "PO" + getSerialDateNo("Po", 8);
	}

	/**
	 * 获取盘点单单号
	 *
	 * @return 盘点单单号
	 */
	public static String getCNo() {
		return "C" + getSerialDateNo("C", 8);
	}



	/**
	 * 获取车次编号
	 *
	 * @return 车次编号
	 */
	public static String getTcNo() {
		return "TC" + getSerialDateNo("TC", 8);
	}

	/**
	 * 获取波次编号
	 *
	 * @return 波次编号
	 */
	public static Long getWellenNo() {
		return Long.valueOf(getSerialDateNo("Wellen", 6));
	}

	/**
	 * 生成任务分组编码
	 *
	 * @return 任务分组编码
	 */
	public static String getTaskGroup() {
		return getSerialDateNo("TaskGroup", 6);
	}

	/**
	 * 生成容器编码
	 *
	 * @return 容器编码
	 */
	public static String getLpnCode() {
		return "LPN" + getSerialNo("LpnCode", 10);
	}


	/**
	 * 按箱收货 生成箱号
	 * @return
	 */
	public static String createBoxCode(){
		RedisAtomicLong counter = new RedisAtomicLong(BOX_CODE_KEY,redisTemplate.getConnectionFactory());
		counter.getAndIncrement();
		new RedisAtomicLong(BOX_CODE_KEY,redisTemplate.getConnectionFactory(),counter.get());
		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		numberFormat.setMinimumIntegerDigits(10);
		numberFormat.setGroupingUsed(false);
		return numberFormat.format(counter.get());
	}

}
