package org.nodes.wms.biz.report;

import org.nodes.core.constant.DictKVConstant;
import org.nodes.wms.biz.basics.skuType.SkuTypeBiz;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.stock.StockDao;
import org.nodes.wms.dao.stock.dto.report.ReportAgeOfInventoryDto;
import org.nodes.wms.dao.stock.dto.report.ReportCountStockDto;
import org.nodes.wms.dao.stock.dto.report.ReportStockDto;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Ureport库存报表类
 **/
@Component
public class ReportStock {

	@Resource
	private StockDao stockDao;
	@Resource
	private ZoneBiz zoneBiz;
	@Resource
	private SkuTypeBiz skuTypeBiz;

	/**
	 * 盘点单
	 */
	public List<ReportCountStockDto> reportCountStock(String dsName, String datasetName, Map<String,Object> parameters){
		return stockDao.getStockBalanceTotalByReportParams(getExcludeZoneIdList(), parameters);
	}

	/**
	 * 实时库存明细
	 */
	public List<ReportStockDto> reportStock(String dsName, String datasetName, Map<String,Object> parameters){
		List<ReportStockDto> result = new LinkedList<>();
		List<ReportStockDto> reportStockDtoList = stockDao.getCurrentTimeStockByReportParams(getExcludeZoneIdList(), parameters);
		List<SkuType> skuTypeAll = skuTypeBiz.findAll();
		Long xpzpId = skuTypeAll.stream()
			.filter(x -> "新品闸片".equals(x.getTypeName()))
			.map(SkuType::getSkuTypeId)
			.findFirst().orElse(null);

		Long jxzpId = skuTypeAll.stream()
			.filter(x -> "检修闸片".equals(x.getTypeName()))
			.map(SkuType::getSkuTypeId)
			.findFirst().orElse(null);

		Long tzpjId = skuTypeAll.stream()
			.filter(x -> "套装配件".equals(x.getTypeName()))
			.map(SkuType::getSkuTypeId)
			.findFirst().orElse(null);

		List<ReportStockDto> xpzpList = reportStockDtoList.stream()
			.filter(x -> x.getSkuTypeId().equals(xpzpId))
			.collect(Collectors.toList());
		List<ReportStockDto> jxzpList = reportStockDtoList.stream()
			.filter(x -> x.getSkuTypeId().equals(jxzpId))
			.collect(Collectors.toList());
		List<ReportStockDto> tzpjList = reportStockDtoList.stream()
			.filter(x -> x.getSkuTypeId().equals(tzpjId))
			.collect(Collectors.toList());

		result.addAll(xpzpList);
		result.addAll(jxzpList);

		// 处理套装配件
		Map<String, List<ReportStockDto>> listMap = tzpjList.stream()
			.collect(Collectors.groupingBy(ReportStockDto::getSkuLot2));

		for (Map.Entry<String, List<ReportStockDto>> entry: listMap.entrySet()){
			List<ReportStockDto> value = entry.getValue();
			ReportStockDto mck = value.stream().filter(x -> "摩擦块".equals(x.getSkuName())).findFirst().orElse(null);
			ReportStockDto xgb = value.stream().filter(x -> "新钢背".equals(x.getSkuName())).findFirst().orElse(null);
			ReportStockDto gb = value.stream().filter(x -> "钢背".equals(x.getSkuName())).findFirst().orElse(null);
			ReportStockDto jxgb = value.stream().filter(x -> "检修钢背".equals(x.getSkuName())).findFirst().orElse(null);
			ReportStockDto xsjt = value.stream().filter(x -> "新三角托".equals(x.getSkuName())).findFirst().orElse(null);
			ReportStockDto jxsjt = value.stream().filter(x -> "检修三角托".equals(x.getSkuName())).findFirst().orElse(null);
			ReportStockDto kh = value.stream().filter(x -> "卡簧".equals(x.getSkuName())).findFirst().orElse(null);
			if (Func.isNotEmpty(mck)){
				result.add(mck);
			}
			if (Func.isNotEmpty(gb)){
				result.add(gb);
			}
			if (Func.isNotEmpty(xgb)){
				result.add(xgb);
			}
			if (Func.isNotEmpty(jxgb)){
				result.add(jxgb);
			}
			if (Func.isNotEmpty(xsjt)){
				result.add(xsjt);
			}
			if (Func.isNotEmpty(jxsjt)){
				result.add(jxsjt);
			}
			if (Func.isNotEmpty(kh)){
				result.add(kh);
			}
		}
		return result;
	}

	/**
	 * 库龄
	 */
	public List<ReportAgeOfInventoryDto> reportAgeOfInventory(String dsName, String datasetName, Map<String,Object> parameters){
		List<Long> excludeZoneIdList = getExcludeZoneIdList();
		return stockDao.getAgeOfInventory(excludeZoneIdList);
	}

	public List<Long> getExcludeZoneIdList(){
		List<String> excludeZoneTypeList = Collections.singletonList(
			DictKVConstant.ZONE_TYPE_PICK_TO.toString());
		// , DictKVConstant.ZONE_TYPE_VIRTUAL.toString()
		List<Zone> excludeZoneList = zoneBiz.findByZoneType(excludeZoneTypeList);
		return excludeZoneList.stream()
			.map(Zone::getZoneId)
			.collect(Collectors.toList());
	}
}
