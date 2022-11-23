package org.nodes.wms.biz.report;

import org.nodes.core.constant.DictKVConstant;
import org.nodes.wms.dao.basics.zone.ZoneDao;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.stock.StockDao;
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
	private ZoneDao zoneDao;

	/**
	 * 盘点单
	 */
	public List<ReportCountStockDto> reportCountStock(String dsName, String datasetName, Map<String,Object> parameters){
		List<String> excludeZoneTypeList = Collections.singletonList(
			DictKVConstant.ZONE_TYPE_PICK_TO.toString());
		List<Zone> excludeZoneList = zoneDao.getByZoneType(excludeZoneTypeList);
		List<Long> excludeZoneIdList = excludeZoneList.stream()
			.map(Zone::getZoneId)
			.collect(Collectors.toList());
		return stockDao.getStockBalanceTotalByReportParams(excludeZoneIdList, parameters);
	}

	/**
	 * 实时库存明细
	 */
	public List<ReportStockDto> reportStock(String dsName, String datasetName, Map<String,Object> parameters){
		List<String> excludeZoneTypeList = Collections.singletonList(
			DictKVConstant.ZONE_TYPE_PICK_TO.toString());
		// , DictKVConstant.ZONE_TYPE_VIRTUAL.toString()
		List<Zone> excludeZoneList = zoneDao.getByZoneType(excludeZoneTypeList);
		List<Long> excludeZoneIdList = excludeZoneList.stream()
			.map(Zone::getZoneId)
			.collect(Collectors.toList());
		return stockDao.getCurrentTimeStockByReportParams(excludeZoneIdList, parameters);
	}
}
