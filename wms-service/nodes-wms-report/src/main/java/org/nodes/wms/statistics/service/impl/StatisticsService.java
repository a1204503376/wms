package org.nodes.wms.statistics.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.mapper.SkuMapper;
import org.nodes.wms.core.basedata.service.ISkuLogService;
import org.nodes.wms.core.basedata.vo.IdleSkuInfoVO;
import org.nodes.wms.core.instock.asn.mapper.AsnHeaderMapper;
import org.nodes.wms.core.outstock.so.dto.SoBillCountDTO;
import org.nodes.wms.core.outstock.so.mapper.SoHeaderMapper;
import org.nodes.wms.core.outstock.so.vo.OutstockSkuRltVO;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.stock.core.mapper.StockLogMapper;
import org.nodes.wms.core.stock.core.mapper.StockMapper;
import org.nodes.wms.core.stock.core.vo.LocRateRltVO;
import org.nodes.wms.core.stock.core.vo.UnsafeStockSkuVO;
import org.nodes.wms.core.system.entity.Task;
import org.nodes.wms.core.system.mapper.TaskMapper;
import org.nodes.wms.core.system.vo.TaskVO;
import org.nodes.wms.core.system.wrapper.TaskWrapper;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.statistics.dto.IdleSkuDTO;
import org.nodes.wms.statistics.mapper.StatisticsMapper;
import org.nodes.wms.statistics.service.IStatisticsService;
import org.nodes.wms.statistics.vo.*;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author pengwei
 * @program WmsCore
 * @description 统计服务类
 * @create 20200402
 */
@Slf4j
@Service
@AllArgsConstructor
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class StatisticsService implements IStatisticsService {

	SoHeaderMapper soHeaderMapper;
	AsnHeaderMapper asnHeaderMapper1;
	SkuMapper skuMapper;
	StockMapper stockMapper;
	StockLogMapper stockLogMapper;
	TaskMapper taskMapper;
	StatisticsMapper statisticsMapper;

	ISkuLogService skuLogService;

	@Override
	public BillCountRltVO soBillCount() {
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime yesterday = LocalDateTime.now().plusDays(-1L);

		BillCountRltVO soBillCountRlt = new BillCountRltVO();
		// 获取今天已完成订单量
		int billCountToday = soHeaderMapper.selectFinishBillCount(today);
		soBillCountRlt.setBillCountToday(billCountToday);
		// 获取昨天已完成订单量
		int billCountYesterday = soHeaderMapper.selectFinishBillCount(yesterday);
		soBillCountRlt.setBillCountYesterday(billCountYesterday);

		// 计算比率
		if (billCountYesterday == 0) {
			soBillCountRlt.setRate(0);
		} else {
			double billCountDiff = billCountToday - billCountYesterday;
			double growthRate = billCountDiff / billCountYesterday * 100;
			soBillCountRlt.setRate(new Double(growthRate).intValue());
		}
		// 获取今天已完成订单物品总数
		soBillCountRlt.setSkuCountToday(soHeaderMapper.selectFinishSkuCount(today));

		return soBillCountRlt;
	}

	@Override
	public BillCountRltVO asnBillCount() {
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime yesterday = today.plusDays(-1L);

		BillCountRltVO asnBillCountRlt = new BillCountRltVO();
		// 获取今天已完成订单量
		Map<String, Object> map = asnHeaderMapper1.selectFinishBillCount(today);
//		int billCountToday = asnHeaderMapper.selectFinishBillCount(today);
		Integer billCountToday = 0;
		if (Func.isNotEmpty(map) && Func.isNotEmpty(map.get("counts"))) {
			billCountToday = ((Long) map.get("counts")).intValue();
		}
		asnBillCountRlt.setBillCountToday(billCountToday);
		// 获取昨天已完成订单量
		Map<String, Object> map1 = asnHeaderMapper1.selectFinishBillCount(yesterday);
		Integer billCountYesterday = 0;
		if (Func.isNotEmpty(map1) && Func.isNotEmpty(map1.get("counts"))) {
			billCountYesterday = ((Long) map1.get("counts")).intValue();
		}
		asnBillCountRlt.setBillCountYesterday(billCountYesterday);
		// 计算比率
		if (billCountYesterday == 0) {
			asnBillCountRlt.setRate(0);
		} else {
			double billCountDiff = billCountToday - billCountYesterday;
			double growthRate = billCountDiff / billCountYesterday * 100;
			asnBillCountRlt.setRate(new Double(growthRate).intValue());
		}
		// 获取今天已完成入库单的物品总数
		Map<String, Object> map2 = asnHeaderMapper1.selectFinishSkuCount(today);
		asnBillCountRlt.setSkuCountToday(0);
		if (Func.isNotEmpty(map2) && Func.isNotEmpty(map2.get("counts"))) {
			asnBillCountRlt.setSkuCountToday(((Long) map2.get("counts")).intValue());
		}
		return asnBillCountRlt;
	}

	@Override
	public SkuCountRltVO outstockSkuCount() {
		SkuCountRltVO skuCountRlt = new SkuCountRltVO();
		List<SoBillCountDTO> list = soHeaderMapper.selectBillCount();
		if (ObjectUtil.isNotEmpty(list)) {
			for (SoBillCountDTO soBillCount : list) {
				if (soBillCount.getQty().compareTo(soBillCount.getStockQty()) <= 0) {
					continue;
				}
				skuCountRlt.setCount(skuCountRlt.getCount().add(BigDecimal.ONE));
				skuCountRlt.setQty(skuCountRlt.getQty().add(soBillCount.getQty()).subtract(soBillCount.getStockQty()));
			}
		}
		return skuCountRlt;
	}

	@Override
	public IdleSkuRltVO getIdleSkuInfo(IdleSkuDTO idleSkuDTO) {
		if (Func.isEmpty(idleSkuDTO)) {
			idleSkuDTO = new IdleSkuDTO();
		}
		IdleSkuRltVO idleSkuRlt = new IdleSkuRltVO();
		LocalDateTime now = LocalDateTime.now();
		// 统计呆滞物品的开始/结束时间
		if (Func.isEmpty(idleSkuDTO.getBeginTime())) {
			idleSkuDTO.setBeginTime(now.plusDays(-6).toLocalDate().atStartOfDay());
		}
		if (Func.isEmpty(idleSkuDTO.getEndTime())) {
			idleSkuDTO.setEndTime(now.plusDays(1).toLocalDate().atTime(0, 0, 0).plusSeconds(-1));
		}
		// 上一周的开始/结束时间
		LocalDateTime lastBeginTime = now.minusDays(now.getDayOfWeek().getValue())
			.minusDays(6)
			.toLocalDate()
			.atStartOfDay();
		LocalDateTime lastEndTime = lastBeginTime.plusDays(7).plusSeconds(-1);

		// 获取当前时间段内呆滞物品
		List<IdleSkuInfoVO> currSkuList = skuMapper.selectUnOutstockCount(
			idleSkuDTO.getBeginTime(), idleSkuDTO.getEndTime());
		idleSkuRlt.setCurrSkuCount(currSkuList.size());
		idleSkuRlt.setCurrSkuList(currSkuList);
		idleSkuRlt.setCurrBeginTime(idleSkuDTO.getBeginTime());
		idleSkuRlt.setCurrEndTime(idleSkuDTO.getEndTime());
		// 获取上周呆滞物品数量
		List<IdleSkuInfoVO> lastSkuList = skuMapper.selectUnOutstockCount(lastBeginTime, lastEndTime);
		idleSkuRlt.setLastSkuCount(lastSkuList.size());
		idleSkuRlt.setLastSkuList(lastSkuList);
		idleSkuRlt.setLastBeginTime(lastBeginTime);
		idleSkuRlt.setLastEndTime(lastEndTime);
		// 计算比率
		if (Func.isEmpty(lastSkuList)) {
			idleSkuRlt.setRate(0);
		} else {
			double billCountDiff = currSkuList.size() - lastSkuList.size();
			double growthRate = billCountDiff / lastSkuList.size() * 100;
			idleSkuRlt.setRate(new Double(growthRate).intValue());
		}
		// 获取呆滞物品占用库位总数
		List<Long> skuIdList = NodesUtil.toList(currSkuList, IdleSkuInfoVO::getSkuId);
		List<Stock> stockList = new ArrayList<>();
		if (Func.isNotEmpty(skuIdList)) {
			stockList = stockMapper.selectList(Condition.getQueryWrapper(new Stock()).lambda()
				.in(Stock::getSkuId, skuIdList)
				.groupBy(Stock::getLocId));
		}
		idleSkuRlt.setOccupyLocCount(stockList.size());

		return idleSkuRlt;
	}

	@Override
	public BillTraitRltVO getBillTrait() {
		BillTraitRltVO billTraitRlt = new BillTraitRltVO();

		// 计算起始时间和结束时间
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime beginTime = now.plusDays(-6).toLocalDate().atStartOfDay();
		LocalDateTime endTime = now.plusDays(1).toLocalDate().atTime(0, 0, 0).plusSeconds(-1);
		// 获取出库单所有物品信息
		List<Sku> outstockSkuList = soHeaderMapper.selectAllSku(beginTime, endTime);
		// 计算序列号管理 与 非序列号管理的物品数量
		List<Sku> snList = outstockSkuList.stream()
			.filter(sku -> new Integer(1).equals(sku.getIsSn()))
			.collect(Collectors.toList());
		List<Sku> notSnList = outstockSkuList.stream()
			.filter(sku -> new Integer(0).equals(sku.getIsSn()))
			.collect(Collectors.toList());
		billTraitRlt.setSnCount(snList.size());
		billTraitRlt.setNotSnCount(notSnList.size());

		return billTraitRlt;
	}

	@Override
	public StockSkuCountRltVO getStockSkuCount() {
		StockSkuCountRltVO skuStockInfoRlt = new StockSkuCountRltVO();
		// 计算这个月 起始时间 / 结束时间
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime currBeginTime = now.minusDays(now.getDayOfMonth()).plusDays(1).toLocalDate().atStartOfDay();
		LocalDateTime currEndTime = currBeginTime.plusMonths(1).minusSeconds(1);
		// 获取这个月库存物品数量
		skuStockInfoRlt.setCurrentSkuCount(stockLogMapper.selectSkuCount(currBeginTime, currEndTime));
		// 计算上个月 起始时间 / 结束时间
		LocalDateTime lastBeginTime = currBeginTime.minusMonths(1);
		LocalDateTime lastEndTime = currBeginTime.minusSeconds(1);
		// 获取上个月库存物品数量
		skuStockInfoRlt.setLastSkuCount(stockLogMapper.selectSkuCount(lastBeginTime, lastEndTime));
		// 计算比率
		if (new Integer(0).equals(skuStockInfoRlt.getLastSkuCount())) {
			skuStockInfoRlt.setRate(0);
		} else {
			double billCountDiff = skuStockInfoRlt.getCurrentSkuCount() - skuStockInfoRlt.getLastSkuCount();
			double growthRate = billCountDiff / skuStockInfoRlt.getLastSkuCount() * 100;
			skuStockInfoRlt.setRate(new Double(growthRate).intValue());
		}
		return skuStockInfoRlt;
	}

	@Override
	public LocOccupyRltVO getLocOccupyInfo() {
		LocOccupyRltVO locOccupyRlt = new LocOccupyRltVO();
		// 计算这个月 起始时间 / 结束时间
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime currBeginTime = now.minusDays(now.getDayOfMonth()).plusDays(1).toLocalDate().atStartOfDay();
		LocalDateTime currEndTime = currBeginTime.plusMonths(1).minusSeconds(1);
		// 获取这个月库存物品数量
		locOccupyRlt.setCurrOccupyCount(stockLogMapper.selectLocCount(currBeginTime, currEndTime));
		// 计算上个月 起始时间 / 结束时间
		LocalDateTime lastBeginTime = currBeginTime.minusMonths(1);
		LocalDateTime lastEndTime = currBeginTime.minusSeconds(1);
		locOccupyRlt.setLastOccupyCount(stockLogMapper.selectSkuCount(lastBeginTime, lastEndTime));
		// 计算比率
		if (new Integer(0).equals(locOccupyRlt.getLastOccupyCount())) {
			locOccupyRlt.setRate(0);
		} else {
			double billCountDiff = locOccupyRlt.getCurrOccupyCount() - locOccupyRlt.getLastOccupyCount();
			double growthRate = billCountDiff / locOccupyRlt.getLastOccupyCount() * 100;
			locOccupyRlt.setRate(new Double(growthRate).intValue());
		}
		return locOccupyRlt;
	}

	@Override
	public List<OutstockSkuRltVO> getOutstockSku() {
		// 计算起始时间和结束时间
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime beginTime = now.plusDays(-30).toLocalDate().atStartOfDay();
		LocalDateTime endTime = now.plusDays(1).toLocalDate().atTime(0, 0, 0).plusSeconds(-1);

		List<OutstockSkuRltVO> list = soHeaderMapper.selectOutstockSku(beginTime, endTime);
		final Integer SIZE = 10;
		if (list.size() <= SIZE) {
			return list;
		} else {
			return list.stream().limit(SIZE).collect(Collectors.toList());
		}
	}

	@Override
	public List<LocRateRltVO> getLocRate() {
		// 计算起始时间和结束时间
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime beginTime = now.plusDays(-6).toLocalDate().atStartOfDay();
		LocalDateTime endTime = now.plusDays(1).toLocalDate().atTime(0, 0, 0).plusSeconds(-1);
		// 获取库位热力值
		return stockLogMapper.selectLocRate(beginTime, endTime);
	}


	@Override
	public List<TaskVO> unExecTaskInfo() {
		List<Task> taskList = new ArrayList<>();
		taskMapper.selectList(Condition.getQueryWrapper(new Task())
				.lambda().isNull(Task::getBeginTime)).stream()
			.collect(Collectors.groupingBy(Task::getTaskTypeCd))
			.forEach((taskTypeCd, list) -> {
				Task task = new Task();
				task.setTaskTypeCd(taskTypeCd);
				task.setTaskQty(list.size());
				taskList.add(task);
			});
		return TaskWrapper.build().listVO(taskList);
	}

	@Override
	public List<UnsafeStockSkuVO> getUnsafeStockSkuList() {
		return stockMapper.selectUnsafeStock();
	}

	@Override
	public List<AdventSkuVO> getAdventSkuList() {
		return new ArrayList<AdventSkuVO>();
	}

	@Override
	public List<SkuOutstockSummaryVO> getSkuOutstockSummary() {
		return statisticsMapper.selectSkuOutstockSummary();
	}

	@Override
	public StatisicsAll all() {
		StatisicsAll statisicsAll = new StatisicsAll();

		long beginTime = System.currentTimeMillis();
		statisicsAll.setSoBillCount(this.soBillCount());
		log.info("查询统计-当前出库订单量: " + (System.currentTimeMillis() - beginTime) + " 毫秒");

		long startTime = System.currentTimeMillis();
		statisicsAll.setAsnBillCount(this.asnBillCount());
		log.info("查询统计-当前入库订单量: " + (System.currentTimeMillis() - startTime) + " 毫秒");

		startTime = System.currentTimeMillis();
		statisicsAll.setOutstockSkuCount(this.outstockSkuCount());
		log.info("查询统计-缺货物品数量: " + (System.currentTimeMillis() - startTime) + " 毫秒");

		startTime = System.currentTimeMillis();
		statisicsAll.setIdleSku(this.getIdleSkuInfo(null));
		log.info("查询统计-呆滞物品: " + (System.currentTimeMillis() - startTime) + " 毫秒");

		startTime = System.currentTimeMillis();
		statisicsAll.setBillTrait(this.getBillTrait());
		log.info("查询统计-一周内订单特征: " + (System.currentTimeMillis() - startTime) + " 毫秒");

		startTime = System.currentTimeMillis();
		statisicsAll.setStockSkuCount(this.getStockSkuCount());
		log.info("查询统计-库存物品数量: " + (System.currentTimeMillis() - startTime) + " 毫秒");

		startTime = System.currentTimeMillis();
		statisicsAll.setLocOccupy(this.getLocOccupyInfo());
		log.info("查询统计-库位占用信息: " + (System.currentTimeMillis() - startTime) + " 毫秒");

		startTime = System.currentTimeMillis();
		statisicsAll.setOutstockSkuList(this.getOutstockSku());
		log.info("查询统计-一周内出库物品排行: " + (System.currentTimeMillis() - startTime) + " 毫秒");

		startTime = System.currentTimeMillis();
		statisicsAll.setLocRate(this.getLocRate());
		log.info("查询统计-一周内库位使用频率: " + (System.currentTimeMillis() - startTime) + " 毫秒");

		startTime = System.currentTimeMillis();
		statisicsAll.setTaskList(this.unExecTaskInfo());
		log.info("查询统计-未执行的任务: " + (System.currentTimeMillis() - startTime) + " 毫秒");

		startTime = System.currentTimeMillis();
		statisicsAll.setUnsafeStockSkuList(this.getUnsafeStockSkuList());
		log.info("查询统计-安全库存预警: " + (System.currentTimeMillis() - startTime) + " 毫秒");

		startTime = System.currentTimeMillis();
		statisicsAll.setAdventSkuList(this.getAdventSkuList());
		log.info("查询统计-临期物品预警: " + (System.currentTimeMillis() - startTime) + " 毫秒");

		startTime = System.currentTimeMillis();
		statisicsAll.setSkuOutstockSummary(this.getSkuOutstockSummary());
		log.info("查询统计-物品出库总数: " + (System.currentTimeMillis() - startTime) + " 毫秒");

		long endTime = System.currentTimeMillis();
		long time = endTime - beginTime;
		log.info("查询统计-总计：" + time + "毫秒");
		return statisicsAll;
	}
}
