package org.nodes.wms.core.basedata.cache;

import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
import org.nodes.wms.dao.basics.sku.enums.SkuLevelEnum;
import org.nodes.wms.core.basedata.service.ISkuPackageDetailService;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.nodes.wms.core.basedata.cache.SkuPackageCache.SKU_PACKAGE_CACHE;

/**
 * @author pengwei
 * @program WmsCore
 * @description 物品包装明细缓存操作类
 * @since 2020-08-27
 */
public class SkuPackageDetailCache {

	static final String SKU_PACKAGE_DETAIL_ID = "sku_package_detail:id:";

	static final String SKU_PACKAGE_DETAIL_WSP_ID = "sku_package_detail:wsp_id";

	static final String SKU_PACKAGE_DETAIL_WSP_ID_AND_SKU_LEVEL = "sku_package_detail:wsp_id&sku_level";

	static ISkuPackageDetailService skuPackageDetailService;

	static {
		skuPackageDetailService = SpringUtil.getBean(ISkuPackageDetailService.class);
	}

	/**
	 * 获取指定包装下的包装明细
	 *
	 * @param wspId 包装ID
	 * @return 包装明细列表
	 */
	public static List<SkuPackageDetail> listByWspId(Long wspId) {
		return CacheUtil.get(SKU_PACKAGE_CACHE, SKU_PACKAGE_DETAIL_WSP_ID, wspId, () -> {
			return skuPackageDetailService.list(Condition.getQueryWrapper(new SkuPackageDetail())
				.lambda()
				.eq(SkuPackageDetail::getWspId, wspId));
		});
	}

	/**
	 * 获取包装明细
	 *
	 * @param wspId    包装ID
	 * @param skuLevel 包装层级
	 * @return 包装明细
	 */
	public static SkuPackageDetail getOne(Long wspId, Integer skuLevel) {
		return CacheUtil.get(SKU_PACKAGE_CACHE, SKU_PACKAGE_DETAIL_WSP_ID_AND_SKU_LEVEL,
			wspId + StringPool.AMPERSAND + skuLevel,
			() -> {
				return skuPackageDetailService.getOne(Condition.getQueryWrapper(new SkuPackageDetail())
					.lambda()
					.eq(SkuPackageDetail::getWspId, wspId)
					.eq(SkuPackageDetail::getSkuLevel, skuLevel)
					.last("limit 1"));
			});
	}

	/**
	 * 获取包装明细
	 *
	 * @param wspId    包装ID
	 * @param skuLevel 包装层级
	 * @return 包装明细
	 */
	public static SkuPackageDetail getOne(Long wspId, SkuLevelEnum skuLevel) {
		return getOne(wspId, skuLevel.getIndex());
	}


	/**
	 * 获取包装明细
	 *
	 * @param wspdId 包装明细ID
	 * @return 包装明细
	 */
	public static SkuPackageDetail getById(Long wspdId) {
		return CacheUtil.get(SKU_PACKAGE_CACHE, SKU_PACKAGE_DETAIL_ID, wspdId, () -> {
			return skuPackageDetailService.getById(wspdId);
		});
	}

	/**
	 * @param wspId    包装id
	 * @param skuLevel 0：会输出所有包装层级  指定层级：会输出指定层级 如果isNext设为true会输出包含指定层级及以下层级
	 * @param qty      基础单位数量
	 * @param isNext   配合skuLevel使用（见skulevel参数说明）
	 * @return
	 */
	public static String convert1(@NotNull Long wspId, @NotNull int skuLevel, BigDecimal qty, boolean isNext) {
		BigDecimal[] val = new BigDecimal[1];
		Arrays.fill(val, qty);
		List<SkuPackageDetail> skuPackageDetailList = listByWspId(wspId).stream()
			.sorted(Comparator.comparingInt(SkuPackageDetail::getSkuLevel).reversed())
			.collect(Collectors.toList());
		String collect = skuPackageDetailList.stream().map(skuPackageDetail -> {
			BigDecimal convertQty = new BigDecimal(skuPackageDetail.getConvertQty());
			if (skuLevel > 0 && !isNext && !skuPackageDetail.getSkuLevel().equals(skuLevel)) {
				return "";
			}
			if (skuLevel > 0 && isNext && skuPackageDetail.getSkuLevel() > skuLevel) {
				return "";
			}
			BigDecimal[] bigDecimals = val[0].divideAndRemainder(convertQty);
			if (BigDecimalUtil.eq(bigDecimals[0], BigDecimal.ZERO)) {
				return "";
			}
			val[0] = bigDecimals[1];
			int value = bigDecimals[0].setScale(0, BigDecimal.ROUND_DOWN).intValue();
			return value + skuPackageDetail.getWsuName();
		}).collect(Collectors.joining());
		if (Func.isEmpty(collect)) {
			SkuPackageDetail one = SkuPackageDetailCache.getOne(wspId, skuLevel);
			if (Func.isEmpty(one)) {
				return "0";
			}
			return "0" + one.getWsuName();
		}
		return collect;
	}


	public static String convert(@NotNull Long wspId, @NotNull int skuLevel, BigDecimal qty) {
		if (wspId == 0L || skuLevel == 0 || Func.isEmpty(skuLevel)) {
			return StringPool.EMPTY;
		}
		// 降序
		List<SkuPackageDetail> skuPackageDetailList = listByWspId(wspId).stream()
			.sorted(Comparator.comparingInt(SkuPackageDetail::getSkuLevel).reversed())
			.collect(Collectors.toList());
		SkuPackageDetail skuPackageDetail = skuPackageDetailList.stream().filter(u -> {
			return u.getSkuLevel().equals(skuLevel);
		}).findFirst().orElse(null);
		if (Func.isEmpty(skuPackageDetail)) {
			throw new ServiceException("包装[ID:" + wspId + "]中不存在层级为" + skuLevel + "的包装明细");
		}

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < skuPackageDetailList.size(); i++) {
			SkuPackageDetail item = skuPackageDetailList.get(i);
			if (item.getSkuLevel() > skuPackageDetail.getSkuLevel()) {
				continue;
			}
			// 查找下当前明细下的，下个层级
			SkuPackageDetail nextItem = null;
			if (i < skuPackageDetailList.size() - 1) {
				nextItem = skuPackageDetailList.get(i + 1);
			}
			BigDecimal convertQty = new BigDecimal(skuPackageDetail.getConvertQty().toString());
			BigDecimal[] array = qty.divideAndRemainder(convertQty);
			if (BigDecimalUtil.eq(BigDecimal.ZERO, qty)) {
				sb.append("0").append(item.getWsuName());
			} else if (skuLevel == 1) {
				sb.append(BigDecimalUtil.globeScale(qty).toPlainString()).append(item.getWsuName());
			} else if (BigDecimalUtil.lt(qty, convertQty)) {
				if (Func.isEmpty(nextItem)) {
					throw new ServiceException("计量单位转换异常(可能原因:转换数量不能整除且没有基础计量单位)");
				}
				sb.append(BigDecimalUtil.globeScale(qty).toPlainString()).append(nextItem.getWsuName());
			} else {
				if (BigDecimalUtil.eq(array[1], BigDecimal.ZERO)) {
					// 可以整除
					sb.append(BigDecimalUtil.globeScale(qty.divide(convertQty)).toPlainString()).append(item.getWsuName());
				} else {
					if (Func.isEmpty(nextItem)) {
						throw new ServiceException("计量单位转换异常(可能原因:转换数量不能整除且没有基础计量单位)");
					}
					// 不能整除
					sb.append(BigDecimalUtil.globeScale(array[0]).toPlainString())
						.append(item.getWsuName())
						.append(convert(wspId, nextItem.getSkuLevel(), array[1]));
				}
			}
			break;
		}

		return sb.toString();
	}
}
