package org.nodes.wms.biz.instock.receiveLog.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.config.DateTimeFinals;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.biz.basics.owner.OwnerBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.instock.receive.ReceiveBiz;
import org.nodes.wms.biz.instock.receiveLog.ReceiveLogBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.instock.receive.dto.input.PdaByPieceReceiveRequest;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailLpnPdaRequest;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveDetailLpnItemDto;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetail;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetailLpn;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.nodes.wms.dao.instock.receiveLog.ReceiveLogDao;
import org.nodes.wms.dao.instock.receiveLog.dto.input.ReceiveLogPageQuery;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogExcelResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogIndexResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogPageResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogResponse;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 清点记录业务层实现类
 */
@Service
@RequiredArgsConstructor
public class ReceiveLogBizImpl implements ReceiveLogBiz {
	private final ReceiveLogDao receiveLogDao;
	private final LocationBiz locationBiz;
	private final ReceiveBiz receiveBiz;
	private final OwnerBiz ownerBiz;
	private final StockBiz stockBiz;


	@Override
	public List<ReceiveLogResponse> getReceiveLogList(Long receiveId) {
		return receiveLogDao.getReceiveLogList(receiveId);
	}

	@Override
	public List<ReceiveLogIndexResponse> findReceiveSkuQtyTop10() {
		return receiveLogDao.getReceiveSkuQtyTop10();
	}

	@Override
	public ReceiveLog newReceiveLog(PdaByPieceReceiveRequest request, ReceiveHeader receiveHeader, ReceiveDetail detail) {
		ReceiveLog receiveLog = new ReceiveLog();
		receiveLog.setReceiveId(request.getReceiveId());
		receiveLog.setReceiveDetailId(request.getReceiveDetailId());
		receiveLog.setBoxCode(request.getBoxCode());
		if (request.getIsSn()) {
			receiveLog.setSnCode(String.join(",", request.getSerialNumberList()));
		}
		receiveLog.setWhCode(request.getWhCode());
		receiveLog.setQty(request.getSurplusQty());
		receiveLog.setLpnCode(request.getBoxCode());
		receiveLog.setLocCode(request.getLocCode());
		receiveLog.setSkuCode(request.getSkuCode());
		receiveLog.setSkuName(request.getSkuName());
		receiveLog.setSkuSpec(request.getSkuLot2());
		receiveLog.setWhId(request.getWhId());
		receiveLog.setWsuCode(request.getWsuCode());
		ReceiveLog log = createReceiveLog(receiveLog, receiveHeader, detail);
		log.setSkuLot1(request.getSkuLot1());
		log.setSkuLot2(request.getSkuLot2());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeFinals.DATE_FORMAT);
		log.setSkuLot3(LocalDateTime.now().format(formatter));
		receiveLogDao.save(log);
		return log;
	}

	ReceiveLog createReceiveLog(ReceiveLog receiveLog, ReceiveHeader receiveHeader, ReceiveDetail detail) {
		SkuLotUtil.setAllSkuLot(receiveLog, detail);
		Location location = locationBiz.findLocationByLocCode(receiveLog.getWhId(), receiveLog.getLocCode());
		receiveLog.setReceiveNo(receiveHeader.getReceiveNo());
		receiveLog.setAsnBillId(receiveHeader.getAsnBillId());
		receiveLog.setAsnBillNo(receiveHeader.getAsnBillNo());
		receiveLog.setLineNo(detail.getLineNo());
		receiveLog.setLocId(location.getLocId());
		receiveLog.setWhCode(detail.getWhCode());
		receiveLog.setSkuId(detail.getSkuId());
		receiveLog.setWspId(detail.getWspId());
		receiveLog.setSkuLevel(detail.getSkuLevel());
		receiveLog.setSkuLot1(detail.getSkuLot1());
		receiveLog.setSkuLot2(detail.getSkuLot2());
		Owner owner;
		if (Func.isNotEmpty(detail.getWoId())) {
			owner = ownerBiz.findById(detail.getWoId());
		} else {
			owner = ownerBiz.findById(receiveHeader.getWoId());
		}
		if (Func.isNotEmpty(owner)) {
			receiveLog.setWoId(owner.getWoId());
			receiveLog.setOwnerCode(owner.getOwnerCode());
		}
		return receiveLog;
	}

	@Override
	public Page<ReceiveLogPageResponse> page(Query query, ReceiveLogPageQuery receiveLogPageQuery) {
		return receiveLogDao.page(Condition.getPage(query), receiveLogPageQuery);
	}

	@Override
	public void exportExcel(ReceiveLogPageQuery receiveLogPageQuery, HttpServletResponse response) {
		List<ReceiveLogExcelResponse> receiveLogList
			= receiveLogDao.getReceiveLogListByQuery(receiveLogPageQuery);
		ExcelUtil.export(response, "收货记录", "收货记录", receiveLogList, ReceiveLogExcelResponse.class);
	}

	@Override
	public ReceiveLog newReceiveLog(ReceiveDetailLpnPdaRequest request, ReceiveDetailLpnItemDto item, ReceiveDetailLpn lpn, ReceiveHeader receiveHeader, ReceiveDetail detail) {
		ReceiveLog receiveLog = new ReceiveLog();
		receiveLog.setReceiveId(request.getReceiveHeaderId());
		receiveLog.setReceiveDetailId(item.getReceiveDetailId());
		receiveLog.setBoxCode(request.getBoxCode());
		receiveLog.setSnCode(lpn.getSnCode());
		receiveLog.setQty(item.getPlanQty());
		receiveLog.setLpnCode(request.getLpnCode());
		receiveLog.setLocCode(request.getLocCode());
		receiveLog.setSkuCode(item.getSkuCode());
		receiveLog.setSkuName(item.getSkuName());
		receiveLog.setSkuSpec(request.getSkuLot2());
		receiveLog.setWhId(request.getWhId());
		receiveLog = createReceiveLog(receiveLog, receiveHeader, detail);
		receiveLogDao.save(receiveLog);
		return receiveLog;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean cancelReceive(List<Long> idList) {
		//根据记录id查询收货记录
		List<ReceiveLog> receiveLogList = receiveLogDao.getReceiveLogListByIdList(idList);
		receiveLogList.forEach(item -> {
			// 判断收货记录中是否存在撤销数为负数的，有就抛异常
			if (BigDecimalUtil.lt(item.getQty(), BigDecimal.ZERO)) {
				throw new ServiceException("撤销失败，选择的记录中不允许有已撤销的记录");
			}
		});
		//货主、物品、库位、状态、箱码、LPNCode、30个批属性相同的才合并
		// 先根据货主、物品、库位、状态、箱码、LPNCode分组
		Map<String, List<ReceiveLog>> collect = receiveLogList.stream().collect(Collectors.groupingBy(
			item -> item.getWhId() + "_" + item.getSkuId() + "_" + item.getLocId() + "_" + item.getBoxCode()
				+ "_" + item.getLpnCode())
		);
		List<ReceiveLog> finalReceiveLogList = new ArrayList<>();
		//遍历分组得到得map
		for (Map.Entry<String, List<ReceiveLog>> entry : collect.entrySet()
		) {
			List<ReceiveLog> value = entry.getValue();
			//根据货主等字段分组后，若结果只有一条数据，直接加到最终的收货记录集合中
			if (value.size() == 1) {
				finalReceiveLogList.add(value.get(0));
				continue;
			}
			// 临时集合接收下面循环时的下标
			List<Integer> tempList = new ArrayList<>();
			tempList.add(-1);
			// 遍历每个分组中的ReceiveLog集合，拿第一个元素和后面的每个元素比较
			//
			for (int i = 0; i < value.size() && !tempList.contains(i); i++) {
				// 设置最终的收货数量为当前元素的数量
				BigDecimal sumQty = value.get(i).getQty();
				for (int j = i + 1; j < value.size() && !tempList.contains(j); j++) {
					boolean flag = SkuLotUtil.compareAllSkuLot(value.get(i), value.get(j));
					// 校验通过
					if (flag) {
						// 最终收货数量为进行比较的两个元素的和
						sumQty = value.get(i).getQty().add(value.get(j).getQty());
						// 将比较的元素的下标放入临时集合中，下次遍历时，跳过该元素
						tempList.add(j);
					}
				}
				// 将比较的元素的下标放入临时集合中，下次遍历时，跳过该元素
				tempList.add(i);
				value.get(i).setQty(sumQty);
				finalReceiveLogList.add(value.get(i));
			}
		}

		finalReceiveLogList.forEach(item -> {
			// 校验是否存在多个库存
			Stock stock = stockBiz.findStockOnStage(item);
			// 更新库存
			item.setQty(item.getQty().negate()); // qty取负数
			stockBiz.outStockByCancleReceive(StockLogTypeEnum.OUTSTOCK_BY_CANCEL_RECEIVE, item, stock);
			// 新增撤销收货记录
			item.setId(null);
			receiveLogDao.save(item);
			// 查询收货明细
			ReceiveDetail receiveDetail = receiveBiz.getDetailByReceiveDetailId(item.getReceiveDetailId());
			// 更新收货明细
			receiveBiz.updateReceiveDetail(receiveDetail, item.getQty());
			// 查询收货头表
			ReceiveHeader receiveHeader = receiveBiz.getReceiveHeaderById(item.getReceiveId());
			// 更新收货头表
			receiveBiz.updateReciveHeader(receiveHeader, receiveDetail);
		});
		return true;
	}
}
