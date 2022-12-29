package org.nodes.wms.biz.report;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.outstock.logSoPick.LogSoPickBiz;
import org.nodes.wms.biz.outstock.plan.SoPickPlanBiz;
import org.nodes.wms.biz.outstock.so.SoBillBiz;
import org.nodes.wms.biz.stock.SerialBiz;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageAggregate;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.dao.basics.sku.entities.SkuUm;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.outstock.so.dto.report.ReportSoPickLotDto;
import org.nodes.wms.dao.outstock.so.dto.report.ReportSoPickSerialDto;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.ConvertUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringPool;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Ureport发货报表类
 **/
@Component
@RequiredArgsConstructor
public class ReportSo {

	private final SoBillBiz soBillBiz;
	private final SoPickPlanBiz soPickPlanBiz;
	private final LogSoPickBiz logSoPickBiz;
	private final SkuBiz skuBiz;
	private final SerialBiz serialBiz;

	/*
	  如果单据没有分配或没有全部拣货则提示用户无法导出，请分配或全部拣货之后再导出。
	  如果单据已分配但没拣货，则从拣货计划表中读取明细数据。
	  如果单据已全部拣货或已关闭，则从拣货记录中读取明细数据。
	 */

	/**
	 * 导出销售发货记录表(批次) 头部数据集
	 */
	public List<Map<String, Object>> reportSoPickLotHeader(String dsName, String datasetName, Map<String, Object> parameters) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		SoHeader soHeader = getSoHeader(parameters);
		if (isCreate(soHeader.getSoBillState())) {
			throw new ServiceException("无法导出, 请分配或全部拣货之后再导出。");
		} else if (isPart(soHeader.getSoBillState())) {
			throw new ServiceException("无法导出, 请全部拣货之后再导出。");
		} else if (isExecuting(soHeader.getSoBillState())) {
			List<SoPickPlan> soPickPlanList = getSoPickPlanList(soHeader.getSoBillId());
			if (Func.isNotEmpty(soPickPlanList)) {
				map.put("skuLot2", soPickPlanList.get(0).getSkuLot2());
				map.put("wsuName", getWsuName(soPickPlanList.get(0).getSkuId()));
			}
		} else if (isAllOutStockOrCompleted(soHeader.getSoBillState())) {
			List<LogSoPick> logSoPickList = getLogSoPickList(soHeader.getSoBillId());
			if (Func.isNotEmpty(logSoPickList)) {
				map.put("skuLot2", logSoPickList.get(0).getSkuLot2());
				map.put("wsuName", getWsuName(logSoPickList.get(0).getSkuId()));
			}
		} else {
			throw new ServiceException("无法导出, 请稍后再试。");
		}
		map.put("createTime", soHeader.getCreateTime());
		map.put("soBillNo", "编码：" + soHeader.getSoBillNo());
		map.put("customerName", soHeader.getCustomerName());
		mapList.add(map);
		return mapList;
	}

	/**
	 * 导出销售发货记录表(批次) 列表数据集
	 */
	public List<ReportSoPickLotDto> reportSoPickLotList(String dsName, String datasetName, Map<String, Object> parameters) {
		SoHeader soHeader = getSoHeader(parameters);
		List<ReportSoPickLotDto> reportSoPickLotDtoList = new ArrayList<>();
		if (isCreate(soHeader.getSoBillState())) {
			throw new ServiceException("无法导出, 请分配或全部拣货之后再导出。");
		} else if (isPart(soHeader.getSoBillState())) {
			throw new ServiceException("无法导出, 请全部拣货之后再导出。");
		} else if (isExecuting(soHeader.getSoBillState())) {
			List<SoPickPlan> soPickPlanList = getSoPickPlanList(soHeader.getSoBillId());
			for (SoPickPlan soPickPlan : soPickPlanList) {
				ReportSoPickLotDto reportSoPickLotDto = new ReportSoPickLotDto();
				reportSoPickLotDto.setBoxCode(soPickPlan.getBoxCode());
				reportSoPickLotDto.setSkuName(soPickPlan.getSkuName());
				reportSoPickLotDto.setSkuLot9(soPickPlan.getSkuLot9());
				reportSoPickLotDto.setSkuLot5(soPickPlan.getSkuLot5());
				reportSoPickLotDto.setQty(soPickPlan.getPickPlanQty());
				reportSoPickLotDto.setWsuName(getWsuName(soPickPlan.getSkuId()));
				reportSoPickLotDtoList.add(reportSoPickLotDto);
			}
		} else if (isAllOutStockOrCompleted(soHeader.getSoBillState())) {
			List<LogSoPick> logSoPickList = getLogSoPickList(soHeader.getSoBillId());
			for (LogSoPick logSoPick : logSoPickList) {
				ReportSoPickLotDto reportSoPickLotDto = new ReportSoPickLotDto();
				reportSoPickLotDto.setBoxCode(logSoPick.getBoxCode());
				reportSoPickLotDto.setSkuName(logSoPick.getSkuName());
				reportSoPickLotDto.setSkuLot9(logSoPick.getSkuLot9());
				reportSoPickLotDto.setSkuLot5(logSoPick.getSkuLot5());
				reportSoPickLotDto.setQty(logSoPick.getPickRealQty());
				reportSoPickLotDto.setWsuName(getWsuName(logSoPick.getSkuId()));
				reportSoPickLotDtoList.add(reportSoPickLotDto);
			}
		} else {
			throw new ServiceException("无法导出, 请稍后再试。");
		}
		List<ReportSoPickLotDto> finalDtoList = new ArrayList<>();
		Map<String, List<ReportSoPickLotDto>> listGroupByBoxCode = reportSoPickLotDtoList.stream().collect(Collectors.groupingBy(ReportSoPickLotDto::getBoxCode));
		for (Map.Entry<String, List<ReportSoPickLotDto>> entry : listGroupByBoxCode.entrySet()) {
			for (int i = 0; i < entry.getValue().size(); i++) {
				if (i != 0) {
					entry.getValue().get(i).setBoxCode(null);
				}
			}
			finalDtoList.addAll(entry.getValue());
		}
		return finalDtoList;
	}

	public SoHeader getSoHeader(Map<String, Object> parameters) {
		Long soBillId = ConvertUtil.convert(parameters.get("soBillId"), Long.class);
		return soBillBiz.getSoHeaderById(soBillId);
	}

	public List<SoPickPlan> getSoPickPlanList(Long soBillId) {
		return soPickPlanBiz.findBySoHeaderId(soBillId);
	}

	public List<LogSoPick> getLogSoPickList(Long soBillId) {
		List<LogSoPick> logSoPickList = logSoPickBiz.findBySoHeaderId(soBillId);
		// 过滤掉已撤销的记录和撤销记录
		return logSoPickList.stream()
			.filter(x -> Func.isEmpty(x.getCancelLogId()) && BigDecimalUtil.gt(x.getPickRealQty(), BigDecimal.ZERO))
			.collect(Collectors.toList());
	}

	public String getWsuName(Long skuId) {
		SkuPackageAggregate skuPackageAggregate = skuBiz.findSkuPackageAggregateBySkuId(skuId);
		List<SkuPackageDetail> skuPackageDetailList = skuPackageAggregate.getSkuPackageDetailList();
		SkuUm skuUm = skuBiz.findSkuUmById(skuPackageDetailList.get(0).getWsuId());
		return skuUm.getWsuName();
	}

	public boolean isCreate(SoBillStateEnum stateEnum) {
		return SoBillStateEnum.CREATE.equals(stateEnum);
	}

	public boolean isPart(SoBillStateEnum stateEnum) {
		return SoBillStateEnum.PART.equals(stateEnum);
	}

	public boolean isExecuting(SoBillStateEnum stateEnum) {
		return SoBillStateEnum.EXECUTING.equals(stateEnum);
	}

	public boolean isAllOutStockOrCompleted(SoBillStateEnum stateEnum) {
		return SoBillStateEnum.ALL_OUT_STOCK.equals(stateEnum) || SoBillStateEnum.COMPLETED.equals(stateEnum);
	}

	public List<String> getSerial(Long stockId) {
		return serialBiz.findSerialNoByStockId(stockId);
	}

	/**
	 * 生成处理好的序列号与序列号个数
	 */
	public List<Map<String, Object>> getProcessedSerialAndQty(String snCodeStr) {
		List<String> snCodeStrList = Arrays.asList(Func.split(snCodeStr, ","));
		List<String> snCodeList = snCodeStrList.stream()
			.sorted().collect(Collectors.toList());
		List<Map<String, Object>> mapList = new ArrayList<>();
		int startIndex = 0;
		for (int i = 0; i < snCodeList.size(); i++) {
			Map<String, Object> map = new HashMap<>();
			if (i == snCodeList.size() - 1) {
				if (startIndex == i) {
					map.put("snCode", getInterceptionSnCode(snCodeList.get(i)));
					map.put("qty", 1);
				} else {
					map.put("snCode", getInterceptionSnCode(snCodeList.get(startIndex)) + StringPool.DASH + getInterceptionSnCode(snCodeList.get(i)));
					map.put("qty", i - startIndex + 1);
				}
				mapList.add(map);
				break;
			}
			// 255987000075221220241  =>  221220241
			Integer snCode1 = getInterceptionSnCode(snCodeList.get(i));
			Integer snCode2 = getInterceptionSnCode(snCodeList.get(i + 1));

			if (snCode1 + 1 != snCode2) {
				if (startIndex == i) {
					map.put("snCode", getInterceptionSnCode(snCodeList.get(i)));
					map.put("qty", 1);
				} else {
					map.put("snCode", getInterceptionSnCode(snCodeList.get(startIndex)) + StringPool.DASH + getInterceptionSnCode(snCodeList.get(i)));
					map.put("qty", i - startIndex + 1);
				}
				mapList.add(map);
				startIndex = i + 1;
			}
		}
		return mapList;
	}

	public Integer getInterceptionSnCode(String snCode) {
		return ConvertUtil.convert(StringUtil.sub(snCode, 13, snCode.length()), Integer.class);
	}

	/**
	 * 根据 logSoPick 封装 销售发货记录表(序列号) 列表数据集的数据
	 */
	public void setReportSoPickSerialDtoList(LogSoPick logSoPick, List<ReportSoPickSerialDto> reportSoPickSerialDtoList) {
		if (Func.isNotEmpty(logSoPick.getSnCode())) {
			List<Map<String, Object>> mapList = getProcessedSerialAndQty(logSoPick.getSnCode());
			packagingData(mapList, logSoPick.getSkuLot9(), logSoPick.getBoxCode(), logSoPick.getSkuLot5(),
				logSoPick.getSoBillId(), reportSoPickSerialDtoList);
		}
	}

	/**
	 * 根据 soPickPlan 封装 销售发货记录表(序列号) 列表数据集的数据
	 */
	public void setReportSoPickSerialDtoList(SoPickPlan soPickPlan, List<ReportSoPickSerialDto> reportSoPickSerialDtoList) {
		List<String> serialList = getSerial(soPickPlan.getStockId());
		String snCodeStr = "";
		if (Func.isNotEmpty(serialList)) {
			snCodeStr = String.join(",", serialList);
		}
		List<Map<String, Object>> mapList = getProcessedSerialAndQty(snCodeStr);
		packagingData(mapList, soPickPlan.getSkuLot9(), soPickPlan.getBoxCode(), soPickPlan.getSkuLot5(),
			soPickPlan.getSoBillId(), reportSoPickSerialDtoList);
	}

	/**
	 * 封装数据
	 */
	private void packagingData(List<Map<String, Object>> mapList, String skuLot9, String boxCode, String skuLot5, Long soBillId,
							   List<ReportSoPickSerialDto> reportSoPickSerialDtoList) {
		for (Map<String, Object> stringObjectMap : mapList) {
			ReportSoPickSerialDto reportSoPickSerialDto = new ReportSoPickSerialDto();
			reportSoPickSerialDto.setBoxCode(boxCode);
			reportSoPickSerialDto.setSnCode(ConvertUtil.convert(stringObjectMap.get("snCode"), String.class));
			reportSoPickSerialDto.setQty(ConvertUtil.convert(stringObjectMap.get("qty"), BigDecimal.class));
			reportSoPickSerialDto.setSkuLot9(skuLot9);
			reportSoPickSerialDto.setSkuLot5(skuLot5);
			reportSoPickSerialDto.setSoBillId(soBillId);
			reportSoPickSerialDtoList.add(reportSoPickSerialDto);
		}
	}

	/**
	 * 导出销售发货记录表(序列号) 头部数据集
	 */
	public List<Map<String, Object>> reportSoPickSerialHeader(String dsName, String datasetName, Map<String, Object> parameters) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		SoHeader soHeader = getSoHeader(parameters);
		if (isCreate(soHeader.getSoBillState())) {
			throw new ServiceException("无法导出, 请分配或全部拣货之后再导出。");
		} else if (isPart(soHeader.getSoBillState())) {
			throw new ServiceException("无法导出, 请全部拣货之后再导出。");
		} else if (isExecuting(soHeader.getSoBillState())) {
			List<SoPickPlan> soPickPlanList = getSoPickPlanList(soHeader.getSoBillId());
			if (Func.isNotEmpty(soPickPlanList)) {
				map.put("skuLot2", soPickPlanList.get(0).getSkuLot2());
				map.put("wsuName", getWsuName(soPickPlanList.get(0).getSkuId()));
				map.put("skuLot7", soPickPlanList.get(0).getSkuLot7());
			}
		} else if (isAllOutStockOrCompleted(soHeader.getSoBillState())) {
			List<LogSoPick> logSoPickList = getLogSoPickList(soHeader.getSoBillId());
			if (Func.isNotEmpty(logSoPickList)) {
				map.put("skuLot2", logSoPickList.get(0).getSkuLot2());
				map.put("wsuName", getWsuName(logSoPickList.get(0).getSkuId()));
				map.put("skuLot7", logSoPickList.get(0).getSkuLot7());
			}
		} else {
			throw new ServiceException("无法导出, 请稍后再试。");
		}
		map.put("soBillNo", "编码：" + soHeader.getSoBillNo());
		map.put("customerName", soHeader.getCustomerName());
		mapList.add(map);
		return mapList;
	}

	/**
	 * 导出销售发货记录表(序列号) 列表数据集
	 */
	public List<ReportSoPickSerialDto> reportSoPickSerialList(String dsName, String datasetName, Map<String, Object> parameters) {
		SoHeader soHeader = getSoHeader(parameters);
		List<ReportSoPickSerialDto> reportSoPickSerialDtoList = new ArrayList<>();
		if (isCreate(soHeader.getSoBillState())) {
			throw new ServiceException("无法导出, 请分配或全部拣货之后再导出。");
		} else if (isPart(soHeader.getSoBillState())) {
			throw new ServiceException("无法导出, 请全部拣货之后再导出。");
		} else if (isExecuting(soHeader.getSoBillState())) {
			List<SoPickPlan> soPickPlanList = getSoPickPlanList(soHeader.getSoBillId());
			soPickPlanList.forEach(soPickPlan -> {
				setReportSoPickSerialDtoList(soPickPlan, reportSoPickSerialDtoList);
			});
		} else if (isAllOutStockOrCompleted(soHeader.getSoBillState())) {
			List<LogSoPick> logSoPickList = getLogSoPickList(soHeader.getSoBillId());
			logSoPickList.forEach(logSoPick -> {
				setReportSoPickSerialDtoList(logSoPick, reportSoPickSerialDtoList);
			});
		} else {
			throw new ServiceException("无法导出, 请稍后再试。");
		}
		List<ReportSoPickSerialDto> finalDtoList = new ArrayList<>();
		Map<String, List<ReportSoPickSerialDto>> listGroupByBoxCode = reportSoPickSerialDtoList.stream().collect(Collectors.groupingBy(ReportSoPickSerialDto::getBoxCode));
		for (Map.Entry<String, List<ReportSoPickSerialDto>> entry : listGroupByBoxCode.entrySet()) {
			for (int i = 0; i < entry.getValue().size(); i++) {
				if (i != 0) {
					entry.getValue().get(i).setBoxCode(null);
				}
			}
			finalDtoList.addAll(entry.getValue());
		}
		return finalDtoList;
	}
}
