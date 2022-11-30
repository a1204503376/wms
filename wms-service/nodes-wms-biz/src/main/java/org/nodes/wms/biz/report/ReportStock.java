package org.nodes.wms.biz.report;

import org.nodes.core.constant.DictKVConstant;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.stock.StockDao;
import org.nodes.wms.dao.stock.dto.report.ReportAgeOfInventoryDto;
import org.nodes.wms.dao.stock.dto.report.ReportCountStockDto;
import org.nodes.wms.dao.stock.dto.report.ReportStockDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
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
		return stockDao.getCurrentTimeStockByReportParams(getExcludeZoneIdList(), parameters);
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
